package net.metadata.auselit.lorestore.servlet.rdf2go;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.metadata.auselit.lorestore.access.OREAccessPolicy;
import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.exceptions.OREException;
import net.metadata.auselit.lorestore.model.CompoundObject;
import net.metadata.auselit.lorestore.model.rdf2go.CompoundObjectImpl;
import net.metadata.auselit.lorestore.servlet.OREControllerConfig;
import net.metadata.auselit.lorestore.servlet.OREResponse;
import net.metadata.auselit.lorestore.servlet.OREUpdateHandler;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.rdf2go.RepositoryModelFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;

import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * The RDF2GoOREUpdateHandler class processes updates to compound object by
 * OREController
 * 
 * Based loosely on DannoUpdateHandler
 * 
 * @author uqdayers
 */
public class RDF2GoOREUpdateHandler implements OREUpdateHandler {

	protected final OREControllerConfig occ;
	private final TripleStoreConnectorFactory cf;
	private OREAccessPolicy ap;

	public RDF2GoOREUpdateHandler(OREControllerConfig occ) {
		this.occ = occ;
		this.cf = occ.getContainerFactory();
		this.ap = occ.getAccessPolicy();
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREUpdateHandler#post(java.io.InputStream)
	 */
	@Override
	public OREResponse post(InputStream inputRDF) throws RequestFailureException,
			IOException, OREException, InterruptedException {
		
		// TODO: probably should check after we've loaded the model,
		// but at this stage, the security check ignores it anyway
		ap.checkCreate(null);
		
		RepositoryModelFactory mf = new RepositoryModelFactory();

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
		model.close();
		compoundObject.assignURI(newUri.toString());

		// TODO: needs to do stuff like maintaining the created/modified dates,
		// and the creator
		String userURI = occ.getIdentityProvider().obtainUserURI();
		compoundObject.setUser(userURI);
		

		ModelSet ms = null;
		try {
			ms = cf.retrieveConnection();
			ms.addModel(compoundObject.getModel(), newUri);
			ms.commit();
		} finally {
			cf.release(ms);
		}
		OREResponse response = new OREResponse(compoundObject.getModel());
		response.setLocationHeaer(occ.getBaseUri() + uid);
		response.setReturnStatus(201);
		return response;
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREUpdateHandler#delete(java.lang.String)
	 */
	@Override
	public void delete(String oreId) throws NotFoundException,
			InterruptedException {
		ModelSet container = null;
		Model model = null;
		try {
			container = cf.retrieveConnection();
			URI objURI = container.createURI(occ.getBaseUri() + oreId);
			if (!container.containsModel(objURI)) {
				throw new NotFoundException("Cannot delete, object not found");
			}

			model = container.getModel(objURI);
			CompoundObject co = new CompoundObjectImpl(model);
			
			ap.checkDelete(co);
			
			container.removeModel(objURI);
			container.commit();
		} finally {
			if (model != null) {
				model.close();
			}
			cf.release(container);
		}
	}


	@Override
	public OREResponse put(String oreId, InputStream inputRDF)
			throws RequestFailureException, IOException, OREException,
			InterruptedException {
		ModelFactory mf = RDF2Go.getModelFactory();
		URI objURI = mf.createModel().createURI(occ.getBaseUri() + oreId);

		Model newModel = null;
		ModelSet container = cf.retrieveConnection();
		try {
			String oldUserUri = null;
			Model oldModel = container.getModel(objURI);
			try {
				oldUserUri = checkUserCanUpdate(oldModel);
			} finally {
				oldModel.close();
			}
			
			newModel = mf.createModel(objURI);
			newModel.open();

			
			try {
				newModel.readFrom(inputRDF, Syntax.RdfXml, occ.getBaseUri());
			} catch (ModelRuntimeException e) {
				newModel.close();
				throw new RequestFailureException(
						HttpServletResponse.SC_BAD_REQUEST, "Error reading RDF");
			}

			// TODO: needs to do stuff like maintaining the created/modified
			// dates,
			// and the creator
			CompoundObjectImpl compoundObject = new CompoundObjectImpl(newModel);
			newModel.close();
			compoundObject.setUser(oldUserUri);

			newModel = compoundObject.getModel();

			container.removeModel(objURI);
			container.addModel(newModel, objURI);
			container.commit();
			
			
		} finally {
			cf.release(container);
		}

		return new OREResponse(newModel);
	}

	private String checkUserCanUpdate(Model oldModel) throws OREException {
		if (oldModel == null || oldModel.isEmpty()) {
			if (oldModel != null)
				oldModel.close();
			throw new OREException("Cannot update non-existant object");
		}
		String oldUserUri = null;
		CompoundObjectImpl co = new CompoundObjectImpl(oldModel);
		try {
			oldUserUri = co.getOwnerId();
			ap.checkUpdate(co);
		} finally {
			co.close();
		}
		return oldUserUri;
	}

	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREUpdateHandler#bulkImport(java.io.InputStream, java.lang.String)
	 */
	@Override
	public void bulkImport(InputStream body, String fileName) throws Exception {
		RepositoryConnection con = null;
		ModelSet modelSet = null;
		try {
			modelSet = cf.retrieveConnection();
	
			Repository rep = (Repository) modelSet
					.getUnderlyingModelSetImplementation();
			con = rep.getConnection();
			con.setAutoCommit(false);
			RDFFormat rdfFormat = RDFFormat.forFileName(fileName);
			con.add(body, "", rdfFormat);
			con.commit();
		} finally {
			if (con != null)
				con.close();
			if (modelSet != null)
				cf.release(modelSet);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.metadata.auselit.lorestore.servlet.rdf2go.OREUpdateHandler#wipeDatabase()
	 */
	@Override
	public void wipeDatabase() throws InterruptedException {

		ModelSet modelSet = null;
		try {
			modelSet = cf.retrieveConnection();
			
			modelSet.removeAll();
			
			modelSet.commit();
			
		} finally {
			if (modelSet != null)
				cf.release(modelSet);
		}
	}
}
