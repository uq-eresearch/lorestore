package oreservlet.servlets;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

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
		request.setPathInfo("/rem/13532");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		OREResponse oreResponse = controller.get(request, response);
		
		assertNotNull(oreResponse);
	}
}
