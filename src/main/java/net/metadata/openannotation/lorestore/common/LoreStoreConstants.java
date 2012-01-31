package net.metadata.openannotation.lorestore.common;


public interface LoreStoreConstants {

	String ORE_USE_STYLESHEET = "oreUseStylesheet";
	
	String ORE_SYNTAX_URL = "http://www.openarchives.org/ore/terms/";
	String OAC_SYNTAX_URL = "http://www.openannotation.org/ns/";
	
	String ORE_RESOURCEMAP_CLASS = ORE_SYNTAX_URL + "ResourceMap";
	
	String ORE_AGGREGATION_CLASS = ORE_SYNTAX_URL + "Aggregation";
	
	String OAC_ANNOTATION_CLASS = OAC_SYNTAX_URL + "Annotation";
	String OAC_REPLY_CLASS = OAC_SYNTAX_URL + "Reply";
	String OAC_DATA_ANNOTATION_CLASS = OAC_SYNTAX_URL + "DataAnnotation";
	String OAC_TARGET_PROPERTY = OAC_SYNTAX_URL + "hasTarget";
	
	String ORE_DESCRIBES_PROPERTY = ORE_SYNTAX_URL + "describes";
	
	
	
    /**
     * The namespace URL for RDF syntax.
     */
    String RDF_SYNTAX_URL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    //
    // Properties defined by the RDF schema.
    //

    String RDF_TYPE_PROPERTY = RDF_SYNTAX_URL + "type";
    
    
    
    String DCTERMS_NS_URL = "http://purl.org/dc/terms/";
    String DCTERMS_CREATED = DCTERMS_NS_URL + "created";
    String DCTERMS_MODIFIED = DCTERMS_NS_URL + "modified";
    String DCTERMS_CREATOR = DCTERMS_NS_URL + "creator";
    
    String DC_NS_URL = "http://purl.org/dc/elements/1.1/";
    String DC_CREATOR = DC_NS_URL + "creator";
    String DC_TITLE = DC_NS_URL + "title";
    
    
    String LORESTORE_NS_URL = "http://auselit.metadata.net/lorestore/";
    String LORESTORE_USER = LORESTORE_NS_URL + "user";

    String LORESTORE_PRIVATE = LORESTORE_NS_URL + "isPrivate";
    String LORESTORE_LOCKED = LORESTORE_NS_URL + "isLocked";
    

	String SPARQL_RESULTS_XML = "application/sparql-results+xml";
	String RDF_RESULTS_XML = "application/rdf+xml";
	public static final String AGGREGATION = "#aggregation";
}
