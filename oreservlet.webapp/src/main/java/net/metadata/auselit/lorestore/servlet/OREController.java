package net.metadata.auselit.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import net.metadata.auselit.lorestore.exceptions.OREException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.RedirectView;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class OREController {
    private static final Logger LOG = Logger.getLogger(OREController.class);

	private final OREControllerConfig occ;
	private OREQueryHandler qh;
	private OREUpdateHandler uh;

	public OREController(OREControllerConfig occ) {
		this.occ = occ;
		this.qh = new OREQueryHandler(occ);
		this.uh = new OREUpdateHandler(occ);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String post(InputStream inputRDF) throws RequestFailureException,
			IOException, OREException {

		return uh.post(inputRDF);
	}

//	@RequestMapping(method = RequestMethod.GET)
//	public OREResponse get(HttpServletRequest request)
//			throws RepositoryException, ServletException {
//		String pathInfo = request.getPathInfo();
//		if (pathInfo == null || pathInfo.equals("") || pathInfo.equals("/")) {
//			LOG.error("Incorrect path in GET request");
//			return null;
//		} else {
//			return qh.plainGet(request);
//		}
//	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public OREResponse get(@PathVariable("id") String oreId) {
		return qh.getOreObject(oreId);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String put(@PathVariable("id") String oreId, InputStream in) throws RequestFailureException, IOException, OREException {
		return uh.put(oreId, in);
	}

	@RequestMapping(value = "/", params="refersTo", method = RequestMethod.GET)
	public ResponseEntity<String> refersToQuery(@RequestParam("refersTo") String urlParam) throws Exception {
		return qh.browseQuery(urlParam);
	}
	
	@RequestMapping(value = "/", params="exploreFrom", method = RequestMethod.GET)
	public ResponseEntity<String> exploreQuery(@RequestParam("exploreFrom") String urlParam) throws Exception {
		return qh.exploreQuery(urlParam);
	}
	
	public View saveORE() {
		return new RedirectView("", true);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ RequestFailureException.class })
	public void return404() {
		LOG.debug("Handling RequestFailureException, returning 404");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public OREResponse delete(@PathVariable("id") String oreId, MockHttpServletRequest servletRequest)
			throws NoSuchRequestHandlingMethodException {
		return uh.delete(servletRequest, oreId);
	}

	public OREControllerConfig getControllerConfig() {
		return occ;
	}
}
