package net.metadata.auselit.lorestore.servlet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.metadata.auselit.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.auselit.lorestore.access.DefaultOREAccessPolicy;
import net.metadata.auselit.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.util.UIDGenerator;
import net.metadata.auselit.test.mocks.MockAuthenticationContext;
import net.metadata.auselit.test.mocks.MockOREIdentityProvider;

import org.junit.Before;

public class OREControllerTestsBase {

	private TripleStoreConnectorFactory cf;
	private MockOREIdentityProvider ip;
	protected static OREController controller;
	protected static OREController authController;
	static XPath xPath;
	OREControllerConfig noauthOCC;
	OREControllerConfig authReqdOCC;

	@Before
	public void setUp() throws Exception {
		cf = new MemoryTripleStoreConnectorFactory();
		controller = createController();
		authController = createAuthController();
		xPath = XPathFactory.newInstance().newXPath();
	}

	private OREController createController() throws InterruptedException {
		noauthOCC = new OREControllerConfig();
		noauthOCC.setContainerFactory(cf);
		noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
		noauthOCC.setBaseUri("http://example.com/");
		noauthOCC.setUidGenerator(new UIDGenerator());
		noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
		return new OREController(noauthOCC);
	}

	private OREController createAuthController() throws Exception {
		authReqdOCC = new OREControllerConfig();
		authReqdOCC.setContainerFactory(cf);
		ip = new MockOREIdentityProvider();
		DefaultOREAccessPolicy ap = new DefaultOREAccessPolicy();
		ap.setReadAuthorities("ROLE_USER,ROLE_ANONYMOUS");
		ap.setWriteAuthorities("ROLE_ORE");
		ap.setAdminAuthorities("ROLE_ADMIN");
		ap.afterPropertiesSet();
		ap.setIdentityProvider(ip);
		authReqdOCC.setAccessPolicy(ap);
	
		authReqdOCC.setIdentityProvider(ip);
		authReqdOCC.setBaseUri("http://example.com/");
		authReqdOCC.setUidGenerator(new UIDGenerator());
		return new OREController(authReqdOCC);
	}

	protected void updateAuthenticationContext(String username, String uri,
			String[] authorities) throws Exception {
				DefaultOREAccessPolicy accessPolicy = (DefaultOREAccessPolicy)authController.getControllerConfig().getAccessPolicy();
				accessPolicy.setAuthenticationContext(new MockAuthenticationContext(username, uri, authorities));
				accessPolicy.afterPropertiesSet();
				
				ip.setUserURI(uri);
			}

}
