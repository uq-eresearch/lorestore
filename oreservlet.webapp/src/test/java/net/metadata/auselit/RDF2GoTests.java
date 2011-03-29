package net.metadata.auselit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.impl.URIGenerator;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.rdf2go.RepositoryModelSet;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.sail.memory.MemoryStore;

public class RDF2GoTests {

	@Before
	public void setUp() throws Exception {
	}

	public Repository getInMemRepo() throws RepositoryException {
		SailRepository myRepo = new SailRepository(new MemoryStore());
		myRepo.initialize();
		return myRepo;
	}
	
	public Repository getRemoteRepo() throws RepositoryException {
		HTTPRepository myRepo = new HTTPRepository("http://doc.localhost/openrdf-sesame/repositories/lore");
		myRepo.initialize();
		return myRepo;
	}
	

	public void connectToMemorySesame() throws Exception {
		Model model = new RepositoryModel(getInMemRepo());
		model.open();
		assertTrue(model.isEmpty());
	}
	
	
	public Model create(InputStream in) throws ModelRuntimeException, IOException {
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		URI uri = URIGenerator.createNewRandomUniqueURI("http://example.com/rem/");
		Model model = modelFactory.createModel(uri);
		model.open();
		model.readFrom(in);
		return model;
	}
	
	public Model save(Model model) throws RepositoryException {
		Repository remoteRepo = getRemoteRepo();
		RepositoryModelSet modelSet = new RepositoryModelSet(remoteRepo);
		modelSet.open();
		
		modelSet.addModel(model);
		modelSet.commit();
		
		return model;
	}
	
	

	public void connectToRemoteSesame() throws Exception {
		Repository myRepo = getRemoteRepo();
		RepositoryConnection connection = myRepo.getConnection();
		System.out.println(connection.size());
		connection.export(new RDFXMLWriter(System.out));

		
		
		Repository rdf2goRepo = getRemoteRepo();

		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();
		
		modelSet.dump();

		ClosableIterator<URI> modelURIs = modelSet.getModelURIs();
		while (modelURIs.hasNext()) {
			System.out.println(modelURIs.next());
		}
	
		System.out.println(modelSet.size());
		assertFalse(modelSet.isEmpty());
	}
	
	
	@Test
	public void saveAndRetrieve() throws Exception {
		Model model = create(new ByteArrayInputStream(getSampleRDF().getBytes()));
		save(model);
		String stringUri = model.getContextURI().toString();
		System.out.println("Created URI: " + stringUri);
		
		Model retrievedModel = retrieve(stringUri);
		assertFalse(retrievedModel.isEmpty());
		retrievedModel.writeTo(System.out);
		
		
		assertTrue(delete(stringUri));
		

		
	}

	public Model retrieve(String stringURI) throws Exception {
		Repository rdf2goRepo = getRemoteRepo();

		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();
		
		URI uri = modelSet.createURI(stringURI);
		Model model = modelSet.getModel(uri);
		
		return model;		
	}
	
	
	public boolean delete(String stringURI) throws Exception {
		Repository rdf2goRepo = getRemoteRepo();
		ModelSet modelSet = new RepositoryModelSet(rdf2goRepo);
		modelSet.open();
		
		return modelSet.removeModel(modelSet.createURI(stringURI));
	}

	
	
	private String getSampleRDF() {
	return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
	"<rdf:RDF\r\n" + 
	"	xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\r\n" + 
	"	xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\r\n" + 
	"	xmlns:annoreply=\"http://www.w3.org/2001/12/replyType#\"\r\n" + 
	"	xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"\r\n" + 
	"	xmlns:ore=\"http://www.openarchives.org/ore/terms/\"\r\n" + 
	"	xmlns:annotea=\"http://www.w3.org/2000/10/annotation-ns#\"\r\n" + 
	"	xmlns:vanno=\"http://austlit.edu.au/ontologies/2009/03/lit-annotation-ns#\"\r\n" + 
	"	xmlns:sparql=\"http://www.w3.org/2005/sparql-results#\"\r\n" + 
	"	xmlns:dcterms=\"http://purl.org/dc/terms/\"\r\n" + 
	"	xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\r\n" + 
	"	xmlns:http=\"http://www.w3.org/1999/xx/http#\"\r\n" + 
	"	xmlns:layout=\"http://maenad.itee.uq.edu.au/lore/layout.owl#\"\r\n" + 
	"	xmlns:thread=\"http://www.w3.org/2001/03/thread#\"\r\n" + 
	"	xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\r\n" + 
	"	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\r\n" + 
	"	xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\r\n" + 
	"	xmlns:austlit=\"http://austlit.edu.au/owl/austlit.owl#\"\r\n" + 
	"	xmlns:oac=\"http://www.openannotation.org/ns/\"\r\n" + 
	"	xmlns:annotype=\"http://www.w3.org/2000/10/annotationType#\">\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"http://doc.localhost/rem/7d5d612e-1965-f6de-1d90-d3a10db2de1c\">\r\n" + 
	"	<ore:describes rdf:resource=\"http://doc.localhost/rem/7d5d612e-1965-f6de-1d90-d3a10db2de1c#aggregation\"/>\r\n" + 
	"	<rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/ResourceMap\"/>\r\n" + 
	"	<dcterms:modified rdf:datatype=\"http://purl.org/dc/terms/W3CDTF\">2011-02-23T17:02:49+10:00</dcterms:modified>\r\n" + 
	"	<dcterms:created rdf:datatype=\"http://purl.org/dc/terms/W3CDTF\">2011-02-23T17:02:14+10:00</dcterms:created>\r\n" + 
	"	<dc:creator>Damien Ayers</dc:creator>\r\n" + 
	"	<dc:title>Local Test CO</dc:title>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"http://doc.localhost/rem/7d5d612e-1965-f6de-1d90-d3a10db2de1c#aggregation\">\r\n" + 
	"	<rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/Aggregation\"/>\r\n" + 
	"	<dcterms:modified>2011-02-23T17:02:49+10:00</dcterms:modified>\r\n" + 
	"	<ore:aggregates rdf:resource=\"http://omad.net/\"/>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"http://omad.net/\">\r\n" + 
	"	<dc:title>omad.net</dc:title>\r\n" + 
	"	<dc:format>text/html; charset=UTF-8</dc:format>\r\n" + 
	"	<layout:x>40</layout:x>\r\n" + 
	"	<layout:y>40</layout:y>\r\n" + 
	"	<layout:width>220</layout:width>\r\n" + 
	"	<layout:height>170</layout:height>\r\n" + 
	"	<layout:originalHeight>-1</layout:originalHeight>\r\n" + 
	"	<layout:orderIndex>1</layout:orderIndex>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"http://doc.localhost/rem/96ae9ffb-905a-79a6-b5bb-b927d94ef8b1\">\r\n" + 
	"	<ore:describes rdf:resource=\"http://doc.localhost/rem/96ae9ffb-905a-79a6-b5bb-b927d94ef8b1#aggregation\"/>\r\n" + 
	"	<rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/ResourceMap\"/>\r\n" + 
	"	<dcterms:modified rdf:datatype=\"http://purl.org/dc/terms/W3CDTF\">2011-03-02T13:46:40+10:00</dcterms:modified>\r\n" + 
	"	<dcterms:created rdf:datatype=\"http://purl.org/dc/terms/W3CDTF\">2011-03-02T13:43:33+10:00</dcterms:created>\r\n" + 
	"	<dc:creator>Damien Ayers</dc:creator>\r\n" + 
	"	<dc:title>Test CO</dc:title>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"http://doc.localhost/rem/96ae9ffb-905a-79a6-b5bb-b927d94ef8b1#aggregation\">\r\n" + 
	"	<rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/Aggregation\"/>\r\n" + 
	"	<dcterms:modified>2011-03-02T13:46:40+10:00</dcterms:modified>\r\n" + 
	"	<ore:aggregates rdf:resource=\"https://doc.localhost/oreservlet/secure/login.html\"/>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"<rdf:Description rdf:about=\"https://doc.localhost/oreservlet/secure/login.html\">\r\n" + 
	"	<dc:title>503 Service Temporarily Unavailable</dc:title>\r\n" + 
	"	<dc:format>text/html;charset=utf-8</dc:format>\r\n" + 
	"	<layout:x>40</layout:x>\r\n" + 
	"	<layout:y>40</layout:y>\r\n" + 
	"	<layout:width>383</layout:width>\r\n" + 
	"	<layout:height>285</layout:height>\r\n" + 
	"	<layout:originalHeight>-1</layout:originalHeight>\r\n" + 
	"	<layout:orderIndex>1</layout:orderIndex>\r\n" + 
	"</rdf:Description>\r\n" + 
	"\r\n" + 
	"</rdf:RDF>";
}
	
}
