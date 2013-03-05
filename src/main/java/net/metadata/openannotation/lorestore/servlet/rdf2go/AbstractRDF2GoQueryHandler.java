package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.SPARQL_RESULTS_XML;

import java.io.ByteArrayOutputStream;
import java.io.Writer;

import net.metadata.openannotation.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.LoreStoreQueryHandler;
import net.metadata.openannotation.lorestore.servlet.OREController;
import net.metadata.openannotation.lorestore.servlet.OREResponse;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.impl.TupleQueryResultBuilder;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.trig.TriGWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The AbstractRDF2GoOREQueryHandler class provides common features for subclasses that
 * handle queries for ORE and OA objects from the {@link OREController}.
 */
public abstract class AbstractRDF2GoQueryHandler implements LoreStoreQueryHandler {
	protected final Logger LOG = Logger.getLogger(AbstractRDF2GoQueryHandler.class);
	protected final LoreStoreControllerConfig occ;
	protected TripleStoreConnectorFactory cf;
	protected LoreStoreAccessPolicy ap;

	public AbstractRDF2GoQueryHandler(LoreStoreControllerConfig occ) {
		this.occ = occ;
		this.cf = occ.getContainerFactory();
		this.ap = occ.getAccessPolicy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.metadata.openannotation.lorestore.servlet.rdf2go.LoreStoreQueryHandler#getNamedGraphObject
	 * (java.lang.String)
	 */
	@Override
	public ModelAndView getNamedGraphObject(String objId) throws NotFoundException,
			InterruptedException {
		ModelSet container = cf.retrieveConnection();
		Model model = null;
		// FIXME: should copy the model to a separate store, this currently
		// maintains its connection to the main triplestore via the opened connection,
		// right through until the view has finished dealing with it.
		try {
			URI uri = container.createURI(occ.getBaseUri() + objId);
			model = container.getModel(uri);
			if (model == null || model.isEmpty()) {
				if (model != null) {
					model.close();
				}
				LOG.debug("Cannot find requested resource: " + objId);
				throw new NotFoundException("Cannot find resource: " + objId);
			}
			checkUserCanRead(model);
		} catch (AccessDeniedException ex) {
			// The model is normally closed after the response view is generated,
			// but since an exception is thrown it must be closed here.
			model.close();
			throw ex;
		} finally {
			cf.release(container);
		}
		return new OREResponse(model);
	}
	
	protected void checkUserCanRead(Model m) {	
	}
	
	@Override
	public ModelAndView refersToQuery(String urlParam)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, InvalidQueryParametersException, RDFHandlerException {
	        String queryString = generateBrowseQuery(urlParam);
	        return runSparqlQueryIntoGraphsMAV(queryString);
	}
	
	@Override
	public ModelAndView searchQuery(String urlParam,
			String matchpred, String matchval, Boolean includeAbstract, Boolean asTriples) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException, RDFHandlerException {
		String queryString = generateSearchQuery(urlParam, matchpred, matchval, includeAbstract);
		if (asTriples){
		return runSparqlQueryIntoMAV(queryString);
		} else {
		    return runSparqlQueryIntoGraphsMAV(queryString);
		}
	}

	

	protected void checkURLIsValid(String url)
			throws InvalidQueryParametersException, InterruptedException {
		ModelSet connection = null;
		try {
			connection = cf.retrieveConnection();
			if (!connection.isValidURI(url)) {
				throw new InvalidQueryParametersException(
						"Supplied URL is invalid: " + url);
			}
		} finally {
			cf.release(connection);
		}

	}

	@Override
	public ModelAndView browseAtomQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		// Implemented in subclasses
		return null;
	}

	/**
	 * Creates the correct headers for returning sparql results xml
	 * 
	 * @return headers object with correct content type and encoding
	 */
	protected HttpHeaders getSparqlResultsHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType
				.parseMediaType(SPARQL_RESULTS_XML + ";charset=UTF-8"));
		return responseHeaders;
	}	
	

	@Override
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException {
		// Implemented in subclasses
		return null;
	}

	/**
	 * Takes a sparql query and a generates a ModelAndView that displays the matching graphs (e.g. annotations, resource maps)
	 * @param queryString
	 * @return
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 * @throws TupleQueryResultHandlerException
	 * @throws InterruptedException
	 */
	protected ModelAndView runSparqlQueryIntoGraphsMAV(String queryString) throws RepositoryException, MalformedQueryException, QueryEvaluationException, TupleQueryResultHandlerException, InterruptedException {
	    // implemented in subclasses
	    return null;
	}
	       
	
	/**
	 * Takes a sparql query and returns a string of the sparql xml results from
	 * running that query on the configured triplestore.
	 * 
	 * @param queryString
	 *            a valid sparql query
	 * @return
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 * @throws TupleQueryResultHandlerException
	 * @throws InterruptedException
	 * @throws RDFHandlerException 
	 */
	protected String runSparqlQuery(String queryString)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException {
		ModelSet container = null;
		RepositoryConnection con = null;
		ByteArrayOutputStream stream;
		try {
			container = cf.retrieveConnection();
			Repository rep = (Repository) container
					.getUnderlyingModelSetImplementation();
			stream = new ByteArrayOutputStream();
			con = rep.getConnection();
			
			Query query = con.prepareQuery(QueryLanguage.SPARQL,
					queryString);
			if (query instanceof TupleQuery){
			    SPARQLResultsXMLWriter resultsXMLWriter = new SPARQLResultsXMLWriter(
                                    stream);
			    ((TupleQuery)query).evaluate(resultsXMLWriter);
			} else if (query instanceof GraphQuery){
			    RDFXMLWriter resultsWriter = new RDFXMLWriter(stream);
			    ((GraphQuery)query).evaluate(resultsWriter);
			} else if (query instanceof BooleanQuery) {
			    return "" + ((BooleanQuery) query).evaluate();
			}
		} finally {
			if (con != null){
				con.close();
			}
			if (container != null){
				cf.release(container);
			}
		}

		return stream.toString();
	}
	
	/**
	 * Run a SPARQL query returning a ModelAndView object
	 * 
	 * @param queryString
	 * @return
	 */
	protected ModelAndView runSparqlQueryIntoMAV(String queryString)
		throws RepositoryException, MalformedQueryException,
		InterruptedException, QueryEvaluationException,
		TupleQueryResultHandlerException, RDFHandlerException {
		ModelAndView mav = new ModelAndView("sparqlxml");
		String queryResult = runSparqlQuery(queryString);
		mav.addObject("sparqlxml", queryResult);
		return mav;
	}
	
	/**
	 * Run a SPARQL query returning the results object, which can be processed
	 * further for example into RSS, instead of the sparqlXML format.
	 * 
	 * @param queryString
	 * @return
	 */
	protected TupleQueryResult runSparqlQueryIntoQR(String queryString)
			throws RepositoryException, MalformedQueryException,
			InterruptedException, QueryEvaluationException,
			TupleQueryResultHandlerException {
		ModelSet container = null;
		RepositoryConnection con = null;
		TupleQueryResultBuilder resultBuilder = null;
		try {
			container = cf.retrieveConnection();
			Repository rep = (Repository) container
					.getUnderlyingModelSetImplementation();
			con = rep.getConnection();
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL,
					queryString);

			resultBuilder = new TupleQueryResultBuilder();

			tupleQuery.evaluate(resultBuilder);
		} finally {
			if (con != null)
				con.close();
			if (container != null)
				cf.release(container);
		}

		return resultBuilder.getQueryResult();
	}

	protected String generateBrowseQuery(String escapedURL) {
		return null;
	}

	/**
	 * Adds or removes the 'www' in a http url. Returns self if the provided url
	 * doesn't match.
	 */
	protected String makeAltURL(String url) {
		// TODO needs to escape
		if (url.contains("http://www.")) {
			return url.replace("http://www.", "http://");
		} else {
			return url.replace("http://", "http://www.");
		}
	}

	protected String generateSearchQuery(String urlParam, String matchpred,
			String matchval, boolean includeAbstract) {
		return null;
	}

	/**
	 * Constructs a regex filter string for a SPARQL query
	 * 
	 * @param matchVal
	 * @return
	 */
	protected String makeFilter(String matchVal) {
		// implicit and, use quotes for phrase search
		String fExpr = "";
		if (matchVal.startsWith("\"") && matchVal.endsWith("\"")) {
			String temp = matchVal.substring(1, matchVal.length() - 1);
			fExpr = "FILTER regex(str(?v?, \"" + temp + "\", \"i\")";
		} else if (matchVal.contains(" ")) {
			for (String term : matchVal.split(" ")) {
				fExpr += "FILTER regex(str(?v), \"" + term + "\", \"i\"). ";
			}
		} else {
			fExpr = "FILTER regex(str(?v), \"" + matchVal + "\", \"i\")";
		}
		return fExpr;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.metadata.openannotation.lorestore.servlet.rdf2go.OREQueryHandler#
	 * getNumberTriples()
	 */
	@Override
	public long getNumberTriples() throws InterruptedException {
		ModelSet container = cf.retrieveConnection();
		try {
			return container.size();
		} finally {
			cf.release(container);
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.metadata.openannotation.lorestore.servlet.rdf2go.OREQueryHandler#exportAll
	 * (java.io.Writer)
	 */
	@Override
	public void exportAll(Writer outputWriter) throws Exception {
		Repository rep = null;
		ModelSet connection = cf.retrieveConnection();
		RepositoryConnection connection2 = null;
		try {
			rep = (Repository) connection.getUnderlyingModelSetImplementation();

			connection2 = rep.getConnection();
			TriGWriter triGWriter = new TriGWriter(outputWriter);
			connection2.export(triGWriter);
		} finally {
			if (connection2 != null) {
				connection2.close();
			}
			if (connection != null) {
				cf.release(connection);
			}
		}
	}

	@Override
	public ResponseEntity<String> sparqlQuery(String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException {
		ap.checkAdmin();

		HttpHeaders responseHeaders = getSparqlResultsHeaders();
		return new ResponseEntity<String>(runSparqlQuery(query),
				responseHeaders, HttpStatus.OK);
	}
}
