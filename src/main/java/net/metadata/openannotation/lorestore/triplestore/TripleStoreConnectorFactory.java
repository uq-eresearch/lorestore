package net.metadata.openannotation.lorestore.triplestore;

import org.ontoware.rdf2go.model.ModelSet;

public interface TripleStoreConnectorFactory {

	public ModelSet retrieveConnection() throws InterruptedException;
	
	public void release(ModelSet connection) throws InterruptedException;
	
	public void destroy();
}
