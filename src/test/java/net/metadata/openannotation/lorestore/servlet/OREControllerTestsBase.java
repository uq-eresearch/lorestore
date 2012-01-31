package net.metadata.openannotation.lorestore.servlet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.metadata.openannotation.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.openannotation.lorestore.access.DefaultLoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.OREController;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;
import net.metadata.openannotation.test.mocks.MockAuthenticationContext;
import net.metadata.openannotation.test.mocks.MockOREIdentityProvider;

import org.junit.Before;

public class OREControllerTestsBase {

	private TripleStoreConnectorFactory cf;
	private MockOREIdentityProvider ip;
	protected static OREController controller;
	protected static OREController authController;
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

	private OREController createController() throws InterruptedException {
		noauthOCC = new LoreStoreControllerConfig();
		noauthOCC.setContainerFactory(cf);
		noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
		noauthOCC.setBaseUri("http://example.com/");
		noauthOCC.setUidGenerator(new UIDGenerator());
		noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
		return new OREController(noauthOCC);
	}

	private OREController createAuthController() throws Exception {
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
		return new OREController(authReqdOCC);
	}

	protected void updateAuthenticationContext(String username, String uri,
			String[] authorities) throws Exception {
				DefaultLoreStoreAccessPolicy accessPolicy = (DefaultLoreStoreAccessPolicy)authController.getControllerConfig().getAccessPolicy();
				accessPolicy.setAuthenticationContext(new MockAuthenticationContext(username, uri, authorities));
				accessPolicy.afterPropertiesSet();
				
				ip.setUserURI(uri);
			}

}
