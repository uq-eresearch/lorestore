package net.metadata.openannotation.lorestore.servlet;

import java.io.Writer;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface LoreStoreQueryHandler {

	/**
	 * Finds a single compound object or annotation of id ngId.
	 * <p>
	 * The object is wrapped in a ModelAndView
	 * 
	 * @param oreId
	 *            The ID of the requested object.
	 * @return a ModelAndView containing the single object
	 * @throws NotFoundException
	 *             if the object doesn't exist here
	 */
	public ModelAndView getNamedGraphObject(String ngId) throws NotFoundException,
			InterruptedException;
	


	/**
	 * Finds all objects referring to the supplied URL, returns a
	 * response ready to be rendered as Atom.
	 * 
	 * @param url
	 * @return a model containing a sparqlxml string and marked to be displayed
	 *         by an atom view
	 */
	public ModelAndView browseAtomQuery(String url) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			TupleQueryResultHandlerException, InterruptedException;

	/**
         * Find all objects matchign a search query and returns response ready to be 
         * rendered as Atom.
         * 
         * @param urlParam A specific url that the compound object must reference
         * @param matchpred A predicate to match the value against
         * @param matchval a keyword string to either look for anywhere, or only 
         * for the supplied matchpred
         * @return atom model and view
         */
        public ModelAndView searchAtomQuery(String urlParam, String matchpred,
                        String matchval, String orderBy, Boolean includeAbstract, Boolean asTriples)
                        throws RepositoryException, MalformedQueryException,
                        QueryEvaluationException, TupleQueryResultHandlerException,
                        InterruptedException, RDFHandlerException;
	/**
	 * Handles a search query
	 * 
	 * @param urlParam A specific url that the compound object must reference
	 * @param matchpred A predicate to match the value against
	 * @param matchval a keyword string to either look for anywhere, or only 
	 * for the supplied matchpred
	 * @return a http response containing results in sparqlXML format
	 */
	public ModelAndView searchQuery(String urlParam, String matchpred,
			String matchval, String orderBy, int offset, int limit, Boolean includeAbstract, Boolean asTriples)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException;
	
	/**
	 * Handles an annotates query
	 * 
	 * @param urlParam A specific url that the annotation must target
	
	 */
	public ModelAndView refersToQuery(String urlParam)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, InvalidQueryParametersException, RDFHandlerException;
	
	/**
	 * LORE explore results for a url
	 * 
	 * @param url
	 *            the url to find explore results for
	 * @return a http response containing results in sparqlXML format
	 */
	public ResponseEntity<String> exploreQuery(String url)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException;

	/**
	 * SPARQL Endpoint, only supports SELECT queries
	 * 
	 * @param query
	 *            the SPARQL query to execute
	 * @return a http response containing results in sparqlXML format
	 */
	public ResponseEntity<String> sparqlQuery(String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, TupleQueryResultHandlerException,
			InterruptedException, RDFHandlerException;

	/**
	 * Counts the total number of triples stored
	 * 
	 * @return the number of triples in the triplestore
	 * @throws InterruptedException
	 */
	public long getNumberTriples() throws InterruptedException;
	
	/**
	 * Counts the total number of named graphs stored 
	 * (e.g. OA annotation or ORE resource map)
	 * 
	 * @return the number of named graphs in the triplestore
	 * @throws InterruptedException
	 */
	public long getNumberNamedGraphs() throws InterruptedException;

	/**
	 * Writes the entire contents of the triplestore out in TriG format.
	 * 
	 * Note: This uses the sesame API directly, instead of through rdf2go.
	 * 
	 * @param outputWriter
	 *            where to write the TriG output
	 * @throws Exception
	 */
	public void exportAll(Writer outputWriter) throws Exception;

}