package net.metadata.openannotation.lorestore.triplestore;

import java.io.File;
import java.io.IOException;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreDBConnectionException;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.inferencer.fc.DirectTypeHierarchyInferencer;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
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
	private String oaSchemaFile = null;
	private String oreSchemaFile = null;
	private String oaSchema = null;
	private String oreSchema = null;
	private String sesameIndexes = "spoc,posc,cspo,ocsp";
	
	/**
	 * Opens a new connection to the repository and returns it.
	 */
	public ModelSet retrieveConnection() {
		LOG.info("retrieve connection");
		if (repo == null) {
			initRepo();
		} 
		
		ModelSet connection = new RepositoryModelSet(repo);
		connection.open();
		connection.setAutocommit(false);
		if (connection.size() == 0){
			LOG.info("load schema");
			// Populate empty repository with schema
			if (oaSchemaFile != null && oaSchema != null){
				loadSchema(connection, oaSchemaFile, oaSchema);
			}
			if (oreSchemaFile != null && oreSchema != null){
				loadSchema(connection, oreSchemaFile, oreSchema);
			}
			connection.commit();
		}
		return connection;
	}

	private void initRepo() {
		File dataDir = new File(dataDirPath);
		NativeStore nativeStore = new NativeStore(dataDir, sesameIndexes);
		repo = new SailRepository(new DirectTypeHierarchyInferencer(new ForwardChainingRDFSInferencer(nativeStore)));
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			throw new LoreStoreDBConnectionException("Error creating persistent sesame store", e);
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
	
	private void loadSchema(ModelSet connection, String schemaFile, String baseUri){
		try {
			ClassLoader cl = this.getClass().getClassLoader();
		    java.io.InputStream in = cl.getResourceAsStream(schemaFile);
		    connection.readFrom(in, Syntax.Trig, baseUri);
		} catch (IOException e){
			LOG.debug("Unable to read schema file " + schemaFile + " " + e.getMessage());
		}
	}
	
	public void setDataDirPath(String dataDirPath) {
		this.dataDirPath = dataDirPath;
	}

	public String getDataDirPath() {
		return dataDirPath;
	}
	
	public void setOaSchemaFile(String schemaFile){
		this.oaSchemaFile = schemaFile;
	}
	
	public String getOaSchemaFile() {
		return this.oaSchemaFile;
	}
	public void setOaSchema(String schema){
		this.oaSchema = schema;
	}
	
	public String getOaSchema() {
		return this.oaSchema;
	}
	
	public void setSesameIndexes(String sesameIndexes) {
		this.sesameIndexes = sesameIndexes;
	}

	public void setOreSchemaFile(String oreSchemaFile){
		this.oreSchemaFile = oreSchemaFile;
	}
	
	public String getOreSchemaFile() {
		return this.oreSchemaFile;
	}
	public void setOreSchema(String oreSchema){
		this.oreSchema = oreSchema;
	}
	
	public String getOreSchema() {
		return this.oreSchema;
	}
	
	public String getSesameIndexes() {
		return sesameIndexes;
	}
}
