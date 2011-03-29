package oreservlet.servlets;

import org.ontoware.rdf2go.model.ModelSet;

public interface TripleStoreConnectorFactory {

	public ModelSet retrieveConnection();
}
