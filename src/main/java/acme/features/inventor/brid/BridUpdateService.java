package acme.features.inventor.brid;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.Artifact;
import acme.entities.brid.Brid;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;
import acme.systemSetting.SystemSetting;

@Service
public class BridUpdateService implements AbstractUpdateService<Inventor, Brid>{

	@Autowired
	protected BridRepository repository;

	@Override
	public boolean authorise(final Request<Brid> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Brid> request, final Brid entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "creationMoment" ,"theme", "summary", "period", "helping", "furtherInfo");
		model.setAttribute("isNew", false);
		List<Artifact> listArt = this.repository.findArtifactList();
		Artifact a = new Artifact();
		listArt.add(0, a);
		a = entity.getArtefact();
		if(entity.getArtefact() != null) {
			listArt.add(0, a);
		}
		
		model.setAttribute("artifact", listArt);
	}

	@Override
	public Brid findOne(final Request<Brid> request) {
		assert request != null;

		Brid result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneBridById(id);

		return result;
	}

	@Override
	public void bind(Request<Brid> request, Brid entity, Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "creationMoment", "theme", "summary", "period", "helping", "furtherInfo");
	}

	@Override
	public void validate(Request<Brid> request, Brid entity, Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		final SystemSetting s = this.repository.findSystemSetting();
		if(entity.getHelping()!=null) {
			errors.state(request, s.getAcceptedCurrencies().contains(entity.getHelping().getCurrency()) ,
					  "retailPrice", "inventor.artifact.retailPrice.not-able-currency");
		errors.state(request, entity.getHelping().getAmount() > 0, "helping", "inventor.brid.code.repeated.retailPrice.non-negative");
		}else {
			errors.state(request, entity.getHelping()!=null, "retailPrice", "inventor.artifact.retailPrice.null");
		}
		
		errors.state(request, entity.getPeriod().after(entity.getCreationMoment()), "period", "inventor.brid.period.order-error");
		
		final LocalDateTime startDate = entity.getPeriod().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		
		final LocalDateTime finishDate = entity.getCreationMoment().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		errors.state(request, Duration.between(finishDate, startDate).toDays() > 30, "period", "inventor.brid.period.duration-error");
		
		Brid ch = this.repository.findAnyBridByCode(entity.getCode());
		errors.state(request, ch==null, "code", "inventor.brid.period.code-error");
	}

	@Override
	public void update(Request<Brid> request, Brid entity) {
		assert request !=null;
		assert entity !=null;
		
		final Artifact art=this.repository.findArtifactById(request.getModel().getInteger("artifactId"));
		
		entity.setArtefact(art);
		
		this.repository.save(entity);
		
	}
	
	

}
