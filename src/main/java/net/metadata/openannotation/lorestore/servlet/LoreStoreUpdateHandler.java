package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import au.edu.diasb.chico.mvc.RequestFailureException;

public interface LoreStoreUpdateHandler {

	/**
	 * Handle POST requests; Used for creating new Compound Objects and Annotations
	 * 
	 * @param request
	 *            the servlet request
	 * @return
	 * @throws IOException
	 * @throws RequestFailureException
	 * @throws LoreStoreException
	 * @throws InterruptedException
	 */
	public OREResponse post(InputStream inputRDF)
			throws RequestFailureException, IOException, LoreStoreException,
			InterruptedException;

	public void delete(String objId) throws NotFoundException,
			InterruptedException;

	/**
	 * Handle PUT requests; Used for updating existing compound objects and annotations
	 * 
	 * A compound object must already exist for the supplied objId, and the user
	 * must be authorised to update it.
	 * @param oreId 
	 * @param inputRDF
	 * @return
	 */
	public OREResponse put(String objId, InputStream inputRDF)
			throws RequestFailureException, IOException, LoreStoreException,
			InterruptedException;

	/**
	 * Import a group of resources. Must be supplied as RDF named graphs.
	 * 
	 * @param body
	 * @param fileName
	 * @throws Exception
	 */
	public void bulkImport(InputStream body, String fileName) throws Exception;

	/**
	 * <b>Danger:</b> Clears all data from the database
	 * 
	 * @throws InterruptedException
	 */
	public void wipeDatabase() throws InterruptedException;

}