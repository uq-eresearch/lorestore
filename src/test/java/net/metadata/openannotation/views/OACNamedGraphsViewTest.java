package net.metadata.openannotation.views;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

import net.metadata.openannotation.lorestore.servlet.CommonTestRecords;
import net.metadata.openannotation.lorestore.servlet.OREResponse;
import net.metadata.openannotation.lorestore.servlet.OREResponseView;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.misc.TestUtilities;
import net.metadata.openannotation.views.OACNamedGraphsView;


import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class OACNamedGraphsViewTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ModelAndView mv;
	private OACNamedGraphsView view;

	@Before
	public void setUp() throws Exception {
		Model model1 = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.OAC_WITH_OWNER.getBytes()),
				"http://example.com/oac/");
		Model model2 = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.OAC_INLINE_BODY.getBytes()),
				"http://example.com/oac/");
		
		MemoryTripleStoreConnectorFactory mf = new MemoryTripleStoreConnectorFactory();
		ModelSet ms = mf.retrieveConnection();
		ms.addModel(model1);
		ms.addModel(model2);
		model1.close();
		model2.close();
		view = new OACNamedGraphsView();
		mv = new ModelAndView("oac");
		mv.addObject("annotations",ms);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void constructorTest() {
		new OACNamedGraphsView();
	}

	@Test
	public void renderTrix() throws Exception {
		request.addHeader("Accept", "application/trix");
		view.render(mv.getModel(), request, response);
		//System.out.print(response.getContentAsString());
		assertEquals("application/xml", response.getContentType());
		
	}
	@Test
	public void renderRDFXML() throws Exception {
		request.addHeader("Accept", "application/rdf+xml");
		view.render(mv.getModel(), request, response);
		assertEquals("application/rdf+xml", response.getContentType());
	}
	@Test
	public void renderTrig() throws Exception {
		request.addHeader("Accept", "application/x-trig");
		view.render(mv.getModel(), request, response);
		assertEquals("application/x-trig", response.getContentType());
	}
	@Test
	public void renderJson() throws Exception {
		request.addHeader("Accept", "application/json");
		view.render(mv.getModel(), request, response);
		//System.out.print(response.getContentAsString());
		assertEquals("application/json", response.getContentType());
	}
}
