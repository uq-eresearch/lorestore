package oreservlet.servlets;

import org.springframework.web.servlet.ModelAndView;

import au.edu.diasb.annotation.danno.model.RDFContainer;

public class OREResponse extends ModelAndView {

	public static final String RESPONSE_RDF_KEY = "responseRDF";
	public static final String ORE_PROPS_KEY = "oreProperties";
	
	public OREResponse(RDFContainer resContainer) {
		// The name of the view we will use
		super("ore");
		this.addObject(RESPONSE_RDF_KEY, resContainer);
	}

}
