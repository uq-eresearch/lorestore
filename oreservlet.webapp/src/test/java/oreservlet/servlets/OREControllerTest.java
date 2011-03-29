package oreservlet.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import au.edu.diasb.chico.mvc.RequestFailureException;

public class OREControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	private OREController getController() {
		OREControllerConfig occ = new OREControllerConfig();
		// occ.setContainerFactory(new InMemoryTripleStoreConnectorFactory());
		HttpTripleStoreConnectorFactory cf = new HttpTripleStoreConnectorFactory();
		cf.setRepositoryURL("http://localhost:8080/openrdf-sesame/repositories/lore");
		occ.setContainerFactory(cf);
		occ.setAccessPolicy(new DefaultOREAccessPolicy());
		occ.setBaseUri("http://example.com/");
		return new OREController(occ);
	}

	@Test
	public void constructor() {
		getController();
	}

	@Test
	public void getUnknown() throws Exception {
		OREController controller = getController();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://doc.localhost/ore/rem/13532");
		request.setPathInfo("/rem/13532");

		try {
			controller.get(request);
			fail("should have thrown exception");
		} catch (NoSuchRequestHandlingMethodException e) {

		}
	}

	@Test
	public void deleteUnknown() {
		OREController controller = getController();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://doc.localhost/ore/rem/13532");
		request.setPathInfo("/rem/13532");

		try {
			controller.delete(request);
			fail("should have thrown exception");
		} catch (Exception e) {

		}
	}

	@Test
	public void postEmpty() throws RequestFailureException, IOException {
		OREController controller = getController();
		InputStream in = new ByteArrayInputStream("".getBytes());
		try {
			controller.post(in);
			fail("should have thrown exception");
		} catch (RequestFailureException e) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, e.getStatus());
		}
	}

	@Test
	public void postBadURL() {
		fail();
		// Expect sc 400 - bad request
	}

	@Test
	public void postBadCompoundObject() {
		fail();
		// expect 400 - bad request
	}

	@Test
	public void postBadCompoundObject2() {
		// invalid xml or something
		fail();
		// expect 400 - bad request
	}

	@Test
	public void postCompoundObjectAndGet() {
		fail();
		// make sure can both save and retrieve compound objects
	}

	@Test
	public void postGetDeleteGet() throws Exception {
		OREController controller = getController();

		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.ORE_TEXT.getBytes());

		String redirect = controller.post(in);
		assertTrue(redirect.startsWith("redirect:"));
		String createdId = redirect.substring(redirect.lastIndexOf("/"));

		servletRequest = new MockHttpServletRequest();
		servletRequest.setRequestURI(createdId);
		servletRequest.setPathInfo("fakepathinfo");
		OREResponse oreResponse2 = controller.get(servletRequest);
		assertNotNull(oreResponse2);

		assertEquals("ore", oreResponse2.getViewName());
		Map<String, Object> model2 = oreResponse2.getModel();
		Model rdf2 = (Model) model2.get(OREResponse.RESPONSE_RDF_KEY);
		assertNotNull(rdf2);

		servletRequest = new MockHttpServletRequest();
		servletRequest.setRequestURI(createdId);
		servletRequest.setPathInfo("fakepathinfo");
		controller.delete(servletRequest);

		servletRequest = new MockHttpServletRequest();
		servletRequest.setRequestURI(createdId);
		servletRequest.setPathInfo("fakepathinfo");
		try {
			controller.get(servletRequest);
			fail("Object should have been deleted, method should have thrown exception");
		} catch (NoSuchRequestHandlingMethodException e) {
			// Expected
		}

		// check responses of get and delete pass (are 200), and then the second
		// get should 404

	}

}
