package net.metadata.openannotation.lorestore.access;

import net.metadata.openannotation.lorestore.model.NamedGraph;

import org.ontoware.rdf2go.model.Model;


/**

 * AccessPolicy implements fine-grained access control rules on
 * requests. A typical implementation will decide on the
 * basis of the users' identity, authorities and other attributes, together with
 * details of the target object(s).
 */
public interface LoreStoreAccessPolicy {

	/**
	 * Called for GET requests after fetching the object(s) from the triple
	 * store and preparing the result container. An implementation may throw an
	 * unchecked exception or modify the contents of the container.
	 * 
	 * @param obj
	 *            The object requested to read
	 */
	void checkRead(NamedGraph obj);

	/**
	 * Called for PUT and POST requests after preparing the result container and
	 * prior to committing. An implementation may throw an unchecked exception.
	 * (Modifying the result container is neither necessary or advisable. It
	 * will only change the result, not the triples committed to the triple
	 * store.)
	 * 
	 * @param res
	 *            the result container containing the triples to be returned in
	 *            the response.
	 */
	void checkCreate(Model res);

	/**
	 * Called for PUT requests to check that the requestor may update an object.
	 * An implementation may throw an unchecked exception.
	 * 
	 * @param obj
	 *            the original version of the object as fetched from the triple
	 *            store.
	 */
	void checkUpdate(NamedGraph obj);

	/**
	 * Called for DELETE requests prior to committing. An implementation may
	 * throw an unchecked exception.
	 * 
	 * @param obj
	 *            the original version of the object as fetched from the triple
	 *            store.
	 */
	void checkDelete(NamedGraph obj);

	/**
	 * Called for administrative requests to check that the request has the
	 * rights required for a given admin request.
	 * 
	 * @param request
	 *            the specific request.
	 */
	void checkAdmin();

}
