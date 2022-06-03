package acme.features.inventor.brid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.artifact.Artifact;
import acme.entities.brid.Brid;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;

@Service
public class BridShowService implements AbstractShowService<Inventor, Brid>{
	
	// Internal state ---------------------------------------------------------

		@Autowired
		protected BridRepository repository;

		// AbstractShowService<Inventor, Brid> interface --------------------------

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

			request.unbind(entity, model, "code", "creationMoment", "theme", "summary", "period", "helping", "furtherInfo");
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

		

}
