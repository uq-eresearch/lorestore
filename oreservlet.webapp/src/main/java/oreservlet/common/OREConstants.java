package oreservlet.common;

import org.springframework.http.MediaType;

public interface OREConstants {

	String ORE_USE_STYLESHEET = "oreUseStylesheet";
	
	String ORE_SYNTAX_URL = "http://www.openarchives.org/ore/terms/";
	
	String ORE_RESOURCEMAP_CLASS = ORE_SYNTAX_URL + "ResourceMap";
	
	String ORE_AGGREGATION_CLASS = ORE_SYNTAX_URL + "Aggregation";
	
	String ORE_DESCRIBES_PROPERTY = ORE_SYNTAX_URL + "describes";
	
	
	
    /**
     * The namespace URL for RDF syntax.
     */
    String RDF_SYNTAX_URL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    //
    // Properties defined by the RDF schema.
    //

    String RDF_TYPE_PROPERTY = RDF_SYNTAX_URL + "type";

	String SPARQL_RESULTS_XML = "application/sparql-results+xml";

	public static final String AGGREGATION = "#aggregation";
}
