package net.metadata.openannotation.performance;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.servlet.LoreStoreQueryHandler;
import net.metadata.openannotation.lorestore.servlet.LoreStoreUpdateHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREUpdateHandler;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.triplestore.NativeTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.triplestore.SimpleSesamePool;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;

public class SesamePerformanceTests {

	private String dataDirPath = "D:\\temp";
	private String dataFile = "D:\\localhost9090export.trig";
	private int numTests = 10;
//	private String sesameIndexes = "spoc,posc,cspo";
	private String sesameIndexes = "spoc,posc,cspo,ocsp";
//	private String sesameIndexes = "cspo";
	private TripleStoreConnectorFactory cf;
	private LoreStoreQueryHandler qh;
	private LoreStoreUpdateHandler uh;
	private LoreStoreControllerConfig occ;
	private boolean printQueryResults = false;
	
	public static void main(String[] args) throws Exception {
		
		SesamePerformanceTests st = new SesamePerformanceTests();
		
//		st.initMemory();
		st.initNative();
		st.initPool();
		st.setupQueryHandler();
//		st.loadData();
		st.printMemoryUsage();
		
		System.out.println("Sesame Indexes: " + st.sesameIndexes);
		st.trialKeywordSearch("Damien");
		st.trialKeywordSearch("Anna");
		st.trialExplore("http://omad.net/");
		
		st.printMemoryUsage();
		
		st.end();
	}
	

	
	public void trialKeywordSearch(String searchString) throws Exception {
		System.out.println("keyword: " + searchString);
		for (int i = 0; i < numTests; i++) {
			long startTime = System.currentTimeMillis();
			ModelAndView searchQuery = qh.searchQuery(null, null, searchString);
			if (printQueryResults)
				System.out.println(searchQuery.getModel().get("sparqlxml"));
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		}
	}

	public void trialAdvancedSearch(String searchString) throws Exception {
		System.out.println("advanced: " + searchString);
		for (int i = 0; i < numTests; i++) {
			long startTime = System.currentTimeMillis();
			qh.searchQuery(null, "http://purl.org/dc/elements/1.1/creator", searchString);
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		}
	}
	
	public void trialExplore(String startUrl) throws Exception {
		System.out.println("explore: " + startUrl);
		for (int i = 0; i < numTests; i++) {
			long startTime = System.currentTimeMillis();
			qh.exploreQuery(startUrl);
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		}
	}
	
	public void initMemory() throws Exception {
		cf = new MemoryTripleStoreConnectorFactory();
	}
	
	
	public void initNative() throws Exception {
		NativeTripleStoreConnectorFactory ncf = new NativeTripleStoreConnectorFactory();
		ncf.setDataDirPath(dataDirPath);
		ncf.setSesameIndexes(sesameIndexes);
		cf = ncf;
	}
	
	public void initPool() throws Exception {
		SimpleSesamePool pool = new SimpleSesamePool(cf);
		cf = pool;
	}
	
	public void setupQueryHandler() throws Exception {
		occ = new LoreStoreControllerConfig();
		occ.setContainerFactory(cf);
		qh = new RDF2GoOREQueryHandler(occ);
	}
	
	public void loadData() throws Exception {
		//do stuff
		uh = new RDF2GoOREUpdateHandler(occ);
		uh.wipeDatabase();
		
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(dataFile));
		uh.bulkImport(stream, dataFile);
	}

	public void end() throws Exception {
//		uh.wipeDatabase();
		cf.destroy();
	}

	public void printMemoryUsage() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Used memory in bytes: " + memory);
		System.out.println("Used memory in megabytes: "
				+ bytesToMegabytes(memory));
		
	}
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
}
