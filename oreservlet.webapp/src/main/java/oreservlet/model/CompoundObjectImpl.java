package oreservlet.model;

import static oreservlet.common.OREConstants.*;
import oreservlet.exceptions.OREException;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;

public class CompoundObjectImpl {

	private Model model;

	public CompoundObjectImpl(Model model) {
		this.model = model;
	}

	public Model getModel() {
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
		model.setAutocommit(false);
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
		ClosableIterator<Statement> it = model.findStatements(Variable.ANY,
				model.createURI(RDF_TYPE_PROPERTY),
				model.createURI(ORE_RESOURCEMAP_CLASS));
		Statement s = lookupOneStatement(it);
		return s.getSubject();
	}

	public Node getCreator() throws OREException {
		return lookupResourceMapNode(model.createURI(DC_CREATOR));
	}

	public Node getCreatedDate() throws OREException {
		return lookupResourceMapNode(model
				.createURI(DCTERMS_CREATED));
	}

	public Node getModifiedDate() throws OREException {
		return lookupResourceMapNode(model
				.createURI(DCTERMS_MODIFIED));
	}

	public Node getUser() throws OREException {
		return lookupResourceMapNode(model.createURI(AUSELIT_USER));
	}

	public Node lookupResourceMapNode(URI uri) throws OREException {
		Resource findResourceMap = findResourceMap();

		ClosableIterator<Statement> it = model.findStatements(findResourceMap,
				uri, Variable.ANY);
		return lookupOneObject(it);
	}

	public void setUser(String newUser) throws OREException {
		Node user = getUser();
		if (user != null) {
			throw new OREException("User already set, not allowed to update");
		}
		Resource findResourceMap = findResourceMap();
		model.addStatement(findResourceMap,
				model.createURI(AUSELIT_USER),
				model.createPlainLiteral(newUser));
		model.commit();
	}

	private Node lookupOneObject(ClosableIterator<Statement> it)
			throws OREException {
		Statement s = lookupOneStatement(it);

		return s.getObject();
	}

	private Statement lookupOneStatement(ClosableIterator<Statement> it)
			throws OREException {
		Statement s = null;
		if (it.hasNext()) {
			s = it.next();
		}
		if (s == null) {
			throw new OREException("No result found");
		}
		if (it.hasNext()) {
			throw new OREException("Multiple results found");
		}
		it.close();
		return s;
	}

}
