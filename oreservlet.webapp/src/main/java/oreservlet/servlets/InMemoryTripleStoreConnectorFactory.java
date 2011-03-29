package oreservlet.servlets;

import oreservlet.exceptions.OREDBConnectionException;

import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public class InMemoryTripleStoreConnectorFactory implements
		TripleStoreConnectorFactory {

	private SailRepository repo;
	
	public ModelSet retrieveConnection() {
		if (repo == null) {
			initRepo();
		}
		ModelSet container = new RepositoryModelSet(repo);
		container.open();
		return container;
	}

	private void initRepo() {
		repo = new SailRepository(new MemoryStore());
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			throw new OREDBConnectionException("Error creating in memory sesame store", e);
		}
	}
}
