package net.metadata.auselit.misc;

import java.io.IOException;
import java.io.InputStream;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.impl.URIGenerator;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public abstract class TestUtilities {

	/**
	 * 
	 * @param in
	 * @param baseUri eg. http://example.com/rem/
	 * @return
	 * @throws ModelRuntimeException
	 * @throws IOException
	 */
	public static Model create(InputStream in, String baseUri) throws ModelRuntimeException, IOException {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		URI uri = URIGenerator.createNewRandomUniqueURI(baseUri);
		Model model = modelFactory.createModel(uri);
		model.open();
		model.readFrom(in);
		return model;
	}
	
	public static Repository getRemoteRepo() throws RepositoryException {
		HTTPRepository myRepo = new HTTPRepository(
				"http://doc.localhost/openrdf-sesame/repositories/lore");
		myRepo.initialize();
		return myRepo;
	}

}
