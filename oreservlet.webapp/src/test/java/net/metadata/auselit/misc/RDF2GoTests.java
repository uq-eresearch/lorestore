package net.metadata.auselit.misc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import net.metadata.auselit.lorestore.servlet.CommonTestRecords;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.sail.memory.MemoryStore;

public class RDF2GoTests {

	@Before
	public void setUp() throws Exception {
	}

	public Repository getInMemRepo() throws RepositoryException {
		SailRepository myRepo = new SailRepository(new MemoryStore());
		myRepo.initialize();
		return myRepo;
	}


	public void connectToMemorySesame() throws Exception {
		Model model = new RepositoryModel(getInMemRepo());
		model.open();
		assertTrue(model.isEmpty());
	}



	public Model save(Model model) throws RepositoryException {
		Repository remoteRepo = TestUtilities.getRemoteRepo();
		RepositoryModelSet modelSet = new RepositoryModelSet(remoteRepo);
		modelSet.open();

		modelSet.addModel(model);
		modelSet.commit();

		return model;
	}

	public void connectToRemoteSesame() throws Exception {
		Repository myRepo = TestUtilities.getRemoteRepo();
		RepositoryConnection connection = myRepo.getConnection();
		System.out.println(connection.size());
		connection.export(new RDFXMLWriter(System.out));

		Repository rdf2goRepo = TestUtilities.getRemoteRepo();

		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();

		modelSet.dump();

		ClosableIterator<URI> modelURIs = modelSet.getModelURIs();
		while (modelURIs.hasNext()) {
			System.out.println(modelURIs.next());
		}

		System.out.println(modelSet.size());
		assertFalse(modelSet.isEmpty());
	}

	@Test
	public void saveAndRetrieve() throws Exception {
		Model model = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.ORE_TEXT.getBytes()),
				"http://example.com/rem/");
		save(model);
		String stringUri = model.getContextURI().toString();
		System.out.println("Created URI: " + stringUri);

		Model retrievedModel = retrieve(stringUri);
		assertFalse(retrievedModel.isEmpty());
		retrievedModel.writeTo(System.out);

		assertTrue(delete(stringUri));

	}

	public Model retrieve(String stringURI) throws Exception {
		Repository rdf2goRepo = TestUtilities.getRemoteRepo();

		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();

		URI uri = modelSet.createURI(stringURI);
		Model model = modelSet.getModel(uri);

		return model;
	}

	public boolean delete(String stringURI) throws Exception {
		Repository rdf2goRepo = TestUtilities.getRemoteRepo();
		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();

		return modelSet.removeModel(modelSet.createURI(stringURI));
	}

}
