package oreservlet.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openrdf.repository.RepositoryException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class OREController {

	private final OREControllerConfig occ;
	private OREQueryHandler qh;
	private OREUpdateHandler uh;

	public OREController(OREControllerConfig occ) {
		this.occ = occ;

	}

	@RequestMapping(method = RequestMethod.POST)
	public OREResponse post(HttpServletRequest request,
			HttpServletResponse response) throws RequestFailureException,
			IOException {

		return uh.post(request, response);
	}

	@RequestMapping(method = RequestMethod.GET)
	public OREResponse get(HttpServletRequest request,
			HttpServletResponse response) throws RequestFailureException,
			RepositoryException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("") || pathInfo.equals("/")) {
			return qh.query(request, response);
		} else {
			return qh.plainGet(request, response);
		}
	}

	public OREControllerConfig getControllerConfig() {
		return occ;
	}

}
