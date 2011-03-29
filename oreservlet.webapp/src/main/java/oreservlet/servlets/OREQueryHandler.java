package oreservlet.servlets;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.util.ModelUtils;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * The OREQueryHandler class handles queries from the {@link OREController}.
 * 
 * @author uqdayers
 */
public class OREQueryHandler {

	protected final OREControllerConfig occ;
	private TripleStoreConnectorFactory cf;

	public OREQueryHandler(OREControllerConfig occ) {
		this.occ = occ;
		this.cf = occ.getContainerFactory();
	}

	public OREResponse query(HttpServletRequest request) {

		Map<String, String[]> parameterMap = request.getParameterMap();

		// TODO Auto-generated method stub
		return null;
	}

	public OREResponse plainGet(HttpServletRequest request)
			throws RepositoryException, RequestFailureException,
			ServletException {
		String stringURI = request.getRequestURI();

		ModelSet container = cf.retrieveConnection();

		URI uri = container.createURI(stringURI);
		Model model = container.getModel(uri);

		if (model == null || model.isEmpty()) {
			throw new NoSuchRequestHandlingMethodException(request);
			// throw new RequestFailureException(
			// HttpServletResponse.SC_NOT_FOUND,
			// "No resource for '" + uri + "'");
		}
		occ.getAccessPolicy().checkRead(request, model);

		return new OREResponse(model);

	}

	public String browseQuery(String url) throws RepositoryException, MalformedQueryException, QueryEvaluationException, TupleQueryResultHandlerException {
		ModelSet container = cf.retrieveConnection();
		String queryString = browseSparql(url);

		System.out.println("Query String: " + queryString);
		
		Repository rep = (Repository)container.getUnderlyingModelSetImplementation();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RepositoryConnection con = rep.getConnection();
		try {
//			queryString = "select * where { ?s ?p ?o. } ";
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			
			SPARQLResultsXMLWriter resultsXMLWriter = new SPARQLResultsXMLWriter(stream);
			
			tupleQuery.evaluate(resultsXMLWriter);
		} finally {
			con.close();
		}
		
		return stream.toString();
	}
	/*
select distinct ?g ?a ?m ?t
where {
   graph ?g  {
          {<escapedURL> ?p ?o .}
    UNION {?s ?p2 <escapedURL>}
    UNION {<altURL> ?p3 ?o2 .}
    UNION {?s2 ?p4 <altURL>}
  } 
  . {?g <http://purl.org/dc/elements/1.1/creator> ?a}
  . {?g <http://purl.org/dc/terms/modified> ?m}
  . OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?t}
};
	 */
	protected String browseSparql(String escapedURL) {
		// Needs to match both www and non-www version of URL
		String altURL = makeAltURL(escapedURL);
		String query = "select distinct ?g ?a ?m ?t" + " where { graph ?g {"
				+ "{<" + escapedURL + "> ?p ?o .}" + " UNION " + "{?s ?p2 <"
				+ escapedURL + ">}" + " UNION " + "{<" + altURL
				+ "> ?p3 ?o2 .}" + " UNION " + "{?s2 ?p4 <" + altURL + ">}} "
				+ ". {?g <http://purl.org/dc/elements/1.1/creator> ?a}"
				+ ". {?g <http://purl.org/dc/terms/modified> ?m}"
				+ ". OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?t}}";

		return query;
	}

	protected String makeAltURL(String url) {
		// TODO needs to escape
		if (url.contains("http://www.")) {
			return url.replace("http://www.", "http://");
		} else {
			return url.replace("http://", "http://www.");
		}
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

	public OREResponse getOreObject(String oreId) {
		ModelSet container = cf.retrieveConnection();

		URI uri = container.createURI(occ.getBaseUri() + oreId);
		Model model = container.getModel(uri);
		if (model == null || model.isEmpty()) {
			throw new RuntimeException("Can't find resource: " + uri);
			// throw new RequestFailureException(
			// HttpServletResponse.SC_NOT_FOUND,
			// "No resource for '" + uri + "'");
		}
		// occ.getAccessPolicy().checkRead(request, model);

		return new OREResponse(model);

	}
}
