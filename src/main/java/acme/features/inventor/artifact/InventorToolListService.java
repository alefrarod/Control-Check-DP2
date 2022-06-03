package acme.features.inventor.artifact;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.Artifact;
import acme.artifact.ArtifactType;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Inventor;

@Service
public class InventorToolListService implements AbstractListService<Inventor, Artifact> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorArtifactRepository repository;

	// AbstractListService<Anonymous, Artifact>  interface -------------------------


	@Override
	public boolean authorise(final Request<Artifact> request) {
		assert request != null;

		return true;
	}
	
	@Override
	public Collection<Artifact> findMany(final Request<Artifact> request) {
		assert request != null;

		Collection<Artifact> result;

		final int i = request.getPrincipal().getActiveRoleId();
		
		result = this.repository.findManyTool(ArtifactType.TOOL, i);

		return result;
	}
	
	@Override
	public void unbind(final Request<Artifact> request, final Collection<Artifact> entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		model.setAttribute("isTool", true);
		
	}
	
	@Override
	public void unbind(final Request<Artifact> request, final Artifact entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "name", "code", "description", "retailPrice");
		
		
	}

}
