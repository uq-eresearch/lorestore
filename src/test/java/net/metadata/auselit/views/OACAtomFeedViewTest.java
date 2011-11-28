package net.metadata.auselit.views;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;


import static org.junit.Assert.*;

import net.metadata.auselit.lorestore.servlet.CommonTestRecords;
import net.metadata.auselit.lorestore.servlet.OREResponse;
import net.metadata.auselit.lorestore.servlet.OREResponseView;
import net.metadata.auselit.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.auselit.misc.TestUtilities;


import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class OACAtomFeedViewTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ModelAndView mv;
	private OACAtomFeedView view;

	@Before
	public void setUp() throws Exception {
		Model model1 = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.OAC_WITH_PROPS.getBytes()),
				"http://example.com/oac/");
		Model model2 = TestUtilities.create(new ByteArrayInputStream(
				CommonTestRecords.OAC_WITH_OWNER.getBytes()),
				"http://example.com/oac/");
		
		ArrayList<Model> annotations = new ArrayList<Model>();
		annotations.add(model1);
		annotations.add(model2);
		
		view = new OACAtomFeedView();
		mv = new ModelAndView("oacAtom");
		mv.addObject("annotations",annotations);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void constructorTest() {
		new OACAtomFeedView();
	}

	
	@Test
	public void renderAtom() throws Exception {
		
		view.render(mv.getModel(), request, response);
		//System.out.print(response.getContentAsString());
		assertEquals("application/atom+xml", response.getContentType());
		
	}
	
}
