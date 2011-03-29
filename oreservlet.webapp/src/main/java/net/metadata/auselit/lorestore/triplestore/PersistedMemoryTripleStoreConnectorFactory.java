package net.metadata.auselit.lorestore.triplestore;

import java.io.File;

import net.metadata.auselit.lorestore.exceptions.OREDBConnectionException;

import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.nativerdf.NativeStore;

public class PersistedMemoryTripleStoreConnectorFactory implements
		TripleStoreConnectorFactory {

	private SailRepository repo;
	private String dataDirPath;
	
	public ModelSet retrieveConnection() {
		if (repo == null) {
			initRepo();
		}
		ModelSet container = new RepositoryModelSet(repo);
		container.open();
		return container;
	}

	private void initRepo() {
		File dataDir = new File(dataDirPath);
		repo = new SailRepository(new NativeStore(dataDir));
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

	public void setDataDirPath(String dataDirPath) {
		this.dataDirPath = dataDirPath;
	}

	public String getDataDirPath() {
		return dataDirPath;
	}

	public void release(ModelSet connection) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
}
