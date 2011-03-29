package oreservlet.servlets;

import org.ontoware.rdf2go.model.Model;
import org.springframework.web.servlet.ModelAndView;

public class OREResponse extends ModelAndView {

	public static final String RESPONSE_RDF_KEY = "responseRDF";
	public static final String ORE_PROPS_KEY = "oreProperties";
	
	public OREResponse(Model model) {
		// The name of the view we will use
		super("ore");
		this.addObject(RESPONSE_RDF_KEY, model);
	}

}
