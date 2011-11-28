package net.metadata.auselit.lorestore.model.rdf2go;

import static net.metadata.auselit.lorestore.common.LoreStoreConstants.DCTERMS_CREATOR;
import static net.metadata.auselit.lorestore.common.LoreStoreConstants.OAC_ANNOTATION_CLASS;
import static net.metadata.auselit.lorestore.common.LoreStoreConstants.OAC_REPLY_CLASS;
import static net.metadata.auselit.lorestore.common.LoreStoreConstants.OAC_DATA_ANNOTATION_CLASS;
import static net.metadata.auselit.lorestore.common.LoreStoreConstants.RDF_TYPE_PROPERTY;
import static net.metadata.auselit.lorestore.common.LoreStoreConstants.OAC_TARGET_PROPERTY;

import net.metadata.auselit.lorestore.exceptions.LoreStoreException;
import net.metadata.auselit.lorestore.model.NamedGraph;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;

public class OACAnnotationImpl extends NamedGraphImpl implements NamedGraph {

	
	public OACAnnotationImpl(Model model) {
		super(model);
	}

	public String getCreator() throws LoreStoreException {
		Node c = lookupNode(model.createURI(DCTERMS_CREATOR));
		return (c != null? c.toString(): null);
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
		
		model.commit();
	}
	protected Resource findObject() throws LoreStoreException {
		// bare minimum validation: annotation must be one of the OAC types and have at least one target
		QueryResultTable resultTable = model.sparqlSelect(String.format(
				"select ?anno WHERE {{?anno a <%1$s>} UNION {?anno a <%2$s>} UNION {?anno a <%3$s>}. ?anno <%4$s> ?target}",
				 OAC_ANNOTATION_CLASS, OAC_REPLY_CLASS, OAC_DATA_ANNOTATION_CLASS, OAC_TARGET_PROPERTY));

		Resource res = null;
		for (QueryRow row : resultTable) {
			res = row.getValue("anno").asResource();
		}
		if (res != null) {
			return res;
		}
		throw new LoreStoreException("Invalid OAC Annotation");
	}

}
