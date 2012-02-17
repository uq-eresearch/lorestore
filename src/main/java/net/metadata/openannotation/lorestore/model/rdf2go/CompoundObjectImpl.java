package net.metadata.openannotation.lorestore.model.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.AGGREGATION;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_LOCKED;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.ORE_AGGREGATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.ORE_DESCRIBES_PROPERTY;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.RDF_TYPE_PROPERTY;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.CompoundObject;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;

public class CompoundObjectImpl extends NamedGraphImpl implements CompoundObject {


	public CompoundObjectImpl(Model model) {
		super(model);
	}


	/**
	 * Assigns a new URI to the embedded model, updating the URI of the
	 * ResourceMap and the associated Aggregation
	 * 
	 * @param newUriString
	 * @throws LoreStoreException
	 */
	public void assignURI(String newUriString) throws LoreStoreException {
		Resource oldUri = findObject();
		URI newURI = model.createURI(newUriString);
		ClosableIterator<Statement> resourceMapStmts = model.findStatements(
				oldUri, Variable.ANY, Variable.ANY);
		updateSubjectURI(resourceMapStmts, newURI);
		updateAggregationURI(newURI);
		model.commit();
	}

	private void updateAggregationURI(Resource resourceMap) throws LoreStoreException {
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
		model.commit();
	}

	
	protected Resource findObject() throws LoreStoreException {
		Resource res = null;
		
		QueryResultTable resultTable = model.sparqlSelect(String.format(
				"select ?rem WHERE {?aggre <%1$s> <%2$s>. ?rem <%3$s> ?aggre}",
				RDF_TYPE_PROPERTY, ORE_AGGREGATION_CLASS,
				ORE_DESCRIBES_PROPERTY));

		for (QueryRow row : resultTable) {
			res = row.getValue("rem").asResource();
		}
		
		if (res != null) {
			return res;
		}
		
		throw new LoreStoreException("Invalid CompoundObject, no ResourceMap found");
	}

	
	public boolean isLocked() {
		return model.contains(Variable.ANY, model.createURI(LORESTORE_LOCKED), Variable.ANY);
	}

}
