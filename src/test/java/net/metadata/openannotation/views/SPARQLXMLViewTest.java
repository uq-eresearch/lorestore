package net.metadata.openannotation.views;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

import net.metadata.openannotation.lorestore.servlet.CommonTestRecords;
import net.metadata.openannotation.lorestore.servlet.LoreStoreResponse;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.views.OACNamedGraphsView;
import net.metadata.openannotation.lorestore.views.LoreStoreResponseView;
import net.metadata.openannotation.lorestore.views.SPARQLXMLView;
import net.metadata.openannotation.misc.TestUtilities;


import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class SPARQLXMLViewTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ModelAndView mv;
	private SPARQLXMLView view;

	@Before
	public void setUp() throws Exception {
		
		view = new SPARQLXMLView();
		mv = new ModelAndView("sparqlxml");
		mv.addObject("sparqlxml", CommonTestRecords.EMPTY_SPARQL_RESULT);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void constructorTest() {
		new SPARQLXMLView();
	}

	
	@Test
	public void renderRDFXML() throws Exception {
		request.addHeader("Accept", "application/rdf+xml");
		view.render(mv.getModel(), request, response);
		assertEquals("application/rdf+xml", response.getContentType());
	}
	
	@Test
	public void renderXML() throws Exception {
		request.addHeader("Accept", "application/xml");
		view.render(mv.getModel(), request, response);
		assertEquals("application/xml", response.getContentType());
	}
	
}
