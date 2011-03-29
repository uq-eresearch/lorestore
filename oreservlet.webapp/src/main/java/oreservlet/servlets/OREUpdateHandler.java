package oreservlet.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.impl.URIGenerator;
import org.ontoware.rdf2go.model.node.URI;

import oreservlet.common.ORETypeFactory;
import oreservlet.model.CompoundObject;
import au.edu.diasb.annotation.danno.db.RDFDBContainer;
import au.edu.diasb.annotation.danno.db.RDFDBContainerFactory;
import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.annotation.danno.model.RDFParserException;
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
	protected final RDFDBContainerFactory cf;
	protected final UIDGenerator gen;
	protected final ORETypeFactory tf;

	public OREUpdateHandler(OREControllerConfig occ) {
		this.cf = occ.getContainerFactory();
		this.occ = occ;
		this.gen = occ.getUIDGenerator();
		this.tf = occ.getTypeFactory();
	}

	/**
	 * Handle POST requests; posting of new Compound Objects
	 * 
	 * @param request
	 *            the servlet request
	 * @param response
	 *            the servlet response
	 * @return
	 * @throws IOException 
	 * @throws RequestFailureException 
	 */
	public OREResponse post(HttpServletRequest request,
			HttpServletResponse response) throws RequestFailureException, IOException {
		// FIXME: totally wrong, just compile
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model container = modelFactory.createModel();
		try {
			container.open();

			CompoundObject newObject = create(container, null, request, response);
			if (newObject != null) {
				occ.getAccessPolicy().checkCreate(request, newObject);
				container.commit();
				return new OREResponse(newObject);
			} else {
				return null;
			}
		} finally {
			// Abort any uncommitted transactions
			container.close();
		}
	}

	/**
	 * This create method for Compound Objects is used by the POST operation.
	 * It reads the new version of the object from the request input stream, sets
	 * its URI and then adds the resources to the persistent container. If successful,
	 * and RDF container containing the object is returned. Otherwise, a failure status
	 * and message are set in the response.
	 * 
	 * @param container the container to put the created resource into
	 * @param uri an exiting URI to be (re-)used for the Compound Object. If this argument
	 * is <code>null</code>, a new URI should be allocated
	 * @param request the HTTP request object
	 * @param response the HTTP response object
	 * @return an RDF container containing the RDF to be sent back in response
	 * @throws IOException
	 * @throws RequestFailureException
	 */
	private CompoundObject create(Model container, String uri,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, RequestFailureException {
		CompoundObject object;
		try {
			object = tf.createCompoundObject(request.getInputStream());
			return create(request, container, uri, object, response);
		} catch (RDFParserException ex) {
			throw new RequestFailureException(
					HttpServletResponse.SC_BAD_REQUEST, "Malformed RDF-XML", ex);
		}

	}

	private CompoundObject create(HttpServletRequest request, Model container,
			String uri, CompoundObject object, HttpServletResponse response) {
		// Assign the CO's URI.
		if (uri == null) {
			String newUri = gen.generateCompoundObjectURI();
			object.assignURI(newUri);
		}
		container.addModel(object);
		
        // Set the Location and status code if we have a response object.
        if (response != null) {
            response.setStatus(uri == null ? HttpServletResponse.SC_CREATED : 
                HttpServletResponse.SC_OK);
            if (uri == null) {
                response.setHeader("Location", object.getContextURI().toString());
            }
        }
        return object;
	}

	/**
	 * 
	 */
}
