package net.metadata.openannotation.lorestore.servlet.rdf2go;

import org.ontoware.rdf2go.model.Model;
import net.metadata.openannotation.lorestore.model.rdf2go.CompoundObjectImpl;
import net.metadata.openannotation.lorestore.model.rdf2go.NamedGraphImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;

/**
 * The RDF2GoOREUpdateHandler class processes updates to compound object by
 * OREController
 * 
 * Based loosely on DannoUpdateHandler
 * 
 */
public class RDF2GoOREUpdateHandler extends AbstractRDF2GoUpdateHandler {

	public RDF2GoOREUpdateHandler(LoreStoreControllerConfig occ) {
		super(occ);
	}


	protected NamedGraphImpl makeNewObject (Model model){
		return new CompoundObjectImpl(model);
	}
}
