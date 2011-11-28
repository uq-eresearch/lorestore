package net.metadata.auselit.lorestore.servlet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.metadata.auselit.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.auselit.lorestore.access.DefaultLoreStoreAccessPolicy;
import net.metadata.auselit.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.util.UIDGenerator;
import net.metadata.auselit.test.mocks.MockAuthenticationContext;
import net.metadata.auselit.test.mocks.MockOREIdentityProvider;

import org.junit.Before;

public class OACControllerTestsBase {

	private TripleStoreConnectorFactory cf;
	private MockOREIdentityProvider ip;
	protected static OACController controller;
	protected static OACController authController;
	static XPath xPath;
	LoreStoreControllerConfig noauthOCC;
	LoreStoreControllerConfig authReqdOCC;

	@Before
	public void setUp() throws Exception {
		cf = new MemoryTripleStoreConnectorFactory();
		controller = createController();
		authController = createAuthController();
		xPath = XPathFactory.newInstance().newXPath();
	}

	private OACController createController() throws InterruptedException {
		noauthOCC = new LoreStoreControllerConfig();
		noauthOCC.setContainerFactory(cf);
		noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
		noauthOCC.setBaseUri("http://example.com/");
		noauthOCC.setUidGenerator(new UIDGenerator());
		noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
		return new OACController(noauthOCC);
	}

	private OACController createAuthController() throws Exception {
		authReqdOCC = new LoreStoreControllerConfig();
		authReqdOCC.setContainerFactory(cf);
		ip = new MockOREIdentityProvider();
		DefaultLoreStoreAccessPolicy ap = new DefaultLoreStoreAccessPolicy();
		ap.setReadAuthorities("ROLE_USER,ROLE_ANONYMOUS");
		ap.setWriteAuthorities("ROLE_ORE");
		ap.setAdminAuthorities("ROLE_ADMIN");
		ap.afterPropertiesSet();
		ap.setIdentityProvider(ip);
		authReqdOCC.setAccessPolicy(ap);
	
		authReqdOCC.setIdentityProvider(ip);
		authReqdOCC.setBaseUri("http://example.com/");
		authReqdOCC.setUidGenerator(new UIDGenerator());
		return new OACController(authReqdOCC);
	}

	protected void updateAuthenticationContext(String username, String uri,
			String[] authorities) throws Exception {
				DefaultLoreStoreAccessPolicy accessPolicy = (DefaultLoreStoreAccessPolicy)authController.getControllerConfig().getAccessPolicy();
				accessPolicy.setAuthenticationContext(new MockAuthenticationContext(username, uri, authorities));
				accessPolicy.afterPropertiesSet();
				
				ip.setUserURI(uri);
			}

}
