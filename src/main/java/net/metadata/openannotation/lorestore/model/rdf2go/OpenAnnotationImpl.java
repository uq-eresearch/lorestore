package net.metadata.openannotation.lorestore.model.rdf2go;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DCTERMS_CREATOR;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OA_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_TARGET_PROPERTY;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OA_TARGET_PROPERTY;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.NamedGraph;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.util.ModelUtils;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

public class OpenAnnotationImpl extends NamedGraphImpl implements NamedGraph {

    
    public OpenAnnotationImpl(Model model) {
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

    public Resource findValidObject(Model schema) throws LoreStoreException {
        RepositoryModel tempModel = null;
        try {
            // support inferencing
            SailRepository sailRepository = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
            sailRepository.initialize();
            tempModel = new RepositoryModel(sailRepository);
            tempModel.open();
            // combine schema and internalModel to allow for type validation
            ModelUtils.union(model, schema, tempModel);
            // bare minimum validation: annotation must be of type Annotation and have at least one target
            QueryResultTable resultTable = 
                tempModel.sparqlSelect(String.format(
                    "select ?anno WHERE {{?anno a <%1$s> . ?anno <%2$s> ?target} UNION {?anno a <%3$s> . ?anno <%4$s> ?target}}",
                     OAC_ANNOTATION_CLASS, 
                     OAC_TARGET_PROPERTY,
                     OA_ANNOTATION_CLASS,
                     OA_TARGET_PROPERTY));
            Resource res = null;
            for (QueryRow row : resultTable) {
                res = row.getValue("anno").asResource();
            }
            if (res != null) {
                return res;
            }
        } catch (RepositoryException e){
            LOG.debug(e.getMessage());
        } finally {
            if (tempModel != null){
                tempModel.close();
            }
        }
        throw new LoreStoreException("Invalid Annotation");
    }
    
    protected Resource findObject() throws LoreStoreException {
        // we don't use type to find the object because it may be a subclass and the internal model does not contain the schema
        QueryResultTable resultTable = model.sparqlSelect(String.format(
            "select ?anno WHERE {{?anno <%1$s> ?target} UNION {?anno <%2$s> ?target}}", 
            OAC_TARGET_PROPERTY, 
            OA_TARGET_PROPERTY));
        Resource res = null;
        for (QueryRow row : resultTable) {
            res = row.getValue("anno").asResource();
        }
        if (res != null) {
            return res;
        }
        
        throw new LoreStoreException("Invalid Annotation");
    }

}
