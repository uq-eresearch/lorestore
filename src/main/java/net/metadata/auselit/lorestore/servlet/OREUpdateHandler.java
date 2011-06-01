package net.metadata.auselit.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.exceptions.OREException;
import au.edu.diasb.chico.mvc.RequestFailureException;

public interface OREUpdateHandler {

	/**
	 * Handle POST requests; posting of new Compound Objects
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

	public OREResponse put(String oreId, InputStream inputRDF)
			throws RequestFailureException, IOException, OREException,
			InterruptedException;

	public void bulkImport(InputStream body, String fileName) throws Exception;

	/**
	 * <b>Danger:</b> Clears all data from the database
	 * 
	 * @throws InterruptedException
	 */
	public void wipeDatabase() throws InterruptedException;

}