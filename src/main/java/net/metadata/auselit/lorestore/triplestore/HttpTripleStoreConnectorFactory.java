package net.metadata.auselit.lorestore.triplestore;

import net.metadata.auselit.lorestore.exceptions.OREDBConnectionException;

import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class HttpTripleStoreConnectorFactory implements
		TripleStoreConnectorFactory {

	private HTTPRepository repo;
	private String repositoryURL;
	
	public ModelSet retrieveConnection() {
		if (repo == null) {
			initRepo();
		}
		ModelSet container = new RepositoryModelSet(repo);
		container.open();
		return container;
	}

	private void initRepo() {
		repo = new HTTPRepository(getRepositoryURL());
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			throw new OREDBConnectionException("Error creating http proxied sesame store", e);
		}
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void release(ModelSet connection) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
