package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.impl.TriplePatternImpl;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;

import au.edu.diasb.chico.mvc.RequestFailureException;

@RunWith(JUnit4.class)
public class OACControllerTest extends OACControllerTestsBase {

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
			controller.post(in);
			fail("should have thrown exception");
		} catch (RequestFailureException e) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, e.getStatus());
		}
	}

	@Test(expected = RequestFailureException.class)
	public void postBadCompoundObjectBrokenXML() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_OAC_BROKEN_XML.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test(expected = LoreStoreException.class)
	public void postBadAnnotation() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(
				CommonTestRecords.BAD_OAC_NO_ANNOTATION.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test
	public void postAnnotationAndGet() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		ModelAndView response = controller.post(in);
		assertNotNull(response);
		
	}

	@Test
	public void postGetDeleteGet() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.OAC_INLINE_BODY);

		ModelAndView oacResponse2 = (ModelAndView) controller.get(createdId);
		assertNotNull(oacResponse2);

		assertEquals("oac", oacResponse2.getViewName());
		Map<String, Object> model2 = oacResponse2.getModel();
		ModelSet rdf2 = (ModelSet) model2.get("annotations");
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
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		controller.put("", in);
	}


	/**
	 * Can only PUT to a url with an existing URL
	 * @throws Exception
	 */
	@Test(expected = LoreStoreException.class)
	public void putNonexistent() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		controller.put("http://example.com/blergh", in);
	}

	@Test
	public void postThenPut() throws Exception {
		String id = saveRecordToStore(CommonTestRecords.OAC_INLINE_BODY);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		controller.put(id, in);

	}

	/**
	 * Refers to (annotates) query only accepts valid URLs
	 * @throws Exception
	 */
	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToEmpty() throws Exception {
		controller.refersToQuery("");
	}

	/**
	 * Refers to (annotates) query only accepts valid URLs
	 * @throws Exception
	 */
	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToInvalidURL() throws Exception {
		controller.refersToQuery("zxcv");
	}

	@Test
	public void queryRefersToNonExistentURL() throws Exception {
		ModelSet annotations = (ModelSet) controller.refersToQuery("http://itee.uq.edu.au/~agerber/").getModel().get("annotations");
		assertNotNull(annotations);

		
		assertEquals(0, annotations.size());
	}

	@Test
	public void queryRefersToExistingURL() throws Exception {
		// save an object
		
		InputStream in = new ByteArrayInputStream(CommonTestRecords.OAC_INLINE_BODY.getBytes());
		OREResponse response = (OREResponse) controller.post(in);
		String createdId = response.getLocationHeader();
		
		// query for it
		ModelSet annotations = (ModelSet) controller.refersToQuery("http://itee.uq.edu.au/~agerber/").getModel().get("annotations");

		// check the results include the object
		assertTrue(annotations.containsModel(annotations.createURI(createdId)));
				
	}

	@Test
	public void keywordSearch() throws Exception {
		String body = (String) controller.searchQuery("", "", "test", false).getModel().get("sparqlxml");

		assertNotNull(body);
	}

	@Test(expected = AccessDeniedException.class)
	public void postWithNoAuth() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		authController.post(in);
	}

	@Test
	public void postWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		authController.post(in);
	}


	@Test(expected = LoreStoreException.class)
	public void putToNonExistant() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		authController.put("http://nonexistant.example.com/oac/id", in);
	}

	/**
	 * Test creating an annotation, ignoring any authorisations.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putSuccessful() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.OAC_INLINE_BODY);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		controller.put(createdId, in);

		in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		controller.put(createdId, in);
	}

	/**
	 * A user without valid authorisation cannot create an annotation.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putWithNoAuth() throws Exception {
		String createdId = saveRecordToStore(CommonTestRecords.OAC_INLINE_BODY);

		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		exception.expect(AccessDeniedException.class);
		exception
				.expectMessage("Authentication problem: request has no authentication object");
		authController.put(createdId, in);
	}

	/**
	 * A user with valid authorisation can create annotation.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		OREResponse response =  (OREResponse) authController.post(in);

		String recordId = findUIDFromOREResponse(response);

		in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		response = (OREResponse) authController.put(recordId, in);
	}

	/**
	 * Make sure that a user who doesn't own an annotation cannot overwrite one.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithChangedAuth() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		OREResponse response =  (OREResponse) authController.post(in);

		String recordId = findUIDFromOREResponse(response);

		updateAuthenticationContext("cindy", "http://example.com/user/cindy",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		exception.expect(AccessDeniedException.class);
		exception.expectMessage("You do not own this object");
		response = (OREResponse) authController.put(recordId, in);
	}
	

	/**
	 * Make sure that an admin can overwrite an annotation they don't own.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutAsAdmin() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		OREResponse response = (OREResponse) authController.post(in);

		String recordId = findUIDFromOREResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE", "ROLE_ADMIN" });
		in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		response = (OREResponse) authController.put(recordId, in);
	}

	/**
	 * Make sure that an admin can delete an annotation they don't own.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenDeleteAsAdmin() throws Exception {
		updateAuthenticationContext("bob", "http://example.com/user/bob",
				new String[] { "ROLE_USER", "ROLE_ORE" });
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());
		OREResponse response = (OREResponse) authController.post(in);

		String recordId = findUIDFromOREResponse(response);

		updateAuthenticationContext("james", "http://example.com/user/james",
				new String[] { "ROLE_USER", "ROLE_ORE", "ROLE_ADMIN" });
		in = new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes());

		authController.delete(recordId);
	}

	/**
	 * Make sure that the recorded owner of an annotation cannot be removed.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postThenPutWithRemovedAuth() throws Exception {
		String userUri = "http://example.com/user/bob";
		updateAuthenticationContext("bob", userUri, new String[] { "ROLE_USER",
				"ROLE_ORE" });
		String simpleExample = CommonTestRecords.OAC_INLINE_BODY;
		InputStream in = new ByteArrayInputStream(simpleExample.getBytes());
		OREResponse response =  (OREResponse) authController.post(in);

		checkUserInModel(response, userUri);

		String recordId = findUIDFromOREResponse(response);
		in = new ByteArrayInputStream(simpleExample.getBytes());
		response =  (OREResponse) authController.put(recordId, in);

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
		String simpleExample = CommonTestRecords.OAC_WITH_OWNER;
		InputStream in = new ByteArrayInputStream(simpleExample.getBytes());
		OREResponse response = (OREResponse) authController.post(in);

		checkUserInModel(response, userUri);

		String recordId = findUIDFromOREResponse(response);
		in = new ByteArrayInputStream(simpleExample.getBytes());
		response =  (OREResponse) authController.put(recordId, in);

		checkUserInModel(response, userUri);
	}

	
	
	//
	// Private Methods
	//
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
		OREResponse response = (OREResponse) controller.post(in);

		String id = findUIDFromOREResponse(response);

		return id;
	}
	
	private String findUIDFromOREResponse(OREResponse response) {
		
		// assertTrue(redirect.startsWith("redirect:"));
		String redirect = response.getLocationHeader();
		String createdId = redirect.substring(redirect.lastIndexOf("/") + 1);
		return createdId;
		
	}
	
	private String findUIDFromOACResponse(ModelAndView response){
		ModelSet annotations = (ModelSet) response.getModel().get("annotations");
		assertNotNull(annotations);
		String theId = annotations.getModelURIs().next().toString();
		String createdId = theId.substring(theId.lastIndexOf("/") + 1);
		return createdId;
		
	}
	private void checkUserInOACModel(ModelAndView response, String userUri) {
		ModelSet annotations = (ModelSet) response.getModel().get("annotations");
		Model model = annotations.getModels().next();

		URI userPred = model.createURI(LoreStoreConstants.LORESTORE_USER);
		assertEquals(1, model.countStatements(new TriplePatternImpl(
				Variable.ANY, userPred, Variable.ANY)));

		assertTrue(model.contains(Variable.ANY, userPred, userUri));
	}

}
