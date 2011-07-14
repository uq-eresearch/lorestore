package net.metadata.auselit.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.exceptions.OREException;
import au.edu.diasb.chico.mvc.RequestFailureException;

public interface OREUpdateHandler {

	/**
	 * Handle POST requests; Used for creating new Compound Objects
	 * 
	 * @param request
	 *            the servlet request
	 * @return
	 * @throws IOException
	 * @throws RequestFailureException
	 * @throws OREException
	 * @throws InterruptedException
	 */
	public OREResponse post(InputStream inputRDF)
			throws RequestFailureException, IOException, OREException,
			InterruptedException;

	public void delete(String oreId) throws NotFoundException,
			InterruptedException;

	/**
	 * Handle PUT requests; Used for updating existing compound objects
	 * 
	 * A compound object must already exist for the supplied oreId, and the user
	 * must be authorised to update it.
	 * @param oreId 
	 * @param inputRDF
	 * @return
	 */
	public OREResponse put(String oreId, InputStream inputRDF)
			throws RequestFailureException, IOException, OREException,
			InterruptedException;

	/**
	 * Import a group of compound object resources. Must be supplied as RDF named graphs.
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