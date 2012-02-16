package net.metadata.openannotation.lorestore.views;

import static org.junit.Assert.assertEquals;
import net.metadata.openannotation.lorestore.servlet.CommonTestRecords;

import org.junit.Before;
import org.junit.Test;
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
