package acme.features.inventor.brid;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.brid.Brid;
import acme.framework.controllers.AbstractController;
import acme.roles.Inventor;

@Controller
public class BridController extends AbstractController<Inventor, Brid>{
	// Internal state ---------------------------------------------------------

		@Autowired
		protected BridListService	listService;

		@Autowired
		protected BridShowService	showService;
		
		@Autowired
		protected BridUpdateService	updateService;
		
		@Autowired
		protected BridCreateService	createService;
		
		@Autowired
		protected BridDeleteService	deleteService;

		// Constructors -----------------------------------------------------------


		@PostConstruct
		protected void initialise() {
			super.addCommand("list", this.listService);
			super.addCommand("show", this.showService);
			super.addCommand("update", this.updateService);
			super.addCommand("create", this.createService);
			super.addCommand("delete", this.deleteService);
		}

}
