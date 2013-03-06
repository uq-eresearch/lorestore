package net.metadata.openannotation.lorestore.servlet;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple extension of ModelAndView that uses a default view
 * of name 'ore', and allows the setting of return status and
 * location headers.
 * 
 * @author uqdayers
 */
public class LorestoreResponse extends ModelAndView {

	public static final String MODEL_KEY = "rdfmodel";
	public static final String MODELSET_KEY = "rdfmodelset";
	public static final String ORE_PROPS_KEY = "oreProperties";
	public static final String LOCATION_HEADER = "locationHeader";
	public static final String RETURN_STATUS = "returnStatus";
	public static final String OVERRIDE_CTYPE = "overrideContentType";
	
	public LorestoreResponse(String view) {
		super(view);
	}
	
	public void setRDFModel(Model model){
	    this.addObject(MODEL_KEY,model);
	}
	
	public void setRDFModelSet(ModelSet modelset){
	    this.addObject(MODELSET_KEY,modelset);
	}
	
	public void setReturnStatus(int status) {
		this.addObject(RETURN_STATUS, status);
	}

	public int getReturnStatus() {
		return (Integer)this.getModelMap().get(RETURN_STATUS);
	}
	
	public void setLocationHeader(String locationHeader) {
		this.addObject(LOCATION_HEADER, locationHeader);
	}
	
	public String getLocationHeader() {
		return (String)this.getModelMap().get(LOCATION_HEADER);
	}
	public void setOverrideContentType(String ctype) {
	    this.addObject(OVERRIDE_CTYPE, ctype);
	}
	public String getOverrideContentType() {
	    return (String)this.getModelMap().get(OVERRIDE_CTYPE);
	}
}
