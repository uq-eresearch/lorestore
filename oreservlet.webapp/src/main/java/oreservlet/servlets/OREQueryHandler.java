package oreservlet.servlets;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

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
			HttpServletResponse response) throws RepositoryException, RequestFailureException  {
		String uri = request.getRequestURI().toString();
		
		Repository repo = ORETypeFactory.getRemoteRepo();
		ModelSet container = new RepositoryModelSet(repo);
		try {
			Model model = ORETypeFactory.retrieve(uri);
			if (model == null) {
	            throw new RequestFailureException(
	                    HttpServletResponse.SC_NOT_FOUND,  
	                    "No resource for '" + uri + "'");
			}
			occ.getAccessPolicy().checkRead(request, model);
			
			return new OREResponse(model);
		} finally {
			container.close();
		}
	}

}
