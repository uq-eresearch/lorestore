package net.metadata.auselit.lorestore.triplestore;

import java.io.File;

import net.metadata.auselit.lorestore.exceptions.LoreStoreDBConnectionException;

import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.memory.MemoryStore;
import org.springframework.beans.factory.InitializingBean;

public class MemoryTripleStoreConnectorFactory implements
		TripleStoreConnectorFactory, InitializingBean {

	private SailRepository repo;
	private String dataDir;
	private MemoryStore memoryStore;

	public MemoryTripleStoreConnectorFactory() {
		this.memoryStore = new MemoryStore();
	}
	
	public void afterPropertiesSet() throws Exception {
		if (dataDir != null) {
			File dataLocation = new File(dataDir);
			this.memoryStore = new MemoryStore(dataLocation);
		}
	}
	
	public ModelSet retrieveConnection() {
		if (repo == null) {
			initRepo();
		}
		ModelSet connection = new RepositoryModelSet(repo);
		connection.open();
		connection.setAutocommit(false);
		return connection;
	}

	private void initRepo() {
		repo = new SailRepository(memoryStore);
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			throw new LoreStoreDBConnectionException("Error creating in memory sesame store", e);
		}
	}
	
	public long size() throws RepositoryException {
		SailRepositoryConnection connection = repo.getConnection();
		long size = connection.size();
		connection.close();
		return size;
	}

	public void release(ModelSet connection) throws InterruptedException {
		connection.close();
	}

	public void destroy() {
		if (repo != null) {
			try {
				repo.shutDown();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the dataDir
	 */
	public String getDataDir() {
		return dataDir;
	}

	/**
	 * @param dataDir the dataDir to set
	 */
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

}
