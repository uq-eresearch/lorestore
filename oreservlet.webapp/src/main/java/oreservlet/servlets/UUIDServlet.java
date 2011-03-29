package oreservlet.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oreservlet.sesame.SesameUtils;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;

public class UUIDServlet extends HttpServlet {
	private static final long serialVersionUID = 4111277153315884656L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		UUID newUUID = UUID.randomUUID();

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.println(newUUID);
		
		out.close();
	}
	
	private void checkNotInUse(String id) throws RepositoryException, RDFHandlerException {
		
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			String sesameServer = "http://doc.localhost/openrdf-sesame";
			String repositoryID = "lore";
			HTTPRepository repo = new HTTPRepository(sesameServer, repositoryID);
			repo.initialize();
			
			RepositoryConnection tmpRepo = SesameUtils.makeMemoryRepo();
			
			RepositoryConnection conn = repo.getConnection();
			
			long size = conn.size();
			ValueFactory vf = conn.getValueFactory();
			RepositoryResult<Statement> it = conn.getStatements(null, vf.createURI("prefixURI"), vf.createURI("id"), false);

			while (it.hasNext()) {
				Statement st = it.next();
				tmpRepo.add(st);
			}
			tmpRepo.export(new RDFXMLPrettyWriter(System.out));
		} finally {
			lock.unlock();
		}
		
	}
	
	
	private void markInUse(String id) {
		
	}
}
