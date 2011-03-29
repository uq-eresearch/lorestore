package net.metadata.auselit.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.metadata.auselit.lorestore.access.DefaultOREAccessPolicy;
import net.metadata.auselit.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.auselit.lorestore.exceptions.NotFoundException;
import net.metadata.auselit.lorestore.exceptions.OREException;
import net.metadata.auselit.lorestore.triplestore.InMemoryTripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.util.UIDGenerator;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import au.edu.diasb.chico.mvc.RequestFailureException;

public class OREControllerTest {

	private InMemoryTripleStoreConnectorFactory cf;
	private OREController controller;
	private XPath xPath;

	@Before
	public void setUp() throws Exception {
		controller = getController();
		xPath = XPathFactory.newInstance().newXPath();
	}

	private OREController getController() {
		OREControllerConfig occ = new OREControllerConfig();
		cf = new InMemoryTripleStoreConnectorFactory();
		occ.setContainerFactory(cf);
//		HttpTripleStoreConnectorFactory cf = new HttpTripleStoreConnectorFactory();
//		cf.setRepositoryURL("http://localhost:8080/openrdf-sesame/repositories/lore");
//		occ.setContainerFactory(cf);
		occ.setAccessPolicy(new DefaultOREAccessPolicy());
		occ.setBaseUri("http://example.com/");
		occ.setUidGenerator(new UIDGenerator());
		return new OREController(occ);
	}

//	@Test
//	public void constructor() {
//		getController();
//	}

//	@Test
//	public void getUnknown() throws Exception {
//		OREController controller = getController();
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setRequestURI("http://doc.localhost/ore/rem/13532");
//		request.setPathInfo("/rem/13532");
//
//		try {
//			controller.get(request);
//			fail("should have thrown exception");
//		} catch (NoSuchRequestHandlingMethodException e) {
//
//		}
//	}

	@Test(expected = NotFoundException.class)
	public void deleteUnknown() throws Exception {
		controller.delete("13532");

	}

	@Test
	public void postEmpty() throws RequestFailureException, IOException, OREException {
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
		ByteArrayInputStream in = new ByteArrayInputStream(CommonTestRecords.BAD_ORE_BROKEN_XML.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test(expected = OREException.class)	
	public void postBadCompoundObject2() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(CommonTestRecords.BAD_ORE_NO_RESOURCEMAP.getBytes());
		controller.post(in);
		// expect 400 - bad request
	}

	@Test
	public void postCompoundObjectAndGet() throws Exception {
		InputStream in = new ByteArrayInputStream(
				CommonTestRecords.SIMPLE_ORE_EXAMPLE.getBytes());
		String redirect = controller.post(in);
		assertTrue(redirect.startsWith("redirect:"));

		assertEquals(16, cf.size());
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

	private String findUIDFromRedirect(String redirect) {
		assertTrue(redirect.startsWith("redirect:"));
		String createdId = redirect.substring(redirect.lastIndexOf("/") + 1);
		return createdId;
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

	private String saveRecordToStore(String recordXML) throws RequestFailureException,
			IOException, OREException {
		InputStream in = new ByteArrayInputStream(
				recordXML.getBytes());
		String redirect = controller.post(in);
		String id = findUIDFromRedirect(redirect);
		return id;
	}
	
	@Test(expected = InvalidQueryParametersException.class)
	public void queryRefersToEmpty() throws Exception {
		controller.refersToQuery("");
	}
	
//	@Test(expected = InvalidQueryParametersException.class)
//	public void queryRefersToInvalidURL() throws Exception {
//		controller.refersToQuery("zxcv");
//	}
	
	@Test
	public void queryRefersToNonExistantURL() throws Exception {
		String body = controller.refersToQuery("http://omad.net/").getBody();
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
		String body = controller.refersToQuery("http://omad.net/").getBody();

		// check the results include the object
		assertNotNull(body);
		assertTrue(body.contains(createdId));
		
		
		Document document = parseXmlToDocument(body);
		assertEquals(1, document.getElementsByTagName("result").getLength());
		
		assertEquals("Damien Ayers", xPath.evaluate("//binding[@name='a']/literal", document));
	}
	
	private Document parseXmlToDocument(String xml) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
	}
}
