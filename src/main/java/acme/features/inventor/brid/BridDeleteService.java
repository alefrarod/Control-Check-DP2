package acme.features.inventor.brid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.brid.Brid;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Inventor;

@Service
public class BridDeleteService implements AbstractDeleteService<Inventor, Brid> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected BridRepository repository;


	@Override
	public boolean authorise(final Request<Brid> request) {
		assert request != null;
		
		return true;
	}

	@Override
	public void validate(final Request<Brid> request, final Brid entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		
	}

	@Override
	public void bind(final Request<Brid> request, final Brid entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "code", "theme", "summary", "period", "helping", "furtherInfo");
		
	}

	@Override
	public void unbind(final Request<Brid> request, final Brid entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "theme", "summary", "period", "helping", "furtherInfo");
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
	public void delete(Request<Brid> request, Brid entity) {
		assert request != null;
		assert entity != null;
		
		
		this.repository.delete(entity);
		
	}

	

}
