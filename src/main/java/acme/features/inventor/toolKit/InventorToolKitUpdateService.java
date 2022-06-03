package acme.features.inventor.toolKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.ToolKit;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;

@Service
public class InventorToolKitUpdateService implements AbstractUpdateService<Inventor, ToolKit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorToolKitRepository repository;

	// AbstractUpdateService<Employer, Job> -------------------------------------


	@Override
	public boolean authorise(final Request<ToolKit> request) {
		assert request != null;

		boolean result;
		int masterId;
		ToolKit art;
		Inventor inv;

		masterId = request.getModel().getInteger("id");
		art = this.repository.findOneToolKitById(masterId);
		inv = art.getInventor();
		result = request.isPrincipal(inv);

		return result;
	}

	@Override
	public void validate(final Request<ToolKit> request, final ToolKit entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		ToolKit ch = this.repository.findAnyToolKitByCode(entity.getCode());
		errors.state(request, ch==null, "code", "inventor.brid.period.code-error");
		
	}

	@Override
	public void bind(final Request<ToolKit> request, final ToolKit entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "code", "title", "description", "assemblyNotes", "link");
		
	}

	@Override
	public void unbind(final Request<ToolKit> request, final ToolKit entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "title", "description", "assemblyNotes", "link", "isPublish");
	}

	@Override
	public ToolKit findOne(final Request<ToolKit> request) {
		assert request != null;

		ToolKit result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneToolKitById(id);

		return result;
	}

	@Override
	public void update(final Request<ToolKit> request, final ToolKit entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
