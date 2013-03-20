package net.metadata.openannotation.lorestore.servlet.rdf2go;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.rdf2go.NamedGraphImpl;
import net.metadata.openannotation.lorestore.model.rdf2go.OpenAnnotationImpl;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.LoreStoreValidationHandler;
import net.metadata.openannotation.lorestore.servlet.OREController;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.OATripleCallback;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.rdf2go.RepositoryModelFactory;
import org.springframework.web.servlet.ModelAndView;

import de.dfki.km.json.JSONUtils;
import de.dfki.km.json.jsonld.JSONLD;
import de.dfki.km.json.jsonld.JSONLDProcessingError;

import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * The RDF2GoOAQueryHandler class handles queries from the
 * {@link OREController}.
 */
public class OAValidationHandler implements LoreStoreValidationHandler {

    protected final LoreStoreControllerConfig occ;
    protected final TripleStoreConnectorFactory cf;
    protected LoreStoreAccessPolicy ap;
    protected final Logger LOG = Logger.getLogger(OAValidationHandler.class);
    private List<Map<String,Object>> validationRules;
    
    public OAValidationHandler(LoreStoreControllerConfig occ) {
        this.occ = occ;
        this.cf = occ.getContainerFactory();
        this.ap = occ.getAccessPolicy();
    }

    @Override
    public ModelAndView validate(InputStream inputRDF, String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException {
        ModelAndView mav = new ModelAndView("validation");
        // create temp model for annotation(s) from inputRDF
        RepositoryModelFactory mf = new RepositoryModelFactory();
        Model model = mf.createModel(Reasoning.owl);
        try {
            model.open();
            if (contentType.equals(Syntax.RdfXml.getMimeType())
                    || contentType.equals(Syntax.Trix.getMimeType()) 
                    || contentType.equals(Syntax.Trig.getMimeType())){
                model.readFrom(inputRDF, Syntax.forMimeType(contentType), occ.getBaseUri());
            
            } else if (contentType.equals("application/json")){
                Object jsonObject = JSONUtils.fromInputStream(inputRDF);
                // TODO if no @context, inject default context
                // TODO if no @id, inject dummy identifier (will be replaced)
                OATripleCallback callback = new OATripleCallback();
                callback.setModel(model);
                JSONLD.toRDF(jsonObject, callback);
            } else {
                throw new RequestFailureException(HttpServletResponse.SC_BAD_REQUEST, "Acceptable content types are RDF/XML or JSON-LD");
            }
        } catch (ModelRuntimeException e) {
            try{
                LOG.debug("Model read failed " + e.getMessage());
                model.close();
            } catch (Exception e2){
                // ignore
            }
            throw new RequestFailureException(HttpServletResponse.SC_BAD_REQUEST, "Error reading RDF " + e.getMessage());
        } catch (JSONLDProcessingError e) {
            LOG.info("Post: Model read failed " + e.getMessage());
            throw new RequestFailureException(HttpServletResponse.SC_BAD_REQUEST, "Error reading JSON " + e.getMessage());
        }

        // Load validation rules 
        if (this.validationRules == null){
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("OAConstraintsSPARQL.json");
            this.validationRules = (List<Map<String,Object>>) JSONUtils.fromInputStream(in);
        }
        // clone the validation rules into an object that we will pass to the ModelAndView
        ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>(validationRules);
        for (Map<String,Object> section : result){
            LOG.info("Running constraints for " + section.get("section"));
            // for each section.constraints
            for (Map<String,String> constraint: ((List<Map<String,String>>)section.get("constraints"))){
                String query = constraint.get("query");
                // run the query and store the result back into the constraint object
                // if query returns results, put severity as status, otherwise status is "ok"
                constraint.put("status", constraint.get("severity"));
            }
            
        }
        
        // run each query and generate report data to store in ModelAndView:
        // pass/fail, title, link, description
        mav.addObject("result", result);
        // destroy temp rdf model
        model.close();
        return mav;
    }

}
