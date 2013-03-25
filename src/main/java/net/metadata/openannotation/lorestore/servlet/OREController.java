package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREUpdateHandler;

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
public class OREController {
    private final Logger LOG = Logger.getLogger(OREController.class);

    private final LoreStoreControllerConfig occ;
    private LoreStoreQueryHandler oreqh;
    private LoreStoreUpdateHandler oreuh;

    public OREController(LoreStoreControllerConfig occ) {
        this.occ = occ;
        this.oreqh = new RDF2GoOREQueryHandler(occ);
        this.oreuh = new RDF2GoOREUpdateHandler(occ);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView post(InputStream inputRDF, 
            @RequestHeader("Content-Type") String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException {

        return oreuh.post(inputRDF, contentType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable("id") String oreId)
            throws NotFoundException, InterruptedException,
            RequestFailureException {
        return oreqh.getNamedGraphObject(oreId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ModelAndView put(@PathVariable("id") String oreId, InputStream in, 
            @RequestHeader("Content-Type") String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException {
        return oreuh.put(oreId, in, contentType);
    }

    @RequestMapping(value = "/", params = "refersTo", method = RequestMethod.GET)
    public ModelAndView refersToQuery(
            @RequestParam("refersTo") String urlParam) throws Exception {
        if (urlParam == null || urlParam.isEmpty()) {
            throw new InvalidQueryParametersException(
                    "Missing or empty query parameters");
        }
        return oreqh.refersToQuery(urlParam);
    }

    @RequestMapping(value = "/", params = { "matchval" }, method = RequestMethod.GET)
    public ModelAndView searchQuery(
            @RequestParam(value = "refersTo", defaultValue = "") String urlParam,
            @RequestParam(value = "matchpred", defaultValue = "") String matchpred,
            @RequestParam("matchval") String matchval,
            @RequestParam(value = "includeAbstract", defaultValue = "false") Boolean includeAbstract,
            @RequestParam(value = "asTriples", defaultValue = "true") Boolean asTriples) throws Exception {
        LOG.info("searchQuery " + urlParam + " " + matchval + " " + matchpred + " " + includeAbstract);
        
        return oreqh.searchQuery(urlParam, matchpred, matchval, includeAbstract, asTriples);
        
    }

    @RequestMapping(value = "/", params = { "matchpred" }, method = RequestMethod.GET)
    public ModelAndView searchPredQuery(
            @RequestParam(value = "refersTo", defaultValue = "") String urlParam,
            @RequestParam("matchpred") String matchpred,
            @RequestParam(value = "matchval", defaultValue = "") String matchval,
            @RequestParam(value = "includeAbstract", defaultValue = "false") Boolean includeAbstract,
            @RequestParam(value = "asTriples", defaultValue = "true") Boolean asTriples) throws Exception {
       
        return oreqh.searchQuery(urlParam, matchpred, matchval, includeAbstract, asTriples);
        
    }
    
    @RequestMapping(value = "/", params = "exploreFrom", method = RequestMethod.GET)
    public ResponseEntity<String> exploreQuery(
            @RequestParam("exploreFrom") String urlParam) throws Exception {
        if (urlParam == null || urlParam.isEmpty()) {
            throw new InvalidQueryParametersException(
                    "Missing or empty query parameters");
        }

        return oreqh.exploreQuery(urlParam);
    }
    
    @RequestMapping(value = "/feed", params = "refersTo", method = RequestMethod.GET)
    public ModelAndView atomRefersToQuery(
            @RequestParam("refersTo") String urlParam) throws Exception {
        if (urlParam == null || urlParam.isEmpty()) {
            throw new InvalidQueryParametersException(
                    "Missing or empty query parameters");
        }

        return oreqh.browseAtomQuery(urlParam);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") String oreId)
            throws NotFoundException, InterruptedException {
        oreuh.delete(oreId);
        return new ResponseEntity<String>("", HttpStatus.NO_CONTENT);
    }

    public LoreStoreControllerConfig getControllerConfig() {
        return occ;
    }
}
