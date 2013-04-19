package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOAQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOAUpdateHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.OAValidationHandler;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class AnnotationController {
    private final Logger LOG = Logger.getLogger(AnnotationController.class);

    private final LoreStoreControllerConfig occ;
    private LoreStoreQueryHandler qh;
    private LoreStoreUpdateHandler uh;
    private LoreStoreValidationHandler vh;
    public AnnotationController(LoreStoreControllerConfig occ) {
        this.occ = occ;
        this.qh = new RDF2GoOAQueryHandler(occ);
        this.uh = new RDF2GoOAUpdateHandler(occ);
        this.vh = new OAValidationHandler(occ);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView post(InputStream inputRDF, 
            @RequestHeader("Content-Type") String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException {
            
        return uh.post(inputRDF, contentType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable("id") String annoId)
            throws NotFoundException, InterruptedException,
            RequestFailureException {
        return qh.getNamedGraphObject(annoId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ModelAndView put(@PathVariable("id") String annoId, InputStream in, 
            @RequestHeader("Content-Type") String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException {
        return uh.put(annoId, in, contentType);
    }

    @RequestMapping(value = "/", params = "annotates", method = RequestMethod.GET)
    public ModelAndView refersToQuery(
            @RequestParam("annotates") String urlParam) throws Exception {
        if (urlParam == null || urlParam.isEmpty()) {
            throw new InvalidQueryParametersException(
                    "Missing or empty query parameters");
        }

        return qh.refersToQuery(urlParam);
    }

    @RequestMapping(value = "/", params = { "matchval" }, method = RequestMethod.GET)
    public ModelAndView searchQuery(
            @RequestParam(value = "annotates", defaultValue = "") String urlParam,
            @RequestParam(value = "matchpred", defaultValue = "") String matchpred,
            @RequestParam("matchval") String matchval,
            @RequestParam(value = "orderBy", defaultValue = "date") String orderBy,
            @RequestParam(value = "includeAbstract", defaultValue = "false") Boolean includeAbstract,
            @RequestParam(value = "asTriples", defaultValue = "true") Boolean asTriples) throws Exception {
        
            return qh.searchQuery(urlParam, matchpred, matchval, orderBy, includeAbstract, asTriples);
        
    }
    @RequestMapping(value = "/", params = { "matchpred" }, method = RequestMethod.GET)
    public ModelAndView searchPredQuery(
            @RequestParam(value = "annotates", defaultValue = "") String urlParam,
            @RequestParam(value = "matchval", defaultValue = "") String matchval,
            @RequestParam("matchpred") String matchpred,
            @RequestParam(value = "orderBy", defaultValue = "date") String orderBy,
            @RequestParam(value = "includeAbstract", defaultValue = "false") Boolean includeAbstract,
            @RequestParam(value = "asTriples", defaultValue = "true") Boolean asTriples) throws Exception {
        
            return qh.searchQuery(urlParam, matchpred, matchval, orderBy, includeAbstract, asTriples);
        
    }
    @RequestMapping(value = "/feed", params = "annotates", method = RequestMethod.GET)
    public ModelAndView atomRefersToQuery(
            @RequestParam("annotates") String urlParam) throws Exception {
        if (urlParam == null || urlParam.isEmpty()) {
            throw new InvalidQueryParametersException(
                    "Missing or empty query parameters");
        }

        return qh.browseAtomQuery(urlParam);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") String oreId)
            throws NotFoundException, InterruptedException {
        uh.delete(oreId);
        return new ResponseEntity<String>("", HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/validate", method = RequestMethod.POST)
    public ModelAndView validate(InputStream inputRDF, @RequestHeader("Content-Type") String contentType)
                    throws RequestFailureException, IOException, LoreStoreException,
                    InterruptedException {

            return vh.validate(inputRDF, contentType);
    }
    
    public LoreStoreControllerConfig getControllerConfig() {
        return occ;
    }
}
