package net.metadata.openannotation.lorestore.servlet.rdf2go;

import net.metadata.openannotation.lorestore.model.rdf2go.NamedGraphImpl;
import net.metadata.openannotation.lorestore.model.rdf2go.OACAnnotationImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;

import org.ontoware.rdf2go.model.Model;


/**
 * The RDF2GoOREUpdateHandler class processes updates to annotation by
 * OREController
 * 
 * Based loosely on DannoUpdateHandler
 * 
 */
public class RDF2GoOACUpdateHandler extends AbstractRDF2GoUpdateHandler {

	public RDF2GoOACUpdateHandler(LoreStoreControllerConfig occ) {
		super(occ);
	}

	protected NamedGraphImpl makeNewObject (Model model){
		return new OACAnnotationImpl(model);
	}

}
