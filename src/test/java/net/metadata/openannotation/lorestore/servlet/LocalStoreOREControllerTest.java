package net.metadata.openannotation.lorestore.servlet;

import javax.xml.xpath.XPathFactory;

import net.metadata.openannotation.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.OREController;
import net.metadata.openannotation.lorestore.triplestore.NativeTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.triplestore.SimpleSesamePool;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.ontoware.rdf2go.model.ModelSet;

public abstract class LocalStoreOREControllerTest extends OREControllerTest {

	
	@Before
	@Override
	public void setUp() {
		
	}
	
	@BeforeClass
	public static void setupConnections() throws Exception {
		controller = getController();
		xPath = XPathFactory.newInstance().newXPath();
	}

	private static OREController getController() throws InterruptedException {
		LoreStoreControllerConfig occ = new LoreStoreControllerConfig();

		NativeTripleStoreConnectorFactory cf = new NativeTripleStoreConnectorFactory();
		cf.setDataDirPath("D:/temp/");
		TripleStoreConnectorFactory pool = new SimpleSesamePool(cf);
		occ.setContainerFactory(pool);

		occ.setAccessPolicy(new AllowEverythingAccessPolicy());
		occ.setBaseUri("http://example.com/");
		occ.setUidGenerator(new UIDGenerator());
		return new OREController(occ);
	}
	
	
	@Before
	public void clearTriplestore() throws Exception {

		TripleStoreConnectorFactory containerFactory = controller
				.getControllerConfig().getContainerFactory();
		ModelSet retrieveConnection = null;
		try {
			retrieveConnection = containerFactory.retrieveConnection();
			retrieveConnection.removeAll();
		} finally {
			containerFactory.release(retrieveConnection);
		}

	}
}
