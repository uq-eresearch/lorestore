package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOACQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOACUpdateHandler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class OACController {
	private final Logger LOG = Logger.getLogger(OACController.class);

	private final LoreStoreControllerConfig occ;
	private LoreStoreQueryHandler qh;
	private LoreStoreUpdateHandler uh;

	public OACController(LoreStoreControllerConfig occ) {
		this.occ = occ;
		this.qh = new RDF2GoOACQueryHandler(occ);
		this.uh = new RDF2GoOACUpdateHandler(occ);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(InputStream inputRDF)
			throws RequestFailureException, IOException, LoreStoreException,
			InterruptedException {

		return uh.post(inputRDF);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") String annoId)
			throws NotFoundException, InterruptedException,
			RequestFailureException {
		return qh.getNamedGraphObject(annoId);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") String annoId, InputStream in)
			throws RequestFailureException, IOException, LoreStoreException,
			InterruptedException {
		return uh.put(annoId, in);
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
			@RequestParam(value = "includeAbstract", defaultValue = "false") Boolean includeAbstract) throws Exception {
		
			return qh.searchQuery(urlParam, matchpred, matchval);
		
	}

	
	@RequestMapping(value = "/feed", params = "annotates", method = RequestMethod.GET)
	public ModelAndView rssRefersToQuery(
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
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ RequestFailureException.class, NotFoundException.class })
	public void return404(Exception ex, HttpServletResponse response)
			throws IOException {
		LOG.debug("Handling Exception, returning 404" + ex);
		response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	}

	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ AccessDeniedException.class })
	public void return403(AccessDeniedException ex, HttpServletResponse response) throws IOException {
		LOG.debug("Handling Exception, returning 403: " + ex);
		response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
	}
	
	
	
	public LoreStoreControllerConfig getControllerConfig() {
		return occ;
	}
}
