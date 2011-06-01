package net.metadata.auselit.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;

import net.metadata.auselit.lorestore.exceptions.OREException;
import net.metadata.auselit.lorestore.model.rdf2go.CompoundObjectImpl;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;

public class CompoundObjectImplTest {

	private CompoundObjectImpl co;
	private String testRecordURL = "http://doc.localhost/rem/344385ed-2a79-4598-8a99-27be35e0b773";
	@Before
	public void setUp() throws Exception {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model model = modelFactory.createModel();
		model.open();
		model.readFrom(new ByteArrayInputStream(CommonTestRecords.SIMPLE_ORE_EXAMPLE_WITH_OWNER.getBytes()));
		model.commit();
		
		co = new CompoundObjectImpl(model);
	}

	@Test
	public void testGetResourceMapURL() throws OREException {
		long beforeSize = co.getModel().size();
		assertEquals(testRecordURL, co.getResourceMapURL());
		assertEquals(beforeSize, co.getModel().size());
	}

	@Test
	public void testAssignURI() throws OREException {
		String newUri = "http://example.com/rem/rand-id-1928";
		long beforeSize = co.getModel().size();
		
		co.assignURI(newUri);
		
		assertEquals(newUri, co.getResourceMapURL());
		String rdfxml = co.getModelAsRDFXML();

		assertFalse(rdfxml.contains(testRecordURL));
		assertEquals(beforeSize, co.getModel().size());
	}

	@Test
	public void getObjectOwner() {
		String ownerId = co.getOwnerId();
		assertEquals("http://doc.localhost/users/ore", ownerId);
	}
}
