package oreservlet.servlets;

import java.io.ByteArrayInputStream;

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
		Model model = ORETypeFactory.create(new ByteArrayInputStream(
				CommonTestRecords.ORE_TEXT.getBytes()),
				"http://example.com/rem/");
		OREResponse mv = new OREResponse(model);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		view.render(mv.getModel(), request, response);
		
		System.out.println(response.getContentType());
	}
}
