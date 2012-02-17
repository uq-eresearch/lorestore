package net.metadata.openannotation.lorestore.model.rdf2go;


import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DCTERMS_CREATED;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DCTERMS_MODIFIED;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DC_CREATOR;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_PRIVATE;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USER;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.NamedGraph;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.util.ModelUtils;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public abstract class NamedGraphImpl implements NamedGraph {

	protected Model model;
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	private static Calendar calendar;
	
	public NamedGraphImpl(Model model) {
		try {
			SailRepository sailRepository = new SailRepository(new MemoryStore());
			sailRepository.initialize();
			RepositoryModel internalModel = new RepositoryModel(sailRepository);
			internalModel.open();
			ModelUtils.copy(model, internalModel);
			this.model = internalModel;
			calendar = Calendar.getInstance();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Model getModel() {
		model.commit();
		return model;
	}

	public String getModelAsRDFXML() throws LoreStoreException {
		return model.serialize(Syntax.RdfXml);
	}

	
	protected void updateSubjectURI(ClosableIterator<Statement> it, URI newURI) {
		while (it.hasNext()) {
			Statement s = it.next();

			model.addStatement(newURI, s.getPredicate(), s.getObject());
			model.removeStatement(s);
		}
		it.close();
		model.commit();
	}

	public String getURL() throws LoreStoreException {
		return findObject().toString();
	}
	
	public String getCreator() throws LoreStoreException {
		Node c = lookupNode(model.createURI(DC_CREATOR));
		return (c != null? c.toString(): null);
	}

	public Node getCreatedDate() throws LoreStoreException {
		return lookupNode(model.createURI(DCTERMS_CREATED));
	}

	public Node getModifiedDate() throws LoreStoreException {
		return lookupNode(model.createURI(DCTERMS_MODIFIED));
	}

	public Node getUser() throws LoreStoreException {
		return lookupNode(model.createURI(LORESTORE_USER));
	}

	public Node lookupNode(URI uri) throws LoreStoreException {
		Resource findObj = findObject();
		ClosableIterator<Statement> it = model.findStatements(findObj,
				uri, Variable.ANY);
		return lookupOneObject(it);
	}
	
	public boolean isPrivate() {
		return model.contains(Variable.ANY, model.createURI(LORESTORE_PRIVATE), Variable.ANY);
	}
	
	protected Resource findObject() throws LoreStoreException {
		return null;
	}
	
	public void setDate(Date date, String datePred) throws LoreStoreException {
		Resource obj = findObject();
		URI datePredURI = model.createURI(datePred);

		ClosableIterator<Statement> dateStatements = model.findStatements(obj, datePredURI, Variable.ANY);
		model.removeAll(dateStatements);
		if (date != null){
			//Format date string to look like this: 2012-02-17T11:54:12+10:00
			int tz = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 60000;
			String dateString = df.format(date) + String.format("%+03d:%02d", (tz / 60), (tz % 60));
			model.addStatement(obj, datePredURI,
					model.createDatatypeLiteral(dateString, model.createURI("http://purl.org/dc/terms/W3CDTF")));
		} 
		model.commit();
	}
	public void setUser(String newUser) throws LoreStoreException {
		Resource obj = findObject();
		URI lorestoreUserPred = model.createURI(LORESTORE_USER);

		ClosableIterator<Statement> userStatements = model.findStatements(
				obj, lorestoreUserPred, Variable.ANY);
		model.removeAll(userStatements);
		if (newUser != null){
			model.addStatement(obj, lorestoreUserPred,
					model.createPlainLiteral(newUser));
		} 
		model.commit();
	}

	private Node lookupOneObject(ClosableIterator<Statement> it)
			throws LoreStoreException {
		Statement s = lookupOneStatement(it);

		return s == null ? null : s.getObject();
	}

	private Statement lookupOneStatement(ClosableIterator<Statement> it)
			throws LoreStoreException {
		Statement s = null;
		if (it.hasNext()) {
			s = it.next();
		}
		if (s == null) {
			return null;
		}
		if (it.hasNext()) {
			throw new LoreStoreException("Multiple results found");
		}
		it.close();
		return s;
	}

	public String getOwnerId() {
		try {
			Node user = getUser();
			if (user != null) {
				return user.toString();
			}
		} catch (LoreStoreException e) {

		}
		return null;
	}
	
	
	/**
	 * Close the internal model resources used by this compound object
	 */
	public void close() {
		if (model != null)
			model.close();
	}
}
