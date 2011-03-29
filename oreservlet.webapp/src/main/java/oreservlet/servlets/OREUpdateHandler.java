package oreservlet.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.UID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oreservlet.model.CompoundObject;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.RedirectView;

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
	 */
	public String post(InputStream inputRDF) throws RequestFailureException,
			IOException {
		ModelFactory mf = RDF2Go.getModelFactory();

		String uid = new UID().toString();
		URI newUri = mf.createModel().createURI(occ.getBaseUri() + uid);
		Model model = mf.createModel(newUri);
		

		model.open();
		try {
			model.readFrom(inputRDF, Syntax.RdfXml,
					occ.getBaseUri());
		} catch (ModelRuntimeException e) {
			throw new RequestFailureException(
					HttpServletResponse.SC_BAD_REQUEST, "Error reading RDF");
		}

//		occ.getAccessPolicy().checkCreate(request, model);
		ModelSet ms = cf.retrieveConnection();
		ms.addModel(model);
		ms.commit();
		ms.close();
		model.close();
//		return new OREResponse(model);
		return "redirect:" + "/ore/" + uid;

	}

	/**
	 * This create method for Compound Objects is used by the POST operation. It
	 * reads the new version of the object from the request input stream, sets
	 * its URI and then adds the resources to the persistent container. If
	 * successful, and RDF container containing the object is returned.
	 * Otherwise, a failure status and message are set in the response.
	 * 
	 * @param container
	 *            the container to put the created resource into
	 * @param uri
	 *            an exiting URI to be (re-)used for the Compound Object. If
	 *            this argument is <code>null</code>, a new URI should be
	 *            allocated
	 * @param request
	 *            the HTTP request object
	 * @param response
	 *            the HTTP response object
	 * @return an RDF container containing the RDF to be sent back in response
	 * @throws IOException
	 * @throws RequestFailureException
	 */
	// private CompoundObject create(Model container, String uri,
	// HttpServletRequest request, HttpServletResponse response)
	// throws IOException, RequestFailureException {
	// CompoundObject object;
	// try {
	// ModelSet connection = cf.retrieveConnection();
	// Model model =
	// oreservlet.servlets.ORETypeFactory.create(request.getInputStream(),
	// occ.getBaseUri());
	// object = tf.createCompoundObject(request.getInputStream());
	// return create(request, container, uri, object, response);
	// } catch (RDFParserException ex) {
	// throw new RequestFailureException(
	// HttpServletResponse.SC_BAD_REQUEST, "Malformed RDF-XML", ex);
	// }
	//
	// }

	private CompoundObject create(HttpServletRequest request, Model container,
			String uri, CompoundObject object, HttpServletResponse response) {
		// Assign the CO's URI.
		if (uri == null) {
			// String newUri = URIGenerator.createNewRandomUniqueURI();
			// object.assignURI(newUri);
		}
		container.addModel(object);

		// Set the Location and status code if we have a response object.
		if (response != null) {
			response.setStatus(uri == null ? HttpServletResponse.SC_CREATED
					: HttpServletResponse.SC_OK);
			if (uri == null) {
				response.setHeader("Location", object.getContextURI()
						.toString());
			}
		}
		return object;
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
}
