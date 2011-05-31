package net.metadata.auselit.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import net.metadata.auselit.lorestore.common.OREConstants;
import net.metadata.auselit.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.exceptions.OREException;

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
			controller.post(in);
			fail("should have thrown exception");
		} catch (RequestFailureException e) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, e.getStatus());
		}
	}

	@Test(expected = RequestFailureException.class)
	public void postBadCompoundObject() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_ORE_BROKEN_XML.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test(expected = OREException.class)
	public void postBadCompoundObject2() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_ORE_NO_RESOURCEMAP.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test
	public void postCompoundObjectAndGet() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = controller.post(in);

		assertNotNull(response.getLocationHeader());
		assertNotNull(response.getReturnStatus());
		assertEquals(201, response.getReturnStatus());

		// assertEquals(16, cf.size());
	}

	@Test
	public void postGetDeleteGet() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		OREResponse oreResponse2 = controller.get(createdId);
		assertNotNull(oreResponse2);

		assertEquals("ore", oreResponse2.getViewName());
		Map<String, Object> model2 = oreResponse2.getModel();
		Model rdf2 = (Model) model2.get(OREResponse.RESPONSE_RDF_KEY);
		assertNotNull(rdf2);

		controller.delete(createdId);

		try {
			controller.get(createdId);
			fail("Object should have been deleted, method should have thrown exception");
		} catch (NotFoundException e) {
			// Expected
		}

		// check responses of get and delete pass (are 200), and then the second
		// get should 404

	}

	@Test(expected = OREException.class)
	public void putNonexistent() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		controller.put("", in);
	}

	@Test
	public void postThenPut() throws Exception {
		String id = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(id, in);

	}

	private String saveRecordToStore(String recordXML) throws Exception {
		InputStream in = new ByteArrayInputStream(recordXML.getBytes());
		OREResponse response = controller.post(in);

		String id = findUIDFromResponse(response);

		return id;

	}

	private String findUIDFromResponse(OREResponse response) {
		// assertTrue(redirect.startsWith("redirect:"));
		String redirect = response.getLocationHeader();
		String createdId = redirect.substring(redirect.lastIndexOf("/") + 1);
		return createdId;
	}

	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToEmpty() throws Exception {
		controller.refersToQuery("");
	}

	// @Test(expected = InvalidQueryParametersException.class)
	// public void queryRefersToInvalidURL() throws Exception {
	// controller.refersToQuery("zxcv");
	// }

	@Test
	public void queryRefersToNonExistentURL() throws Exception {
		String body = controller.refersToQuery("http://omad.net/").getBody();
		assertNotNull(body);

		Document document = parseXmlToDocument(body);
		NodeList results = document.getElementsByTagName("result");
		assertEquals(0, results.getLength());
	}

	private Document parseXmlToDocument(String xml) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}

	@Test
	public void queryRefersToExistingURL() throws Exception {
		// save an object
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		// query for it
		String body = controller.refersToQuery("http://omad.net/").getBody();

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
		String body = controller.keywordSearch("test").getBody();

		assertNotNull(body);
	}

	@Test(expected = AccessDeniedException.class)
	public void postWithNoAuth() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in);
	}

	@Test
	public void postWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in);
	}

	@Test(expected = AccessDeniedException.class)
	public void postWithNoAuth2() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.post(in);
	}

	@Test(expected = OREException.class)
	public void putToNonExistant() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		authController.put("http://nonexistant.example.com/ore/id", in);
	}

	@Test
	public void putSuccessful() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(createdId, in);

		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		controller.put(createdId, in);
	}

	@Test
	public void putWithNoAuth() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.SIMPLE_ORE_EXAMPLE);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		exception.expect(AccessDeniedException.class);
		exception
				.expectMessage("Authentication problem: request has no authentication object");
		authController.put(createdId, in);
	}

	@Test
	public void postThenPutWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = authController.post(in);

		String recordId = findUIDFromResponse(response);

		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		response = authController.put(recordId, in);
	}

	@Test
	public void postThenPutWithChangedAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		OREResponse response = authController.post(in);

		String recordId = findUIDFromResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());

		exception.expect(AccessDeniedException.class);
		exception.expectMessage("You do not own this object");
		response = authController.put(recordId, in);
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
		OREResponse response = authController.post(in);
		
		checkUserInModel(response, userUri);


		String recordId = findUIDFromResponse(response);
		in = new ByteArrayInputStream(simpleOreExample.getBytes());
		response = authController.put(recordId, in);
		

//		checkUserInModel(response, userUri);

	}

	private void checkUserInModel(OREResponse oreResponse, String userUri) {
		Model model = (Model) oreResponse.getModel().get(
				OREResponse.RESPONSE_RDF_KEY);

		URI userPred = model.createURI(OREConstants.LORESTORE_USER);
		assertEquals(1, model.countStatements(new TriplePatternImpl(
				Variable.ANY, userPred, Variable.ANY)));

		assertTrue(model.contains(Variable.ANY, userPred, userUri));
	}
	
	/**
	 * Make sure that the user credentials stored in the posted RDF are ignored,
	 * and the actual logged in user is what is respected. 
	 * @throws Exception
	 */
	@Test
	public void postWithNonMatchingUserUri() throws Exception {
		String userUri = "http://example.com/user/bob";
		updateAuthenticationContext("bob", userUri, new String[] { "ROLE_USER",
				"ROLE_ORE" });
		String simpleOreExample = CommonTestRecords.SIMPLE_ORE_EXAMPLE_WITH_OWNER;
		InputStream in = new ByteArrayInputStream(simpleOreExample.getBytes());
		OREResponse response = authController.post(in);
		
		checkUserInModel(response, userUri);


		String recordId = findUIDFromResponse(response);
		in = new ByteArrayInputStream(simpleOreExample.getBytes());
		response = authController.put(recordId, in);
		

//		checkUserInModel(response, userUri);

	}
}
