package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.rdf2go.OpenAnnotationImpl;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;

public class OpenAnnotationImplTest {

	private OpenAnnotationImpl oa;
	private String testRecordURL = "http://localhost:8080/lorestore/oa/27CFBFF2CD023285";
	@Before
	public void setUp() throws Exception {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model model = modelFactory.createModel();
		model.open();
		model.readFrom(new ByteArrayInputStream(CommonTestRecords.OAC_WITH_OWNER.getBytes()));
		model.commit();
		
		oa = new OpenAnnotationImpl(model);
	}

	@Test
	public void testGetURL() throws LoreStoreException {
		long beforeSize = oa.getModel().size();
		assertEquals(testRecordURL, oa.getURL());
		assertEquals(beforeSize, oa.getModel().size());
	}

	@Test
	public void testAssignURI() throws LoreStoreException {
		String newUri = "http://example.com/oa/rand-id-1928";
		long beforeSize = oa.getModel().size();
		
		oa.assignURI(newUri);
		
		assertEquals(newUri, oa.getURL());
		String rdfxml = oa.getModelAsRDFXML();

		assertFalse(rdfxml.contains(testRecordURL));
		assertEquals(beforeSize, oa.getModel().size());
	}

	@Test
	public void testAssignUser() throws LoreStoreException {
		
		String userUri = "http://localhost/user/1";
		oa.setUser(userUri);
		
		String rdfxml = oa.getModelAsRDFXML();
		assertTrue(rdfxml.contains(userUri));
		
		String userName = "Test User";
		oa.setUserWithName(userUri, userName);
		
		rdfxml = oa.getModelAsRDFXML();
		assertTrue(rdfxml.contains(userName));
		assertTrue(rdfxml.contains("foaf/0.1/Person"));
	}
	
}
