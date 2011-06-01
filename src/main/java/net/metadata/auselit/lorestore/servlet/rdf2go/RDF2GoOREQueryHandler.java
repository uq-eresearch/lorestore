package net.metadata.auselit.lorestore.servlet.rdf2go;

import static net.metadata.auselit.lorestore.common.OREConstants.SPARQL_RESULTS_XML;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Writer;

import net.metadata.auselit.lorestore.access.OREAccessPolicy;
import net.metadata.auselit.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.servlet.OREController;
import net.metadata.auselit.lorestore.servlet.OREControllerConfig;
import net.metadata.auselit.lorestore.servlet.OREQueryHandler;
import net.metadata.auselit.lorestore.servlet.OREResponse;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.trig.TriGWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

/**
 * The RDF2GoOREQueryHandler class handles queries from the {@link OREController}.
 * 
 * @author uqdayers
 */
public class RDF2GoOREQueryHandler implements OREQueryHandler {
	private final Logger LOG = Logger.getLogger(RDF2GoOREQueryHandler.class);
	protected final OREControllerConfig occ;
	private TripleStoreConnectorFactory cf;
	private OREAccessPolicy ap;

	public RDF2GoOREQueryHandler(OREControllerConfig occ) {
		this.occ = occ;
		this.cf = occ.getContainerFactory();
		this.ap = occ.getAccessPolicy();
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#getOreObject(java.lang.String)
	 */
	@Override
	public OREResponse getOreObject(String oreId) throws NotFoundException,
			InterruptedException {
		ModelSet container = cf.retrieveConnection();
		Model model;

		// FIXME: should copy the model to a separate store, this currently
		// maintains
		// it's connection to the main triplestore via the opened connection,
		// right
		// through until the view has finished dealing with it.
		try {
			URI uri = container.createURI(occ.getBaseUri() + oreId);
			model = container.getModel(uri);
			if (model == null || model.isEmpty()) {
				LOG.debug("Cannot find requested resource: " + oreId);
				throw new NotFoundException("Cannot find resource: " + oreId);
			}
		} finally {
			cf.release(container);
		}
		ap.checkRead(model);
		return new OREResponse(model);

	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#browseQuery(java.lang.String)
	 */
	@Override
	public ResponseEntity<String> browseQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, InvalidQueryParametersException {
		checkURLIsValid(url);
		String queryString = generateBrowseQuery(url);

		HttpHeaders responseHeaders = getSparqlResultsHeaders();
		return new ResponseEntity<String>(runSparqlQuery(queryString),
				responseHeaders, HttpStatus.OK);
	}

	private void checkURLIsValid(String url)
			throws InvalidQueryParametersException, InterruptedException {
		ModelSet connection = null;
		try {
			connection = cf.retrieveConnection();
			if (!connection.isValidURI(url)) {
				throw new InvalidQueryParametersException(
						"Supplied URL is invalid: " + url);
			}
		} finally {
			connection.close();
		}

	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#browseRSSQuery(java.lang.String)
	 */
	@Override
	public ModelAndView browseRSSQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		String queryString = generateBrowseQuery(url);
		ModelAndView view = new ModelAndView("xslt");

		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				runSparqlQuery(queryString).getBytes());
		view.addObject("xmlData", inputStream);

		return view;
	}

	/**
	 * Creates the correct headers for returning sparql results xml
	 * 
	 * @return headers object with correct content type and encoding
	 */
	private HttpHeaders getSparqlResultsHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType
				.parseMediaType(SPARQL_RESULTS_XML + ";charset=UTF-8"));
		return responseHeaders;
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#searchQuery(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ResponseEntity<String> searchQuery(String urlParam,
			String matchpred, String matchval) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		String queryString = generateSearchQuery(urlParam, matchpred, matchval);

		HttpHeaders responseHeaders = getSparqlResultsHeaders();
		return new ResponseEntity<String>(runSparqlQuery(queryString),
				responseHeaders, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#exploreQuery(java.lang.String)
	 */
	@Override
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		String queryString = generateExploreQuery(url);

		HttpHeaders responseHeaders = getSparqlResultsHeaders();
		return new ResponseEntity<String>(runSparqlQuery(queryString),
				responseHeaders, HttpStatus.OK);
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
	 */
	private String runSparqlQuery(String queryString)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException {
		ModelSet container = null;
		RepositoryConnection con = null;
		ByteArrayOutputStream stream;
		try {
			container = cf.retrieveConnection();
			Repository rep = (Repository) container
					.getUnderlyingModelSetImplementation();
			stream = new ByteArrayOutputStream();
			con = rep.getConnection();
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL,
					queryString);

			SPARQLResultsXMLWriter resultsXMLWriter = new SPARQLResultsXMLWriter(
					stream);

			tupleQuery.evaluate(resultsXMLWriter);
		} finally {
			if (con != null)
				con.close();
			if (container != null)
				cf.release(container);
		}

		return stream.toString();
	}

	protected String generateBrowseQuery(String escapedURL) {
		// Needs to match both www and non-www version of URL
		String altURL = makeAltURL(escapedURL);
		String query = "SELECT DISTINCT ?g ?a ?m ?t " + " WHERE { graph ?g {"
				+ "{<" + escapedURL + "> ?p ?o .}" + " UNION " + "{?s ?p2 <"
				+ escapedURL + ">}" + " UNION " + "{<" + altURL
				+ "> ?p3 ?o2 .}" + " UNION " + "{?s2 ?p4 <" + altURL + ">}}. "
				+ " {?g <http://purl.org/dc/elements/1.1/creator> ?a}."
				+ " {?g <http://purl.org/dc/terms/modified> ?m}."
				+ " OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?t}}";

		return query;
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

	private String generateSearchQuery(String urlParam, String matchpred,
			String matchval) {
		String escapedURL = "?u";
		if (urlParam != null && !urlParam.isEmpty()) {
			escapedURL = "<" + urlParam + ">";
		}

		String predicate = "?p";
		if (matchpred != null && !matchpred.isEmpty()) {
			predicate = "<" + matchpred + ">";
		}

		String filter = "";
		if (matchval != null && !matchval.isEmpty()) {
			filter = makeFilter(matchval);
		}

		String queryString = "select distinct ?g ?a ?m ?t ?v where {"
				+ " graph ?g {" + escapedURL + " " + predicate + " ?v ."
				+ filter + "} ."
				+ "{?g <http://purl.org/dc/elements/1.1/creator> ?a} ."
				+ "{?g <http://purl.org/dc/terms/modified> ?m} ."
				+ "OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?t}}";
		return queryString;
	}

	/**
	 * Constructs a filter for a SPARQL query
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

	protected String generateExploreQuery(String escapedURI) {
		String query = "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
				+ "PREFIX dcterms:<http://purl.org/dc/terms/>"
				+ "PREFIX ore:<http://www.openarchives.org/ore/terms/> "
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT DISTINCT ?something ?somerel ?sometitle ?sometype ?creator ?modified ?anotherrel ?somethingelse WHERE {"
				// Compound objects that contain this uri
				+ "{?aggre ore:aggregates <"
				+ escapedURI
				+ "> . "
				+ "?something ore:describes ?aggre . "
				+ "?something a ?sometype . "
				+ "OPTIONAL {?something dc:creator ?creator .} "
				+ "OPTIONAL {?something dcterms:modified ?modified .} "
				+ "OPTIONAL {?something dc:title ?sometitle .}}"
				// uris that have an asserted relationship to this uri
				+ "UNION { ?something ?somerel <"
				+ escapedURI
				+ "> . "
				+ "FILTER isURI(?something) ."
				+ "FILTER (?somerel != ore:aggregates) . "
				+ "FILTER (?somerel != rdf:type) . "
				+ "OPTIONAL {?something a ?sometype} ."
				+ "OPTIONAL {?something dc:title ?sometitle.} }"
				// uris that have an asserted relationships from this uri
				+ "UNION {<"
				+ escapedURI
				+ "> ?somerel ?something . "
				+ "FILTER isURI(?something). "
				+ "FILTER (?somerel != rdf:type) . "
				+ "FILTER (?somerel != ore:describes) . "
				+ "OPTIONAL {?something a ?sometype} ."
				+ "OPTIONAL {?something dc:title ?sometitle.}}"
				// if this is a compound object, uris contained
				+ "UNION {<"
				+ escapedURI
				+ "> ore:describes ?aggre ."
				+ "?aggre ?somerel ?something . "
				+ "FILTER isURI(?something) ."
				+ "FILTER (?somerel != rdf:type) ."
				+ "OPTIONAL {?something dc:title ?sometitle . } . "
				+ "OPTIONAL {?something ?anotherrel ?somethingelse . FILTER isURI(?somethingelse)} . "
				+ "OPTIONAL {?something a ?sometype}}}";
		return query;
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#getNumberTriples()
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

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREQueryHandler#exportAll(java.io.Writer)
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
}
