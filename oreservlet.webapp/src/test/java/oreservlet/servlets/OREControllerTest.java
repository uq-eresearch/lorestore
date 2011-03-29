package oreservlet.servlets;


import org.junit.Before;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import au.edu.diasb.chico.mvc.RequestFailureException;

public class OREControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	private OREController getController() {
		OREControllerConfig occ = new OREControllerConfig();
		return new OREController(occ);
	}
	
	@Test
	public void constructor() {
		getController();
	}
	
	@Test
	public void plainGet() throws Exception {
		OREController controller = getController();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://doc.localhost/ore/rem/13532");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		controller.get(request, response);
	}
}
