package net.metadata.openannotation.lorestore.servlet.sesame;

import java.io.Writer;

import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.LoreStoreQueryHandler;
import net.metadata.openannotation.lorestore.servlet.LorestoreResponse;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public class SesameOREQueryHandler implements LoreStoreQueryHandler {

	@Override
	public LorestoreResponse getNamedGraphObject(String oreId) throws NotFoundException,
			InterruptedException {

		Repository myRepository = new SailRepository(new MemoryStore());

		RepositoryConnection con = null;
		try {
			con = myRepository.getConnection();

			ValueFactory vf = myRepository.getValueFactory();
			URI createURI = vf.createURI(oreId);

			RepositoryResult<Statement> statements = con.getStatements(createURI, null, null, false, createURI);
			
			
			Repository myObj = new SailRepository(new MemoryStore());
			RepositoryConnection myObjCon = myObj.getConnection();
			myObjCon.add(statements);
			
			
		} catch (RepositoryException e) {
			try {
				con.close();
			} catch (RepositoryException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}


	@Override
	public ModelAndView browseAtomQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		return null;
	}

	@Override
	public ModelAndView searchQuery(String urlParam,
			String matchpred, String matchval, Boolean includeAbstract, Boolean asTriples) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		return null;
	}

	@Override
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		return null;
	}

	@Override
	public long getNumberTriples() throws InterruptedException {
		return 0;
	}

	@Override
	public void exportAll(Writer outputWriter) throws Exception {

	}

	@Override
	public ResponseEntity<String> sparqlQuery(String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		return null;
	}


	@Override
	public ModelAndView refersToQuery(String urlParam)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		return null;
	}

	@Override
	public long getNumberNamedGraphs() throws InterruptedException {
		return 0;
	}

}
