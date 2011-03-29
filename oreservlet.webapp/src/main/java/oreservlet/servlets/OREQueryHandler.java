package oreservlet.servlets;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import au.edu.diasb.annotation.danno.db.RDFDBContainer;
import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * The OREQueryHandler class handles queries from the {@link OREController}.
 * 
 * @author uqdayers
 */
public class OREQueryHandler {

	protected final OREControllerConfig occ;
	
	public OREQueryHandler(OREControllerConfig occ) {
		this.occ = occ;
	}
	
	public OREResponse query(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		// TODO Auto-generated method stub
		return null;
	}

	public OREResponse plainGet(HttpServletRequest request,
			HttpServletResponse response) throws RequestFailureException {
		String uri = request.getRequestURI().toString();
		
		RDFDBContainer container = occ.getContainerFactory().connect(false);
		try {
			RDFContainer resContainer = plainGet(container, uri);
			occ.getAccessPolicy().checkRead(request, resContainer);
			
			return new OREResponse(resContainer);
		} finally {
			container.close();
		}
	}

	private RDFContainer plainGet(RDFDBContainer container, String uri) throws RequestFailureException {
		RDFContainer resContainer = container.extractResourceClosure(uri, occ.getBlankNodeClosureDepth());
		if (resContainer == null) {
            throw new RequestFailureException(
                    HttpServletResponse.SC_NOT_FOUND,  
                    "No resource for '" + uri + "'");
		}
		return resContainer;
	}

}
