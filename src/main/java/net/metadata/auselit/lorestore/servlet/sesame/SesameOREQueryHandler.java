package net.metadata.auselit.lorestore.servlet.sesame;

import java.io.Writer;

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

import net.metadata.auselit.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.servlet.LoreStoreQueryHandler;
import net.metadata.auselit.lorestore.servlet.OREResponse;

public class SesameOREQueryHandler implements LoreStoreQueryHandler {

	@Override
	public OREResponse getNamedGraphObject(String oreId) throws NotFoundException,
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
	public ResponseEntity<String> browseQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, InvalidQueryParametersException {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView browseAtomQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> searchQuery(String urlParam,
			String matchpred, String matchval) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNumberTriples() throws InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void exportAll(Writer outputWriter) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseEntity<String> sparqlQuery(String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> searchQueryIncludingAbstract(String urlParam,
			String matchpred, String matchval) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView annotatesQuery(String urlParam)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

}
