package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_PRIVATE;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USER;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OA_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_TARGET_PROPERTY;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OA_TARGET_PROPERTY;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.RDF_TYPE_PROPERTY;

import java.util.ArrayList;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.model.rdf2go.OpenAnnotationImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.LorestoreResponse;
import net.metadata.openannotation.lorestore.servlet.OREController;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The RDF2GoOAQueryHandler class handles queries from the
 * {@link OREController}.
 */
public class RDF2GoOAQueryHandler extends AbstractRDF2GoQueryHandler {

    public RDF2GoOAQueryHandler(LoreStoreControllerConfig occ) {
        super(occ);
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
        ModelAndView mav = new ModelAndView("oa");
        MemoryTripleStoreConnectorFactory mf = new MemoryTripleStoreConnectorFactory();
        ModelSet result = mf.retrieveConnection();
        try {
            URI uri = container.createURI(occ.getBaseUri() + objId);
            LOG.debug("Annotations get " + objId);
            model = container.getModel(uri);
            if (model == null || model.isEmpty()) {
                if (model != null) {
                    model.close();
                }
                LOG.debug("Cannot find requested resource: " + objId);
                throw new NotFoundException("Cannot find resource: " + objId);
            }
            LOG.debug("Annotations check can read");
            checkUserCanRead(model);
            
            LOG.debug ("Annotations about to add model to model set " + result.size());
            result.addModel(model);
            mav.addObject(LorestoreResponse.MODELSET_KEY, result); 
        } catch (AccessDeniedException ex) {
            throw ex;
        } finally {
            model.close();
            cf.release(container);
        }
        
        return mav;
    }
    protected String getDefaultViewName() {
        return "oa";
    }
    protected void checkUserCanRead(Model m) {
        OpenAnnotationImpl annotation = new OpenAnnotationImpl(m);
        try {
            ap.checkRead(annotation);
        } finally {
            annotation.close();
        }
    }
    @Override
    public ModelAndView searchAtomQuery(String urlParam,
                    String matchpred, String matchval, String orderBy, Boolean includeAbstract, Boolean asTriples) throws RepositoryException,
                    MalformedQueryException, QueryEvaluationException,
                    TupleQueryResultHandlerException, InterruptedException, RDFHandlerException {
        String queryString = generateSearchQuery(urlParam, matchpred, matchval, orderBy, 0, -1, includeAbstract, asTriples);
        ModelAndView mav = new ModelAndView("oaAtom");
        mav.addObject("feedTitle","Annotations" 
                + (!urlParam.equals("")? " targeting " + urlParam : "" )
                + (!matchpred.equals("")? " containing " + matchval : "")
                + (!matchval.equals("")? " matching " + matchval : "")
                + (!orderBy.equals("")? " ordered by " + orderBy : ""));
        TupleQueryResult queryResult = runSparqlQueryIntoQR(queryString);
        try {
            ModelSet container = cf.retrieveConnection();
            ArrayList <Model> allAnnotations = new ArrayList<Model>();
                Model model = null;
                while (queryResult.hasNext()) {
                    
                    BindingSet bs = queryResult.next();
                    String uri =  bs.getValue("g").stringValue();
                    LOG.debug("URI is " + uri);
                    if (uri != null){
                        model = container.getModel(container.createURI(uri));
                        if (model == null || model.isEmpty()) {
                            if (model != null) {
                                model.close();
                            }
                        }
                    } 
                    allAnnotations.add(model);
                }
                mav.addObject("annotationlist", allAnnotations);
                LOG.debug("atom query found " + allAnnotations.size());
        } catch (Exception e){
            LOG.debug("Problem with atom view " + e.getMessage());
        }
        return mav;
    }
    @Override
    public ModelAndView browseAtomQuery(String url) throws RepositoryException,
            MalformedQueryException, QueryEvaluationException,
            TupleQueryResultHandlerException, InterruptedException {
        String queryString = generateBrowseQuery(url);
        ModelAndView mav = new ModelAndView("oaAtom");
        mav.addObject("feedTitle", "Annotations targeting " + url);

        TupleQueryResult queryResult = runSparqlQueryIntoQR(queryString);
        try {
            ModelSet container = cf.retrieveConnection();
            ArrayList <Model> allAnnotations = new ArrayList<Model>();
                Model model = null;
                while (queryResult.hasNext()) {
                    
                    BindingSet bs = queryResult.next();
                    String uri =  bs.getValue("g").stringValue();
                    LOG.debug("URI is " + uri);
                    if (uri != null){
                        model = container.getModel(container.createURI(uri));
                        if (model == null || model.isEmpty()) {
                            if (model != null) {
                                model.close();
                            }
                        }
                    } 
                    allAnnotations.add(model);
                }
                mav.addObject("annotationlist", allAnnotations);
                LOG.debug("atom query found " + allAnnotations.size());
        } catch (Exception e){
            LOG.debug("Problem with atom view " + e.getMessage());
        }
        
        return mav;
    }

    protected String generateBrowseQuery(String escapedURL) {

        // Needs to match both www and non-www version of URL
        String altURL = makeAltURL(escapedURL);
        String userURI = occ.getIdentityProvider().obtainUserURI();
        // get all annotations that target url (including parts of it - via isPartOf and via constraints)
        // @formatter:off
        String query = "SELECT DISTINCT ?g ?a ?m ?t ?priv"
                + " WHERE { graph ?g {"
                // regular target OAC
                + " {?anno <http://www.openannotation.org/ns/hasTarget> <" + escapedURL + "> .}"
                + " UNION {?anno <http://www.openannotation.org/ns/hasTarget> <" + altURL + "> .} "
                // regular target OA
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> <" + escapedURL + "> .}"
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> <" + altURL + "> .} "
                // target with media fragment OAC
                + " UNION {?anno <http://www.openannotation.org/ns/hasTarget> ?t . ?t <http://purl.org/dc/terms/isPartOf> <" + escapedURL + ">} "
                + " UNION {?anno <http://www.openannotation.org/ns/hasTarget> ?t . ?t <http://purl.org/dc/terms/isPartOf> <" + altURL+ ">} "
                // target with media fragment OA
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> ?t . ?t <http://purl.org/dc/terms/isPartOf> <" + escapedURL + ">} "
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> ?t . ?t <http://purl.org/dc/terms/isPartOf> <" + altURL+ ">} "
                // constrained target OAC
                + " UNION {?anno <http://www.openannotation.org/ns/hasTarget> ?t . ?t <http://www.openannotation.org/ns/constrains> <" + escapedURL + ">} "
                + " UNION {?anno <http://www.openannotation.org/ns/hasTarget> ?t . ?t <http://www.openannotation.org/ns/constrains> <" + altURL + ">} "
                // constrained target OA
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> ?t . ?t <http://www.w3.org/ns/oa#hasSource> <" + escapedURL + ">} "
                + " UNION {?anno <http://www.w3.org/ns/oa#hasTarget> ?t . ?t <http://www.w3.org/ns/oa#hasSource> <" + altURL + ">} "
                + "}."
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
        String queryString = "SELECT DISTINCT ?g " + (asTriples? "?creator ?date ?title ?v ?priv": "")
                + " where {"
                + "   {?g a <" + OAC_ANNOTATION_CLASS + "> } UNION {?g a <" + OA_ANNOTATION_CLASS + ">} ."
                + "   graph ?g {"
                + escapedURL + " " + predicate + " ?v ."
                +        filter
                + " OPTIONAL {?g <http://www.w3.org/ns/oa#annotatedAt> ?date} . "
                + " OPTIONAL {?g <http://www.w3.org/ns/oa#annotatedBy> ?x . ?x <http://xmlns.com/foaf/0.1/name> ?creator} . ";
       if (asTriples) {
                queryString += " OPTIONAL {?g <http://purl.org/dc/elements/1.1/title> ?title} . ";
       }
       queryString +=  "}. OPTIONAL {?g <" + LORESTORE_PRIVATE + "> ?priv}. "
                + " OPTIONAL {?g <" + LORESTORE_USER + "> ?user}. "
                + " FILTER (!bound(?priv) || (bound(?priv) && ?user = '" + userURI + "'))"
                + "}" 
                + " ORDER BY ?" + tempOrderBy;
        if (asTriples){
             queryString += (offset == 0? "" : " OFFSET " + offset)
                + (limit == -1? "" : " LIMIT " + limit);
        }
        LOG.info(queryString);
        // @formatter:on
        return queryString;
    }

    @Override
    public long getNumberNamedGraphs() throws InterruptedException {
        ModelSet modelSet = null;
        Model defaultModel = null;
        long numGraphs = 0;
        try {
            modelSet = cf.retrieveConnection();
            defaultModel = modelSet.getDefaultModel();
            // temporarily support both OAC and OA annotations
            numGraphs = defaultModel.countStatements(
                    modelSet.createQuadPattern(
                            Variable.ANY, 
                            Variable.ANY, 
                            modelSet.createURI(RDF_TYPE_PROPERTY), 
                            modelSet.createURI(OAC_ANNOTATION_CLASS)
                    )) + defaultModel.countStatements(
                                    modelSet.createQuadPattern(
                                            Variable.ANY, 
                                            Variable.ANY, 
                                            modelSet.createURI(RDF_TYPE_PROPERTY), 
                                            modelSet.createURI(OA_ANNOTATION_CLASS)
                                    ));
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

    @Override
    protected ModelAndView runSparqlQueryIntoGraphsMAV(String queryString, int offset, int limit) throws RepositoryException, MalformedQueryException, QueryEvaluationException, TupleQueryResultHandlerException, InterruptedException {
        ModelAndView mav = new ModelAndView("oa");
        TupleQueryResult queryResult = runSparqlQueryIntoQR(queryString);
        ModelSet container = cf.retrieveConnection();
        // construct a ModelSet rather than a list for ease of serialization
        MemoryTripleStoreConnectorFactory mf = new MemoryTripleStoreConnectorFactory();
        ModelSet result = mf.retrieveConnection();
        if (result == null){
            LOG.debug("Problem with model set");
        } else {
            LOG.debug("created model set " + result.size());
        }
        Model model = null;
        
        try{
            int resultCounter = 0;
            while (queryResult.hasNext() && (limit == -1 || resultCounter < (offset + limit))) {
                BindingSet bs = queryResult.next();
                //LOG.debug("processing " + foo + " " + bs.getValue("g").stringValue());
                if (resultCounter >= offset){
                String uri =  bs.getValue("g").stringValue();
                if (uri != null){
                    model = container.getModel(container.createURI(uri));
                    if (model == null || model.isEmpty()) {
                        if (model != null) {
                            model.close();
                        }
                    } else {
                            result.addModel(model);
                        model.close();
                    }
                }
                }
                resultCounter = resultCounter + 1;
            }
            
            mav.addObject(LorestoreResponse.MODELSET_KEY, result);
            LOG.debug ("found " + result.size() + " annotations");
            
        } catch (Exception ex) {
            // The model is normally closed after the response view is generated,
            // but since an exception is thrown it must be closed here.
            if (model != null){
                model.close();
            }
            
            // throw.ex;
            
        } finally {
            cf.release(container);
        }
        return mav;
    }

}
