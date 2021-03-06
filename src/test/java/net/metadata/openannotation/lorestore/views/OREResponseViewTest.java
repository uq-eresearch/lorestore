package net.metadata.openannotation.lorestore.views;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import net.metadata.openannotation.lorestore.servlet.CommonTestRecords;
import net.metadata.openannotation.lorestore.servlet.LorestoreResponse;
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
		LorestoreResponse mv = new LorestoreResponse("ore");
		mv.setRDFModel(model);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		view.render(mv.getModel(), request, response);
		
		assertEquals("application/xml", response.getContentType());
	}
}
