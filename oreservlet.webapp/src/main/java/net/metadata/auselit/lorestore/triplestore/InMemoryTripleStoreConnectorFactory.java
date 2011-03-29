package net.metadata.auselit.lorestore.triplestore;

import net.metadata.auselit.lorestore.exceptions.OREDBConnectionException;

import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
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
	
	public long size() throws RepositoryException {
		SailRepositoryConnection connection = repo.getConnection();
		long size = connection.size();
		connection.close();
		return size;
	}

	public void release(ModelSet connection) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
