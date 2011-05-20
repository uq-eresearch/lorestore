package net.metadata.auselit.lorestore.triplestore;

import java.io.File;

import net.metadata.auselit.lorestore.exceptions.OREDBConnectionException;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * 
 * @author uqdayers
 *
 */
public class NativeTripleStoreConnectorFactory implements
		TripleStoreConnectorFactory {
	private static final Logger LOG = Logger.getLogger(NativeTripleStoreConnectorFactory.class);

	private SailRepository repo;
	private String dataDirPath;
	private String sesameIndexes = "spoc,posc,cspo,ocsp";
	
	/**
	 * Opens a new connection to the repository and returns it.
	 */
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
		File dataDir = new File(dataDirPath);
		NativeStore nativeStore = new NativeStore(dataDir, sesameIndexes);
		repo = new SailRepository(nativeStore);
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			throw new OREDBConnectionException("Error creating persistent sesame store", e);
		}
	}
	
	/**
	 * Return the total number of triples in the triplestore.
	 * @return total number of triples in the triplestore
	 * @throws RepositoryException
	 */
	public long size() throws RepositoryException {
		SailRepositoryConnection connection = repo.getConnection();
		long size = connection.size();
		connection.close();
		return size;
	}

	/**
	 * Ends the use of a connection. The connection will be closed.
	 */
	public void release(ModelSet connection) throws InterruptedException {
		connection.close();
	}
	
	/**
	 * Shut down the repository, closing all connections to it.
	 */
	public void destroy() {
		LOG.info("Shutting down SAIL repository");
		try {
			if (repo != null) {
				repo.shutDown();
			}
		} catch (RepositoryException e) {
			LOG.error("Error shutting down sail repository", e);
		}
	}

	public void setDataDirPath(String dataDirPath) {
		this.dataDirPath = dataDirPath;
	}

	public String getDataDirPath() {
		return dataDirPath;
	}

	public void setSesameIndexes(String sesameIndexes) {
		this.sesameIndexes = sesameIndexes;
	}

	public String getSesameIndexes() {
		return sesameIndexes;
	}
}
