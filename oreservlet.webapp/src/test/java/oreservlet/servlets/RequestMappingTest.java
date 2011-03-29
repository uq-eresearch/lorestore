package oreservlet.servlets;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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
	private OREController controller;
	private AnnotationMethodHandlerAdapter adapter;

	@Before
	public void setUp() throws Exception {
		controller = EasyMock.createNiceMock(OREController.class);

		adapter = new AnnotationMethodHandlerAdapter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void plainGet() throws Exception {
		request.setRequestURI("/asdfkl");
		request.setMethod("GET");


		EasyMock.expect(controller.get("asdfkl")).andReturn(null);
		EasyMock.replay(controller);
		
		adapter.handle(request, response, controller);
		EasyMock.verify(controller);
	}

}
