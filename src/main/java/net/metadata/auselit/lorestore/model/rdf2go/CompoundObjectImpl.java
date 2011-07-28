package net.metadata.auselit.lorestore.model.rdf2go;

import static net.metadata.auselit.lorestore.common.OREConstants.AGGREGATION;
import static net.metadata.auselit.lorestore.common.OREConstants.DCTERMS_CREATED;
import static net.metadata.auselit.lorestore.common.OREConstants.DCTERMS_MODIFIED;
import static net.metadata.auselit.lorestore.common.OREConstants.DC_CREATOR;
import static net.metadata.auselit.lorestore.common.OREConstants.LORESTORE_LOCKED;
import static net.metadata.auselit.lorestore.common.OREConstants.LORESTORE_PRIVATE;
import static net.metadata.auselit.lorestore.common.OREConstants.LORESTORE_USER;
import static net.metadata.auselit.lorestore.common.OREConstants.ORE_AGGREGATION_CLASS;
import static net.metadata.auselit.lorestore.common.OREConstants.ORE_DESCRIBES_PROPERTY;
import static net.metadata.auselit.lorestore.common.OREConstants.RDF_TYPE_PROPERTY;
import net.metadata.auselit.lorestore.exceptions.OREException;
import net.metadata.auselit.lorestore.model.CompoundObject;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
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

public class CompoundObjectImpl implements CompoundObject {

	private Model model;

	public CompoundObjectImpl(Model model) {
		try {
			SailRepository sailRepository = new SailRepository(new MemoryStore());
			sailRepository.initialize();
			
			RepositoryModel internalModel = new RepositoryModel(sailRepository);
			internalModel.open();
			ModelUtils.copy(model, internalModel);
			this.model = internalModel;
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Model getModel() {
		model.commit();
		return model;
	}

	public String getModelAsRDFXML() throws OREException {
		return model.serialize(Syntax.RdfXml);
	}

	public String getResourceMapURL() throws OREException {
		return findResourceMap().toString();
	}

	/**
	 * Assigns a new URI to the embedded model, updating the URI of the
	 * ResourceMap and the associated Aggregation
	 * 
	 * @param newUriString
	 * @throws OREException
	 */
	public void assignURI(String newUriString) throws OREException {
		Resource oldUri = findResourceMap();
		URI newURI = model.createURI(newUriString);
		ClosableIterator<Statement> resourceMapStmts = model.findStatements(
				oldUri, Variable.ANY, Variable.ANY);
		updateSubjectURI(resourceMapStmts, newURI);
		updateAggregationURI(newURI);
		model.commit();
	}

	private void updateAggregationURI(Resource resourceMap) throws OREException {
		URI newAggregationURI = model.createURI(resourceMap.toString()
				+ AGGREGATION);

		// Find all the aggregations referenced by this ResourceMap
		ClosableIterator<Statement> aggregations = model.findStatements(
				resourceMap, model.createURI(ORE_DESCRIBES_PROPERTY),
				Variable.ANY);

		while (aggregations.hasNext()) {
			Statement s = aggregations.next();
			URI aggregationURI = s.getObject().asURI();

			model.addStatement(s.getSubject(), s.getPredicate(),
					newAggregationURI);
			model.removeStatement(s);

			// Find all the statements referenced by this Aggregation
			ClosableIterator<Statement> aggregationStmts = model
					.findStatements(aggregationURI, Variable.ANY, Variable.ANY);
			updateSubjectURI(aggregationStmts, newAggregationURI);
		}
	}

	private void updateSubjectURI(ClosableIterator<Statement> it, URI newURI) {
		while (it.hasNext()) {
			Statement s = it.next();

			model.addStatement(newURI, s.getPredicate(), s.getObject());
			model.removeStatement(s);
		}
		it.close();
	}

	private Resource findResourceMap() throws OREException {
		QueryResultTable resultTable = model.sparqlSelect(String.format(
				"select ?rem WHERE {?aggre <%1$s> <%2$s>. ?rem <%3$s> ?aggre}",
				RDF_TYPE_PROPERTY, ORE_AGGREGATION_CLASS,
				ORE_DESCRIBES_PROPERTY));

		Resource res = null;
		for (QueryRow row : resultTable) {
			res = row.getValue("rem").asResource();
		}
		if (res != null) {
			return res;
		}
		throw new OREException("Invalid CompoundObject, no ResourceMap found");
	}

	public String getCreator() throws OREException {
		return lookupResourceMapNode(model.createURI(DC_CREATOR)).toString();
	}

	public Node getCreatedDate() throws OREException {
		return lookupResourceMapNode(model.createURI(DCTERMS_CREATED));
	}

	public Node getModifiedDate() throws OREException {
		return lookupResourceMapNode(model.createURI(DCTERMS_MODIFIED));
	}

	public Node getUser() throws OREException {
		return lookupResourceMapNode(model.createURI(LORESTORE_USER));
	}

	public Node lookupResourceMapNode(URI uri) throws OREException {
		Resource findResourceMap = findResourceMap();
		ClosableIterator<Statement> it = model.findStatements(findResourceMap,
				uri, Variable.ANY);
		return lookupOneObject(it);
	}
	
	public boolean isPrivate() {
		return model.contains(Variable.ANY, model.createURI(LORESTORE_PRIVATE), Variable.ANY);
	}
	
	public boolean isLocked() {
		return model.contains(Variable.ANY, model.createURI(LORESTORE_LOCKED), Variable.ANY);
	}

	public void setUser(String newUser) throws OREException {
		Resource resourceMap = findResourceMap();
		URI lorestoreUserPred = model.createURI(LORESTORE_USER);

		ClosableIterator<Statement> userStatements = model.findStatements(
				resourceMap, lorestoreUserPred, Variable.ANY);
		model.removeAll(userStatements);
		if (newUser != null){
			model.addStatement(resourceMap, lorestoreUserPred,
					model.createPlainLiteral(newUser));
		} 
		model.commit();
	}

	private Node lookupOneObject(ClosableIterator<Statement> it)
			throws OREException {
		Statement s = lookupOneStatement(it);

		return s == null ? null : s.getObject();
	}

	private Statement lookupOneStatement(ClosableIterator<Statement> it)
			throws OREException {
		Statement s = null;
		if (it.hasNext()) {
			s = it.next();
		}
		if (s == null) {
			return null;
		}
		if (it.hasNext()) {
			throw new OREException("Multiple results found");
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
		} catch (OREException e) {

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
