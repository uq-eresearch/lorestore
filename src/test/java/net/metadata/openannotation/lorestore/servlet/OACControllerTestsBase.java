package net.metadata.openannotation.lorestore.servlet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.metadata.openannotation.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.openannotation.lorestore.access.DefaultLoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.OACController;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;
import net.metadata.openannotation.test.mocks.MockAuthenticationContext;
import net.metadata.openannotation.test.mocks.MockOREIdentityProvider;

import org.junit.Before;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;

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
		
		// Load oac schema into repository
		ClassLoader cl = this.getClass().getClassLoader();
	    java.io.InputStream in = cl.getResourceAsStream("oac.trig");
	    ModelSet connection = cf.retrieveConnection();
	    connection.readFrom(in, Syntax.Trig, "http://www.openannotation.org/ns/");
	    cf.release(connection);
	}

	private OACController createController() throws InterruptedException {
		noauthOCC = new LoreStoreControllerConfig();
		noauthOCC.setContainerFactory(cf);
		noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
		noauthOCC.setBaseUri("http://example.com/");
		noauthOCC.setUidGenerator(new UIDGenerator());
		noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
		noauthOCC.setDefaultSchema("http://www.openannotation.org/ns/");
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
