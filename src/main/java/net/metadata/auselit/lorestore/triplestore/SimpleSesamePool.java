package net.metadata.auselit.lorestore.triplestore;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.ModelSet;

/**
 * Manages a pool of connections to a triple store.
 * 
 * Uses a java BlockingQueue, and based upon:
 * http://stackoverflow.com/questions/1137118/does-this-basic-java-object-pool-work/1139830#1139830
 * 
 * @author uqdayers
 */
public final class SimpleSesamePool implements TripleStoreConnectorFactory {
	private static final Logger LOG = Logger.getLogger(SimpleSesamePool.class);

	private final BlockingQueue<ModelSet> connections;
	private final TripleStoreConnectorFactory cf;
	private int poolSize = 5;
	private int timeout = 2;
	
	public SimpleSesamePool(TripleStoreConnectorFactory cf) throws InterruptedException {
		this.cf = cf;
		this.connections = new ArrayBlockingQueue<ModelSet>(poolSize, false);
		
		for (int i = 0; i < poolSize; i++) {
			ModelSet theConnection = cf.retrieveConnection();
			connections.add(theConnection);
		}
	}

	/**
	 * Return the next available connection from the pool. Will block until one is available.
	 */
	public ModelSet retrieveConnection() throws InterruptedException {
		ModelSet modelSet = this.connections.poll(timeout, TimeUnit.SECONDS);
		LOG.debug("retrieveConnection() returning: " + modelSet + " PoolSize: " + connections.size());
		return modelSet;
	}
	
	/**
	 * Return a connection to the pool.
	 */
	public void release(ModelSet connection) throws InterruptedException {
		LOG.debug("release() replacing: " + connection + " PoolSize: " + connections.size());
		this.connections.put(connection);
	}
	
	/**
	 * Shut down all the connections in the pool.
	 */
	public void destroy() {
		LOG.info("Closing repository connections");
		try {
			while (connections.size() > 0) {
			//FIXME: What happens if there is still a connection in use...
				ModelSet con = retrieveConnection();
				con.close();
			}
		} catch (InterruptedException e) {
			LOG.error("Unable to close connection", e);
		}
		cf.destroy();
	}
}
