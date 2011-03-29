package oreservlet.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import oreservlet.exceptions.OREException;
import oreservlet.model.CompoundObjectImpl;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * The OREUpdateHandler class processes updates to compound object by
 * OREController
 * 
 * Based loosely on DannoUpdateHandler
 * 
 * @author uqdayers
 */
public class OREUpdateHandler {

	protected final OREControllerConfig occ;
	private final TripleStoreConnectorFactory cf;

	public OREUpdateHandler(OREControllerConfig occ) {
		this.occ = occ;
		this.cf = occ.getContainerFactory();
	}

	/**
	 * Handle POST requests; posting of new Compound Objects
	 * 
	 * @param request
	 *            the servlet request
	 * @return
	 * @throws IOException
	 * @throws RequestFailureException
	 * @throws OREException
	 */
	public String post(InputStream inputRDF) throws RequestFailureException,
			IOException, OREException {
		ModelFactory mf = RDF2Go.getModelFactory();

		String uid = occ.getUidGenerator().newUID();
		URI newUri = mf.createModel().createURI(occ.getBaseUri() + uid);
		Model model = mf.createModel(newUri);

		model.open();
		try {
			model.readFrom(inputRDF, Syntax.RdfXml, occ.getBaseUri());
		} catch (ModelRuntimeException e) {
			throw new RequestFailureException(
					HttpServletResponse.SC_BAD_REQUEST, "Error reading RDF");
		}

		CompoundObjectImpl compoundObject = new CompoundObjectImpl(model);
		compoundObject.assignURI(occ.getBaseUri() + uid);

		// occ.getAccessPolicy().checkCreate(request, model);
		ModelSet ms = cf.retrieveConnection();
		ms.addModel(model);
		ms.commit();
		ms.close();
		model.close();
		// return new OREResponse(model);
		return "redirect:" + "/ore/" + uid;

	}

	public OREResponse delete(MockHttpServletRequest request)
			throws NoSuchRequestHandlingMethodException {
		ModelSet ms = cf.retrieveConnection();
		String stringURI = request.getRequestURI();
		URI contextURI = ms.createURI(stringURI);
		if (!ms.containsModel(contextURI)) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
		ms.removeModel(contextURI);

		return new OREResponse(null);
	}

	public String put(String oreId, InputStream inputRDF)
			throws RequestFailureException, IOException {
		ModelFactory mf = RDF2Go.getModelFactory();
		URI objURI = mf.createModel().createURI(occ.getBaseUri() + oreId);
		Model model = mf.createModel(objURI);

		// TODO: should check that we are replacing an object, and fail if there
		// isn't one already

		model.open();
		try {
			model.readFrom(inputRDF, Syntax.RdfXml, occ.getBaseUri());
		} catch (ModelRuntimeException e) {
			throw new RequestFailureException(
					HttpServletResponse.SC_BAD_REQUEST, "Error reading RDF");
		}

		// TODO: needs to do stuff like maintaining the created/modified dates,
		// and the creator

		ModelSet ms = cf.retrieveConnection();
		ms.removeModel(objURI);
		ms.addModel(model);
		ms.commit();
		ms.close();
		model.close();

		return "redirect:" + "/ore/" + oreId;
	}
}
