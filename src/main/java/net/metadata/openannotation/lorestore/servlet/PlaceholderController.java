package net.metadata.openannotation.lorestore.servlet;

import net.metadata.openannotation.lorestore.exceptions.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class PlaceholderController {
	private final LoreStoreControllerConfig occ;
	

	public PlaceholderController(LoreStoreControllerConfig occ) {
		this.occ = occ;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> get(@PathVariable("id") String oreId)
			throws NotFoundException, InterruptedException,
			RequestFailureException {
		return new ResponseEntity<String>("Placeholder identifier used by LORE", HttpStatus.OK);
	}
	
	public LoreStoreControllerConfig getControllerConfig() {
		return occ;
	}
}
