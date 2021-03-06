package acme.features.inventor.brid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.brid.Brid;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Inventor;

@Service
public class BridListService implements AbstractListService<Inventor, Brid>{
	
	// Internal state ---------------------------------------------------------

		@Autowired
		protected BridRepository repository;

		// AbstractListService<Patron, Brid>  interface -------------------------


		@Override
		public boolean authorise(final Request<Brid> request) {
			assert request != null;

			return true;
		}
		
		@Override
		public Collection<Brid> findMany(final Request<Brid> request) {
			assert request != null;

			Collection<Brid> result;

			result = this.repository.findManyBrid();

			return result;
		}
		
		@Override
		public void unbind(final Request<Brid> request, final Brid entity, final Model model) {
			assert request != null;
			assert entity != null;
			assert model != null;

			request.unbind(entity, model, "code", "theme", "summary");
		}

}
