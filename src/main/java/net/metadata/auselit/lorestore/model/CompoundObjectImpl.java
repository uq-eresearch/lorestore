package net.metadata.auselit.lorestore.model;

import static net.metadata.auselit.lorestore.common.OREConstants.AGGREGATION;
import static net.metadata.auselit.lorestore.common.OREConstants.AUSELIT_USER;
import static net.metadata.auselit.lorestore.common.OREConstants.DCTERMS_CREATED;
import static net.metadata.auselit.lorestore.common.OREConstants.DCTERMS_MODIFIED;
import static net.metadata.auselit.lorestore.common.OREConstants.DC_CREATOR;
import static net.metadata.auselit.lorestore.common.OREConstants.ORE_AGGREGATION_CLASS;
import static net.metadata.auselit.lorestore.common.OREConstants.ORE_DESCRIBES_PROPERTY;
import static net.metadata.auselit.lorestore.common.OREConstants.RDF_TYPE_PROPERTY;

import javax.xml.datatype.XMLGregorianCalendar;

import net.metadata.auselit.lorestore.exceptions.OREException;

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

public class CompoundObjectImpl implements CompoundObject {

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
		model.setAutocommit(false);
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
				RDF_TYPE_PROPERTY, ORE_AGGREGATION_CLASS, ORE_DESCRIBES_PROPERTY));

		for (QueryRow row : resultTable) {
			return row.getValue("rem").asResource();
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
		model.addStatement(findResourceMap, model.createURI(AUSELIT_USER),
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

	public void setCreator(String userName) {
		// TODO Auto-generated method stub
		
	}

	public String getOwnerId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOwnerId(String ownerURI) {
		// TODO Auto-generated method stub
		
	}

	public XMLGregorianCalendar getCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDatestamp(String propName, XMLGregorianCalendar timestamp) {
		// TODO Auto-generated method stub
		
	}

	public XMLGregorianCalendar getModified() {
		// TODO Auto-generated method stub
		return null;
	}

	public XMLGregorianCalendar getDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
