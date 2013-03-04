package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import net.metadata.openannotation.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;
import net.metadata.openannotation.test.mocks.MockOREIdentityProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RDF2GoOAUpdateHandlerTest  {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private RDF2GoOAUpdateHandler uh;
	private LoreStoreControllerConfig noauthOCC;
	
	@Before
	public void setUp() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		noauthOCC = new LoreStoreControllerConfig();
		noauthOCC.setContainerFactory(new MemoryTripleStoreConnectorFactory());
		noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
		noauthOCC.setBaseUri("http://example.com/");
		noauthOCC.setUidGenerator(new UIDGenerator());
		noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
		uh = new RDF2GoOAUpdateHandler(noauthOCC);
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}


	@Test
	public void bulkImport() throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("anno.trig");
		int delta = uh.bulkImport(inputStream, "anno.trig");
		// for anno.trig delta will be 17
		assertTrue(delta > 0);
	}


}
