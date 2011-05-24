package net.metadata.auselit.performance;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.springframework.http.ResponseEntity;

import net.metadata.auselit.lorestore.servlet.OREControllerConfig;
import net.metadata.auselit.lorestore.servlet.OREQueryHandler;
import net.metadata.auselit.lorestore.servlet.OREUpdateHandler;
import net.metadata.auselit.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.triplestore.NativeTripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.triplestore.SimpleSesamePool;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;

public class SesamePerformanceTests {

	private String dataDirPath = "D:\\temp";
	private String dataFile = "D:\\localhost9090export.trig";
	private int numTests = 10;
//	private String sesameIndexes = "spoc,posc,cspo";
	private String sesameIndexes = "spoc,posc,cspo,ocsp";
//	private String sesameIndexes = "cspo";
	private TripleStoreConnectorFactory cf;
	private OREQueryHandler qh;
	private OREUpdateHandler uh;
	private OREControllerConfig occ;
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
		st.trialBrowse("http://omad.net/about/");
		st.trialKeywordSearch("Damien");
		st.trialKeywordSearch("Anna");
		st.trialExplore("http://omad.net/");
		
		st.printMemoryUsage();
		
		st.end();
	}
	
	public void trialBrowse(String browseUrl) throws Exception {
		System.out.println("trialBrowse");
		for (int i = 0; i < numTests; i++) {
			long startTime = System.currentTimeMillis();
			ResponseEntity<String> browseQuery = qh.browseQuery(browseUrl);
			if (printQueryResults)
				System.out.println(browseQuery.getBody());
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		}
	}
	

	
	public void trialKeywordSearch(String searchString) throws Exception {
		System.out.println("keyword: " + searchString);
		for (int i = 0; i < numTests; i++) {
			long startTime = System.currentTimeMillis();
			ResponseEntity<String> searchQuery = qh.searchQuery(null, null, searchString);
			if (printQueryResults)
				System.out.println(searchQuery.getBody());
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
		occ = new OREControllerConfig();
		occ.setContainerFactory(cf);
		qh = new OREQueryHandler(occ);
	}
	
	public void loadData() throws Exception {
		//do stuff
		uh = new OREUpdateHandler(occ);
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
