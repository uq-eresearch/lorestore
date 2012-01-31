package net.metadata.openannotation.lorestore.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.model.rdf2go.CompoundObjectImpl;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

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
	public void testGetResourceMapURL() throws LoreStoreException {
		long beforeSize = co.getModel().size();
		assertEquals(testRecordURL, co.getURL());
		assertEquals(beforeSize, co.getModel().size());
	}

	@Test
	public void testAssignURI() throws LoreStoreException {
		String newUri = "http://example.com/rem/rand-id-1928";
		long beforeSize = co.getModel().size();
		
		co.assignURI(newUri);
		
		assertEquals(newUri, co.getURL());
		String rdfxml = co.getModelAsRDFXML();

		assertFalse(rdfxml.contains(testRecordURL));
		assertEquals(beforeSize, co.getModel().size());
	}

	@Test
	public void getObjectOwner() {
		String ownerId = co.getOwnerId();
		assertEquals("http://doc.localhost/users/ore", ownerId);
	}
	
	@Test
	public void setObjectOwner() throws LoreStoreException {
		String useruri = "http://austlit.edu.au/auselit/users/omad";
		co.setUser(useruri);
		
		assertEquals(useruri, co.getUser().toString());
	}
	
	
	@Test
	public void containerWithMultipleModels() throws ModelRuntimeException, IOException, RepositoryException, LoreStoreException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample-compound-objects.trig");
		SailRepository sailRepository = new SailRepository(new MemoryStore());
		sailRepository.initialize();
		
		RepositoryModelSet modelSet = new RepositoryModelSet(sailRepository);
		modelSet.open();
		modelSet.readFrom(inputStream, Syntax.Trig);
		
		
		
		
		URI uri = modelSet.createURI("http://doc.localhost/danno/ore/dfb45021-1a74-f7b7-eb46-8d0077f3c4bc");
		Model model = modelSet.getModel(uri);
		co = new CompoundObjectImpl(model);
		
//		System.out.println(co.getModel().serialize(Syntax.RdfXml));
		assertEquals("Anna Gerber", co.getCreator());
		
	}
}
