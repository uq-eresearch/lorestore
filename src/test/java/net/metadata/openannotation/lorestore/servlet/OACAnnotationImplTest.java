package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.rdf2go.OACAnnotationImpl;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;

public class OACAnnotationImplTest {

	private OACAnnotationImpl oac;
	private String testRecordURL = "http://localhost:8080/lorestore/oac/27CFBFF2CD023285";
	@Before
	public void setUp() throws Exception {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model model = modelFactory.createModel();
		model.open();
		model.readFrom(new ByteArrayInputStream(CommonTestRecords.OAC_WITH_OWNER.getBytes()));
		model.commit();
		
		oac = new OACAnnotationImpl(model);
	}

	@Test
	public void testGetURL() throws LoreStoreException {
		long beforeSize = oac.getModel().size();
		assertEquals(testRecordURL, oac.getURL());
		assertEquals(beforeSize, oac.getModel().size());
	}

	@Test
	public void testAssignURI() throws LoreStoreException {
		String newUri = "http://example.com/oac/rand-id-1928";
		long beforeSize = oac.getModel().size();
		
		oac.assignURI(newUri);
		
		assertEquals(newUri, oac.getURL());
		String rdfxml = oac.getModelAsRDFXML();

		assertFalse(rdfxml.contains(testRecordURL));
		assertEquals(beforeSize, oac.getModel().size());
	}

	
}
