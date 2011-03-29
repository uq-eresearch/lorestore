package oreservlet.servlets;

import java.io.IOException;
import java.io.InputStream;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.impl.URIGenerator;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class ORETypeFactory {
	public static Model create(InputStream in) throws ModelRuntimeException, IOException {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model model = modelFactory.createModel();
		model.open();
		model.readFrom(in);
		return model;
	}
	
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
	
	public static Model retrieve(String stringURI) throws RepositoryException   {
		Repository rdf2goRepo = ORETypeFactory.getRemoteRepo();

		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();

		URI uri = modelSet.createURI(stringURI);
		Model model = modelSet.getModel(uri);

		return model;
	}
}
