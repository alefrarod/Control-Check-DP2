package acme.features.inventor.brid;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEFAULTS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.Artifact;
import acme.entities.brid.Brid;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;
import acme.systemSetting.SystemSetting;

@Service
public class BridCreateService implements AbstractCreateService<Inventor, Brid> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected BridRepository repository;

	// AbstractCreateService<Inventor, Brid> interface --------------


	@Override
	public boolean authorise(final Request<Brid> request) {
		assert request != null;

		return true;
	}

	@Override
	public Brid instantiate(final Request<Brid> request) {
		assert request != null;

		Brid result;
		result = new Brid();
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy:MM:dd");
		String formattedString = localDate.format(formatter);
		result.setCode(formattedString);
		result.setArtefact(this.repository.findArtifactList().get(0));

		return result;
	}

	@Override
	public void bind(final Request<Brid> request, final Brid entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		final Date moment= new Date(System.currentTimeMillis() - 1);

		request.bind(entity, errors, "theme", "summary", "period", "helping", "furtherInfo");
		
		entity.setCreationMoment(moment);
		
	}

	@Override
	public void validate(final Request<Brid> request, final Brid entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		final SystemSetting s = this.repository.findSystemSetting();
		if(entity.getHelping()!=null) {
			errors.state(request, s.getAcceptedCurrencies().contains(entity.getHelping().getCurrency()) ,
				  "retailPrice", "inventor.artifact.retailPrice.not-able-currency");
		errors.state(request, entity.getHelping().getAmount() > 0, "helping", "inventor.brid.code.repeated.retailPrice.non-negative");
		}else {
			errors.state(request, entity.getHelping() !=null, "helping", "inventor.brid.code.repeated.retailPrice.non-null");
		}
		
		if(entity.getPeriod()!=null) {
		
		errors.state(request, entity.getPeriod().after(entity.getCreationMoment()), "period", "inventor.brid.period.order-error");
		final LocalDateTime startDate = entity.getPeriod().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		
		final LocalDateTime finishDate = entity.getCreationMoment().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		
		errors.state(request, Duration.between(finishDate, startDate).toDays() > 30, "period", "inventor.brid.period.duration-error");
		}else {
			errors.state(request, entity.getPeriod()!=null, "period", "inventor.brid.period.null-error");
		}
		
		
		
		Brid ch = this.repository.findAnyBridByCode(entity.getCode());
		errors.state(request, ch==null, "code", "inventor.brid.period.code-error");
	}

	@Override
	public void unbind(final Request<Brid> request, final Brid entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "theme", "summary", "period", "helping", "furtherInfo");
		model.setAttribute("isNew", true);
		List<Artifact> listArt = this.repository.findArtifactList();
		Artifact a = new Artifact();
		listArt.add(a);
		model.setAttribute("artifact", listArt);
	}

	@Override
	public void create(final Request<Brid> request, final Brid entity) {
		assert request != null;
		assert entity != null;

		final Artifact art=this.repository.findArtifactById(request.getModel().getInteger("artifactId"));
		
		entity.setArtefact(art);
		
		this.repository.save(entity);
	}

}
