package acme.features.inventor.chimpum;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.Artifact;
import acme.entities.chimpum.Chimpum;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;

@Service
public class ChimpumCreateService implements AbstractCreateService<Inventor, Chimpum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ChimpumRepository repository;

	// AbstractCreateService<Inventor, Chimpum> interface --------------


	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;

		return true;
	}

	@Override
	public Chimpum instantiate(final Request<Chimpum> request) {
		assert request != null;

		Chimpum result;

		result = new Chimpum();
		result.setArtefact(this.repository.findArtifactList().get(0));

		return result;
	}

	@Override
	public void bind(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		final Date moment= new Date(System.currentTimeMillis() - 1);

		request.bind(entity, errors, "code", "title", "description", "period", "budget", "link");
		
		entity.setCreationMoment(moment);
		
	}

	@Override
	public void validate(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		errors.state(request, entity.getBudget().getAmount() > 0, "budget", "inventor.Chimpum.code.repeated.retailPrice.non-negative");
		errors.state(request, entity.getPeriod().after(entity.getCreationMoment()), "period", "inventor.Chimpum.period.order-error");
		
		final LocalDateTime startDate = entity.getPeriod().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		
		final LocalDateTime finishDate = entity.getCreationMoment().toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
		
		errors.state(request, Duration.between(finishDate, startDate).toDays() > 30, "period", "inventor.Chimpum.period.duration-error");
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "title", "description", "period", "budget", "link");
		model.setAttribute("isNew", true);
		model.setAttribute("artifact", this.repository.findArtifactList());
	}

	@Override
	public void create(final Request<Chimpum> request, final Chimpum entity) {
		assert request != null;
		assert entity != null;

		final Artifact art=this.repository.findArtifactById(request.getModel().getInteger("artifactId"));
		
		entity.setArtefact(art);
		
		this.repository.save(entity);
	}

}