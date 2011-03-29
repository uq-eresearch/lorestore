package oreservlet.servlets;

import oreservlet.sesame.SesameUtils;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

public class SesameUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void blergh() throws RepositoryException, RDFHandlerException {
		RepositoryConnection tmpRepo = SesameUtils.makeMemoryRepo();
		
		RepositoryConnection conn = getServerConnection();
		
		
		long size = conn.size();
		ValueFactory vf = conn.getValueFactory();
		RepositoryResult<Statement> it = conn.getStatements(null, vf.createURI("http://doc.localhost/openrdf-sesame/"), vf.createURI("http://doc.localhost/openrdf-sesame/"), false);

		while (it.hasNext()) {
			Statement st = it.next();
			tmpRepo.add(st);
		}
		tmpRepo.export(new RDFXMLPrettyWriter(System.out));
	}
	
	@Test
	public void getNamedGraph() throws RepositoryException, RDFHandlerException {
		String uri = "http://doc.localhost/rem/7d5d612e-1965-f6de-1d90-d3a10db2de1c";
		RepositoryConnection conn = getServerConnection();
		
		
		ValueFactory vf = conn.getValueFactory();
		conn.export(new RDFXMLPrettyWriter(System.out), vf.createURI(uri));
		
	}

	@Test
	public void testMakeMemoryRepo() throws RepositoryException {
		SesameUtils.makeMemoryRepo();
	}
	
	private RepositoryConnection getServerConnection() throws RepositoryException {
		String sesameServer = "http://doc.localhost/openrdf-sesame/";
		String repositoryID = "lore";
		HTTPRepository repo = new HTTPRepository(sesameServer, repositoryID);
		repo.initialize();
		return repo.getConnection();
	}
}
