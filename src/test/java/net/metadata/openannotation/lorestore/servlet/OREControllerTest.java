package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import net.metadata.openannotation.lorestore.common.LoreStoreConstants;
import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import net.metadata.openannotation.lorestore.servlet.OREResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.impl.TriplePatternImpl;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.springframework.security.access.AccessDeniedException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import au.edu.diasb.chico.mvc.RequestFailureException;

@RunWith(JUnit4.class)
public class OREControllerTest extends OREControllerTestsBase {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test(expected = NotFoundException.class)
	public void deleteUnknown() throws Exception {
		controller.delete("13532");

	}

	@Test
	public void postEmpty() throws Exception {
		InputStream in = new ByteArrayInputStream("".getBytes());
		try {
			controller.post(in, "application/rdf+xml");
			fail("should have thrown exception");
		} catch (RequestFailureException e) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, e.getStatus());
		}
	}

	@Test(expected = RequestFailureException.class)
	public void postBadCompoundObjectBrokenXML() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_ORE_BROKEN_XML.getBytes());
		controller.post(in, "application/rdf+xml");
		// expect 400 - bad request
	}

	@Test(expected = LoreStoreException.class)
	public void postBadCompoundObjectNoResourceMap() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_ORE_NO_RESOURCEMAP.getBytes());
		controller.post(in, "application/rdf+xml");
		// expect 400 - bad request
	}

	@Test
	public void postCompoundObjectAndGet() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = (OREResponse) controller.post(in, "application/rdf+xml");

		assertNotNull(response.getLocationHeader());
		assertNotNull(response.getReturnStatus());
		assertEquals(201, response.getReturnStatus());

		// assertEquals(16, cf.size());
	}

	@Test
	public void postGetDeleteGet() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		OREResponse oreResponse2 = (OREResponse) controller.get(createdId);
		assertNotNull(oreResponse2);

		assertEquals("ore", oreResponse2.getViewName());
		Map<String, Object> model2 = oreResponse2.getModel();
		Model rdf2 = (Model) model2.get(OREResponse.RESPONSE_RDF_KEY);
		assertNotNull(rdf2);

		controller.delete(createdId);

		exception.expect(NotFoundException.class);
		controller.get(createdId);

		// check responses of get and delete pass (are 200), and then the second
		// get should 404

	}

	/**
	 * Can only PUT to a url with an existing URL
	 * @throws Exception
	 */
	@Test(expected = LoreStoreException.class)
	public void putBlankURL() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		controller.put("", in, "application/rdf+xml");
	}


	/**
	 * Can only PUT to a url with an existing URL
	 * @throws Exception
	 */
	@Test(expected = LoreStoreException.class)
	public void putNonexistent() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		controller.put("http://example.com/blergh", in, "application/rdf+xml");
	}

	@Test
	public void postThenPut() throws Exception {
		String id = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(id, in, "application/rdf+xml");

	}

	/**
	 * Refers to query only accepts valid URLs
	 * @throws Exception
	 */
	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToEmpty() throws Exception {
		controller.refersToQuery("");
	}

	/**
	 * Refers to query only accepts valid URLs
	 * @throws Exception
	 */
	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToInvalidURL() throws Exception {
		controller.refersToQuery("zxcv");
	}

	@Test
	public void queryRefersToNonExistentURL() throws Exception {
		String body = (String) controller.refersToQuery("http://omad.net/").getModel().get("sparqlxml");
		assertNotNull(body);

		Document document = parseXmlToDocument(body);
		NodeList results = document.getElementsByTagName("result");
		assertEquals(0, results.getLength());
	}

	@Test
	public void queryRefersToExistingURL() throws Exception {
		// save an object
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		// query for it
		String body = (String) controller.refersToQuery("http://omad.net/").getModel().get("sparqlxml");

		// check the results include the object
		assertNotNull(body);
		assertTrue(body.contains(createdId));

		Document document = parseXmlToDocument(body);
		assertEquals(1, document.getElementsByTagName("result").getLength());

		assertEquals("Damien Ayers",
				xPath.evaluate("//binding[@name='a']/literal", document));
	}

	@Test
	public void keywordSearch() throws Exception {
		String body = (String) controller.searchQuery("", "", "test", false, true).getModel().get("sparqlxml");

		assertNotNull(body);
	}

	@Test(expected = AccessDeniedException.class)
	public void postWithNoAuth() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in, "application/rdf+xml");
	}

	@Test
	public void postWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in, "application/rdf+xml");
	}

	@Test(expected = AccessDeniedException.class)
	public void postWithNoAuth2() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in, "application/rdf+xml");
	}

	@Test(expected = LoreStoreException.class)
	public void putToNonExistant() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.put("http://nonexistant.example.com/ore/id", in, "application/rdf+xml");
	}

	/**
	 * Test creating a compound object, ignoring any authorisations.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putSuccessful() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(createdId, in, "application/rdf+xml");

		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(createdId, in, "application/rdf+xml");
	}

	/**
	 * A user without valid authorisation cannot create a compound object.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putWithNoAuth() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		exception.expect(AccessDeniedException.class);
		exception
				.expectMessage("Authentication problem: request has no authentication object");
		authController.put(createdId, in, "application/rdf+xml");
	}

	/**
	 * A user with valid authorisation can create a compound object.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		String recordId = findUIDFromResponse(response);

		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		response = (OREResponse) authController.put(recordId, in, "application/rdf+xml");
	}

	/**
	 * Make sure that a user who doesn't own a compound object cannot overwrite one.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithChangedAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		String recordId = findUIDFromResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		exception.expect(AccessDeniedException.class);
		exception.expectMessage("You do not own this object");
		response = (OREResponse) authController.put(recordId, in, "application/rdf+xml");
	}
	

	/**
	 * Make sure that an admin can overwrite a compound object they don't own.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutAsAdmin() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		String recordId = findUIDFromResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE", "ROLE_ADMIN" });
		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		response = (OREResponse) authController.put(recordId, in, "application/rdf+xml");
	}

	/**
	 * Make sure that an admin can delete a compound object they don't own.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenDeleteAsAdmin() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		String recordId = findUIDFromResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE", "ROLE_ADMIN" });
		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		authController.delete(recordId);
	}

	/**
	 * Make sure that the recorded owner of a compound object cannot be removed.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithRemovedAuth() throws Exception {
		String userUri = "http://example.com/user/bob";
		updateAuthenticationContext("bob", userUri, new String[] { "ROLE_USER",
				"ROLE_ORE" });
		String simpleOreExample = CommonTestRecords.SIMPLE_ORE_EXAMPLE;
		InputStream in = new ByteArrayInputStream(simpleOreExample.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		checkUserInModel(response, userUri);

		String recordId = findUIDFromResponse(response);
		in = new ByteArrayInputStream(simpleOreExample.getBytes());
		response = (OREResponse) authController.put(recordId, in, "application/rdf+xml");

		checkUserInModel(response, userUri);
	}

	/**
	 * Make sure that the user credentials stored in the posted RDF are ignored,
	 * and the actual logged in user is what is respected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postWithNonMatchingUserUri() throws Exception {
		String userUri = "http://example.com/user/bob";
		updateAuthenticationContext("bob", userUri, new String[] { "ROLE_USER",
				"ROLE_ORE" });
		String simpleOreExample = CommonTestRecords.SIMPLE_ORE_EXAMPLE_WITH_OWNER;
		InputStream in = new ByteArrayInputStream(simpleOreExample.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		checkUserInModel(response, userUri);

		String recordId = findUIDFromResponse(response);
		in = new ByteArrayInputStream(simpleOreExample.getBytes());
		response = (OREResponse) authController.put(recordId, in, "application/rdf+xml");

		checkUserInModel(response, userUri);
	}

	/**
	 * Make sure that a locked object cannot be updated
	 * @throws Exception
	 */
	@Test
	public void postThenPutLocked() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_LOCKED.getBytes());
		OREResponse response = (OREResponse) authController.post(in, "application/rdf+xml");

		String recordId = findUIDFromResponse(response);
		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_LOCKED.getBytes());
		
		exception.expect(AccessDeniedException.class);
		exception.expectMessage("Object is locked, must be administrator to modify");
		authController.put(recordId, in, "application/rdf+xml");
	}
	
	//
	// Private Methods
	//

	private Document parseXmlToDocument(String xml) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}

	private void checkUserInModel(OREResponse oreResponse, String userUri) {
		Model model = (Model) oreResponse.getModel().get(
				OREResponse.RESPONSE_RDF_KEY);

		URI userPred = model.createURI(LoreStoreConstants.LORESTORE_USER);
		assertEquals(1, model.countStatements(new TriplePatternImpl(
				Variable.ANY, userPred, Variable.ANY)));

		assertTrue(model.contains(Variable.ANY, userPred, userUri));
	}

	private String saveRecordToStore(String recordXML) throws Exception {
		InputStream in = new ByteArrayInputStream(recordXML.getBytes());
		OREResponse response = (OREResponse) controller.post(in, "application/rdf+xml");

		String id = findUIDFromResponse(response);

		return id;
	}

	private String findUIDFromResponse(OREResponse response) {
		// assertTrue(redirect.startsWith("redirect:"));
		String redirect = response.getLocationHeader();
		String createdId = redirect.substring(redirect.lastIndexOf("/") + 1);
		return createdId;
	}
}
