package net.metadata.openannotation.lorestore.servlet;

import java.io.ByteArrayInputStream;
import static org.junit.Assert.*;

import net.metadata.openannotation.lorestore.servlet.OREResponse;
import net.metadata.openannotation.lorestore.servlet.OREResponseView;
import net.metadata.openannotation.misc.TestUtilities;


import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class OREResponseViewTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void constructorTest() {
		new OREResponseView();
	}

	@Test
	public void renderRDF() throws Exception {
		OREResponseView view = new OREResponseView();
		Model model = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.ORE_TEXT.getBytes()),
				"http://example.com/rem/");
		OREResponse mv = new OREResponse(model);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		view.render(mv.getModel(), request, response);
		
		assertEquals("application/rdf+xml", response.getContentType());
	}
}
