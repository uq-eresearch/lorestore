package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DCTERMS_CREATED;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DCTERMS_MODIFIED;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OA_ANNOTATED_AT_PROPERTY;

import java.util.Date;

import net.metadata.openannotation.lorestore.access.LoreStoreIdentityProvider;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.OpenAnnotation;
import net.metadata.openannotation.lorestore.model.rdf2go.NamedGraphImpl;
import net.metadata.openannotation.lorestore.model.rdf2go.OpenAnnotationImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;

import org.ontoware.rdf2go.model.Model;


/**
 * The RDF2GoOREUpdateHandler class processes updates to annotation by
 * OREController
 * 
 * Based loosely on DannoUpdateHandler
 * 
 */
public class RDF2GoOAUpdateHandler extends AbstractRDF2GoUpdateHandler {

	public RDF2GoOAUpdateHandler(LoreStoreControllerConfig cc) {
		super(cc);
	}

	protected NamedGraphImpl makeNewObject (Model model) {
		return new OpenAnnotationImpl(model);
	}
	protected String getDefaultViewName() {
	        return "oa";
	}
	
	/**
	 * Add oa:annotatedAt property along with dcterms:created and modified
	 * 
	 * @param obj
	 * @throws LoreStoreException
	 */
	@Override
	protected void storeCreationDate(NamedGraphImpl obj) throws LoreStoreException {
		Date now = new Date();
        obj.setDate(now, DCTERMS_CREATED);
        obj.setDate(now, DCTERMS_MODIFIED);
        obj.setDate(now, OA_ANNOTATED_AT_PROPERTY);
	}
	
	/**
	 * Add user uri and name to the annotation
	 * 
	 * @param obj
	 * @throws LoreStoreException
	 */
	@Override
	protected void storeUser(NamedGraphImpl obj) throws LoreStoreException {
		OpenAnnotation anno = (OpenAnnotation)obj;
		LoreStoreIdentityProvider identityProvider = occ.getIdentityProvider();
		String userURI = identityProvider.obtainUserURI();
		String userName = identityProvider.obtainUserName();
		anno.setUser(userURI);
		anno.setUserWithName(userURI, userName);
	}
	
}
