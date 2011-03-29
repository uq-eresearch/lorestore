package oreservlet.sesame;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public class SesameUtils {

	
	public static RepositoryConnection makeMemoryRepo() throws RepositoryException {
		SailRepository myRepo = new SailRepository(new MemoryStore());
		myRepo.initialize();
		return myRepo.getConnection();
	}
}
