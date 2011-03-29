package oreservlet.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import oreservlet.exceptions.OREException;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.RepositoryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

	@RequestMapping(method = RequestMethod.GET)
	public OREResponse get(HttpServletRequest request)
			throws RepositoryException, ServletException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("") || pathInfo.equals("/")) {
			return qh.query(request);
		} else {
			return qh.plainGet(request);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public OREResponse get(@PathVariable("id") String oreId) {
		return qh.getOreObject(oreId);
	}

	@RequestMapping(value = "/", params="refersTo", method = RequestMethod.GET)
	public ResponseEntity<String> refersToQuery(@RequestParam("refersTo") String refersTo) throws Exception {
		String body = qh.browseQuery(refersTo);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<String>(body, responseHeaders, HttpStatus.OK);
	}
	
	public View saveORE() {
		return new RedirectView("", true);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ RequestFailureException.class })
	public void return404() {

	}

	public OREControllerConfig getControllerConfig() {
		return occ;
	}

	public OREResponse delete(MockHttpServletRequest servletRequest)
			throws NoSuchRequestHandlingMethodException {
		return uh.delete(servletRequest);
	}

}
