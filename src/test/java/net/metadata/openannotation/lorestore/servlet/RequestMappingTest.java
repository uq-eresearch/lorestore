package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;


/**
 * These test cases are to ensure that the @RequestMapping annotations in our controller
 * have been configure properly, and that the correct method will be called.
 * 
 * This may be unnecessary, spring seems to be good at taking care of it all. But it
 * shouldn't do any harm in the case of anything getting mistakenly changed.
 * 
 * Based on:
 * - http://stackoverflow.com/questions/861089/testing-spring-mvc-annotation-mapppings
 * 
 * @author uqdayers
 */
public class RequestMappingTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private OREController oreController;
	private AnnotationController annoController;
	private PlaceholderController pController;
	private AnnotationMethodHandlerAdapter adapter;
	
	private String exampleURI;
	private String exampleID;
	
	@Before
	public void setUp() throws Exception {
		oreController = EasyMock.createNiceMock(OREController.class);
		annoController = EasyMock.createNiceMock(AnnotationController.class);
		pController = EasyMock.createNiceMock(PlaceholderController.class);
		
		adapter = new AnnotationMethodHandlerAdapter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader("Content-Type", "application/rdf+xml");
		exampleURI = "http://example.com/";
		exampleID = "asdn19";
	}

	@Test
	public void plainRetrieveORE() throws Exception {
		EasyMock.expect(oreController.get(exampleID)).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/" + exampleID);
		request.setMethod("GET");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}
	
	@Test
	public void plainRetrieveOA() throws Exception {
		EasyMock.expect(annoController.get(exampleID)).andReturn(null);
		EasyMock.replay(annoController);
		
		request.setRequestURI("/" + exampleID);
		request.setMethod("GET");
		
		adapter.handle(request, response, annoController);
		EasyMock.verify(annoController);
	}
	@Test
	public void plainRetrievePlaceholder() throws Exception {
		EasyMock.expect(pController.get("foo")).andReturn(null);
		EasyMock.replay(pController);
		
		request.setRequestURI("/foo");
		request.setMethod("GET");
		
		adapter.handle(request, response, pController);
		EasyMock.verify(pController);
	}
	
	@Test
	public void post() throws Exception {
		EasyMock.expect(oreController.post(null, "application/rdf+xml")).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/");
		request.setMethod("POST");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}
	
	@Test
	public void put() throws Exception {
		EasyMock.expect(oreController.put(exampleID, null, "application/rdf+xml")).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/" + exampleID);
		request.setMethod("PUT");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}
	
	@Test
	public void delete() throws Exception {
		EasyMock.expect(oreController.delete(exampleID)).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/" + exampleID);
		request.setMethod("DELETE");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}

	@Test
	public void refersToQuery() throws Exception {
		EasyMock.expect(oreController.refersToQuery(exampleURI)).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/");
		request.setParameter("refersTo", exampleURI);
		request.setMethod("GET");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}
	
	@Test
	public void annotatesQuery() throws Exception {
		EasyMock.expect(annoController.refersToQuery(exampleURI)).andReturn(null);
		EasyMock.replay(annoController);
		
		request.setRequestURI("/");
		request.setParameter("annotates", exampleURI);
		request.setMethod("GET");
		
		adapter.handle(request, response, annoController);
		EasyMock.verify(annoController);
	}
	
	@Test
    public void searchQuery() throws Exception {
	   EasyMock.expect(annoController.searchQuery("", "", "test", "date", 0, -1, false, true)).andReturn(new ModelAndView("sparqlxml"));
	   EasyMock.replay(annoController);
       request.setRequestURI("/");
       request.setParameter("matchval", "test");
       request.setMethod("GET");
       ModelAndView mav = adapter.handle(request, response, annoController);
       assertNotNull(mav);
       EasyMock.verify(annoController);
    }
	
	@Test
	public void exploreQuery() throws Exception {
		EasyMock.expect(oreController.exploreQuery(exampleURI)).andReturn(null);
		EasyMock.replay(oreController);
		
		request.setRequestURI("/");
		request.setParameter("exploreFrom", exampleURI);
		request.setMethod("GET");
		
		adapter.handle(request, response, oreController);
		EasyMock.verify(oreController);
	}
	@Test
	public void atomRefersToQuery() throws Exception {
		EasyMock.expect(annoController.atomRefersToQuery(exampleURI)).andReturn(null);
		EasyMock.replay(annoController);
		
		request.setRequestURI("/feed");
		request.setParameter("annotates", exampleURI);
		request.setMethod("GET");
		
		adapter.handle(request, response, annoController);
		EasyMock.verify(annoController);
	}
	
}
