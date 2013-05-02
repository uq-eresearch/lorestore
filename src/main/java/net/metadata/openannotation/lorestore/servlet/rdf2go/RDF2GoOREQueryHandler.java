package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_PRIVATE;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USER;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.ORE_RESOURCEMAP_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.RDF_TYPE_PROPERTY;
import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.model.rdf2go.CompoundObjectImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.OREController;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.Variable;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

/**
 * The RDF2GoOREQueryHandler class handles queries from the
 * {@link OREController}.
 */
public class RDF2GoOREQueryHandler extends AbstractRDF2GoQueryHandler {

	public RDF2GoOREQueryHandler(LoreStoreControllerConfig occ) {
		super(occ);
	}
        protected String getDefaultViewName() {
            return "ore";
        }
	protected void checkUserCanRead(Model m) {
		CompoundObjectImpl compoundObject = new CompoundObjectImpl(m);
		try {
			ap.checkRead(compoundObject);
		} finally {
			compoundObject.close();
		}
	}

	@Override
	public ModelAndView browseAtomQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException {
		String queryString = generateBrowseQuery(url);
		ModelAndView mav = new ModelAndView("oreAtom");
		mav.addObject("browseURL", url);

		TupleQueryResult queryResult = runSparqlQueryIntoQR(queryString);
		mav.addObject("queryResult", queryResult);
		
		return mav;
	}

	@Override
	public ModelAndView refersToQuery(String urlParam) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException, InvalidQueryParametersException, RDFHandlerException {
		checkURLIsValid(urlParam);
		String queryString = generateBrowseQuery(urlParam);
		return runSparqlQueryIntoMAV(queryString);
	}



	@Override
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException {
		String queryString = generateExploreQuery(url);

		HttpHeaders responseHeaders = getSparqlResultsHeaders();
		return new ResponseEntity<String>(runSparqlQuery(queryString),
				responseHeaders, HttpStatus.OK);
	}

	
	protected String generateBrowseQuery(String escapedURL) {
		// Needs to match both www and non-www version of URL
		String altURL = makeAltURL(escapedURL);
		String userURI = occ.getIdentityProvider().obtainUserURI();
		// @formatter:off
		String query = "SELECT DISTINCT ?g ?a ?m ?t ?priv"
				+ " WHERE { graph ?g {"
				+ " {<" + escapedURL + "> ?p ?o .}"
				+ " UNION {?s ?p2 <" + escapedURL + ">}"
				+ " UNION {<" + altURL + "> ?p3 ?o2 .}"
				+ " UNION {?s2 ?p4 <" + altURL+ ">}}. "
				+ " OPTIONAL {?g <http://purl.org/dc/elements/1.1/creator> ?a}."
				+ " OPTIONAL {?g <http://purl.org/dc/terms/modified> ?m}."
				+ " OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?t}."
				+ " OPTIONAL {?g <" + LORESTORE_PRIVATE + "> ?priv}."
				+ " OPTIONAL {?g <" + LORESTORE_USER + "> ?user}."
				+ " FILTER (!bound(?priv) || (bound(?priv) && ?user = '" + userURI + "'))"
				+ "}";
		// @formatter:on
		return query;
	}

	

	protected String generateSearchQuery(String urlParam, String matchpred,
			String matchval, String orderBy, int offset, int limit, boolean includeAbstract, boolean asTriples) {
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
		String tempOrderBy = orderBy;
		if (!(orderBy.equals("date") || orderBy.equals("creator") || orderBy.equals("title"))){
	            tempOrderBy = "date";
	        }
		
		String userURI = occ.getIdentityProvider().obtainUserURI();
		// @formatter:off
		String queryString = "select distinct ?g ?creator ?date ?t ?v ?priv"
				+ (includeAbstract ? "?ab" : "")
				+ " where {"
				+ "   graph ?g {?g a <" + ORE_RESOURCEMAP_CLASS + "> ."
				+ escapedURL + " " + predicate + " ?v ."
				+        filter
				+ "   } ."
				+   "OPTIONAL {?g <http://purl.org/dc/elements/1.1/creator> ?creator} ."
				+   "OPTIONAL {?g <http://purl.org/dc/terms/modified> ?date} ."
				+   "OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?title} ."
				+   (includeAbstract ? "OPTIONAL {?g <http://purl.org/dc/terms/abstract> ?ab}." : "") 
				+ " OPTIONAL {?g <" + LORESTORE_PRIVATE + "> ?priv}."
				+ " OPTIONAL {?g <" + LORESTORE_USER + "> ?user}."
				+ " FILTER (!bound(?priv) || (bound(?priv) && ?user = '" + userURI + "'))"
				+ "} order by ?" + tempOrderBy ;
		// @formatter:on
		
		return queryString;
	}

	

	protected String generateExploreQuery(String escapedURI) {
		// @formatter:off
		String userURI = occ.getIdentityProvider().obtainUserURI();
		String query = "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
				+ "PREFIX dcterms:<http://purl.org/dc/terms/>"
				+ "PREFIX ore:<http://www.openarchives.org/ore/terms/> "
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT DISTINCT ?something ?somerel ?sometitle ?sometype ?creator ?modified ?anotherrel ?somethingelse WHERE {"
				// Compound objects that contain this uri
				+ "{?aggre ore:aggregates <" + escapedURI + "> . "
					+ "?something ore:describes ?aggre . "
					+ "?something a ?sometype . "
					+ "OPTIONAL {?something dc:creator ?creator .} "
					+ "OPTIONAL {?something dcterms:modified ?modified .} "
					+ "OPTIONAL {?something dc:title ?sometitle .}"
					+ "OPTIONAL {?something <" + LORESTORE_PRIVATE + "> ?priv}. "
					+ "OPTIONAL {?something <" + LORESTORE_USER + "> ?user}. "
					+ "}"
				// uris that have an asserted relationship to this uri
				+ "UNION { ?something ?somerel <" + escapedURI + "> . "
					+ "FILTER isURI(?something) ."
					+ "FILTER (?somerel != ore:aggregates) . "
					+ "FILTER (?somerel != rdf:type) . "
					+ "OPTIONAL {?something a ?sometype} ."
					+ "OPTIONAL {?something dc:title ?sometitle.} "
					+ "}"
				// uris that have an asserted relationships from this uri
				+ "UNION {<" + escapedURI + "> ?somerel ?something . "
					+ "FILTER isURI(?something). "
					+ "FILTER (?somerel != rdf:type) . "
					+ "FILTER (?somerel != ore:describes) . "
					+ "OPTIONAL {?something a ?sometype} ."
					+ "OPTIONAL {?something dc:title ?sometitle.}"
					+ "}"
				// if this is a compound object, uris contained
				+ "UNION {<" + escapedURI + "> ore:describes ?aggre ."
					+ "?aggre ?somerel ?something . "
					+ "FILTER isURI(?something) ."
					+ "FILTER (?somerel != rdf:type) ."
					+ "OPTIONAL {?something dc:title ?sometitle . } . "
					+ "OPTIONAL {?something ?anotherrel ?somethingelse . FILTER isURI(?somethingelse)} . "
					+ "OPTIONAL {?something a ?sometype}"
					+ "OPTIONAL {<" + escapedURI + "> <" + LORESTORE_PRIVATE + "> ?priv}. "
					+ "OPTIONAL {<" + escapedURI + "> <" + LORESTORE_USER + "> ?user}. "
					+ "}"
				+ " FILTER (!bound(?priv) || (bound(?priv) && ?user = '" + userURI + "'))"
				+ "}";
		// @formatter:on
		return query;
	}

	@Override
	public long getNumberNamedGraphs() throws InterruptedException {
		ModelSet modelSet = null;
		Model defaultModel = null;
		long numGraphs = 0;
		try {
			modelSet = cf.retrieveConnection();
			defaultModel = modelSet.getDefaultModel();
			numGraphs = defaultModel.countStatements(modelSet.createQuadPattern(Variable.ANY, Variable.ANY, modelSet.createURI(RDF_TYPE_PROPERTY), modelSet.createURI(ORE_RESOURCEMAP_CLASS)));
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		} finally {
			if (defaultModel != null){
				defaultModel.close();
			}
			cf.release(modelSet);
		}
		return numGraphs;
	}
}
