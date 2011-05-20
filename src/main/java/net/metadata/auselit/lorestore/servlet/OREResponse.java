package net.metadata.auselit.lorestore.servlet;

import org.ontoware.rdf2go.model.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple extension of ModelAndView that uses a default view
 * of name 'ore', and allows the setting of return status and
 * location headers.
 * 
 * @author uqdayers
 */
public class OREResponse extends ModelAndView {

	public static final String RESPONSE_RDF_KEY = "responseRDF";
	public static final String ORE_PROPS_KEY = "oreProperties";
	public static final String LOCATION_HEADER = "locationHeader";
	public static final String RETURN_STATUS = "returnStatus";
	
	public OREResponse(Model model) {
		// The name of the view we will use
		super("ore");
		this.addObject(RESPONSE_RDF_KEY, model);
	}
	
	public void setReturnStatus(int status) {
		this.addObject(RETURN_STATUS, status);
	}

	public int getReturnStatus() {
		return (Integer)this.getModelMap().get(RETURN_STATUS);
	}
	
	public void setLocationHeaer(String locationHeader) {
		this.addObject(LOCATION_HEADER, locationHeader);
	}
	
	public String getLocationHeader() {
		return (String)this.getModelMap().get(LOCATION_HEADER);
	}
}
