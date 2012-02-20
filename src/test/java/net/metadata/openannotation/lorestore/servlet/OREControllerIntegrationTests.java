package net.metadata.openannotation.lorestore.servlet;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;

/**
 * 
 * Based on:
 * - http://stackoverflow.com/questions/1401128/how-to-unit-test-a-spring-mvc-controller-using-pathvariable
 * - http://www.scarba05.co.uk/blog/2010/03/integration-testing-of-springs-mvc-annotation-mapppings-for-controllers/
 * 
 * This is just a mock up, not actually used (yet).
 * 
 * @author uqdayers
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({/*
						 * include live config here e.g.
						 * "file:web/WEB-INF/application-context.xml",
						 * "file:web/WEB-INF/dispatcher-servlet.xml"
						 */})
public class OREControllerIntegrationTests {

	@Autowired
	private ApplicationContext applicationContext;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HandlerAdapter handlerAdapter;
	private OREController controller;
	private LoreStoreControllerConfig occ;

	@Before
	public void setUp() throws Exception {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
		occ = applicationContext.getBean(LoreStoreControllerConfig.class);
		controller = new OREController(occ);
	}
	
	
}
