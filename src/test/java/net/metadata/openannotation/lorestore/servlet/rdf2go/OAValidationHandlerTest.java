package net.metadata.openannotation.lorestore.servlet.rdf2go;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.metadata.openannotation.lorestore.access.AllowEverythingAccessPolicy;
import net.metadata.openannotation.lorestore.servlet.OATestRecords;
import net.metadata.openannotation.lorestore.servlet.LoreStoreControllerConfig;
import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;
import net.metadata.openannotation.test.mocks.MockOREIdentityProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.servlet.ModelAndView;
@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class OAValidationHandlerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private OAValidationHandler handler;
    private LoreStoreControllerConfig noauthOCC;
    private final String contentType = "application/x-trig";
    @Before
    public void setUp() {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
            noauthOCC = new LoreStoreControllerConfig();
            noauthOCC.setContainerFactory(new MemoryTripleStoreConnectorFactory());
            noauthOCC.setAccessPolicy(new AllowEverythingAccessPolicy());
            noauthOCC.setBaseUri("http://example.com/");
            noauthOCC.setUidGenerator(new UIDGenerator());
            noauthOCC.setIdentityProvider(new MockOREIdentityProvider());
            handler = new OAValidationHandler(noauthOCC);
    }

    @After
    public void cleanUpStreams() {
            System.setOut(null);
            System.setErr(null);
    }
    // first a few sanity checks to make sure that some basic types of input validate with the right number of warnings, errors etc
    @Test
    public void noData() throws Exception {
        // all rules are skipped when data is empty
        checkTestCounts("",0,0,0,55);
    }
    @Test
    public void basicWithWarnings() throws Exception {
        // minimal annotation based on SOPA example
        checkTestCounts(OATestRecords.OA_BASIC, 9, 5 ,0, 41);
    }
    @Test
    public void basicFullyValid() throws Exception {
        // minimal annotation but with basic provenance etc so that all recommended and should rules are satisfied (i.e. no warnings)
        checkTestCounts(OATestRecords.OA_BASIC_FULLY_VALID, 18, 0, 0, 37);
    }
    @Test
    public void multipleAnnotations() throws Exception{
        // check validation still works when multiple annotations are supplied
    }
    
    // Test individual rules return the correct status
    @Test
    public void bodyTargetIdentifiers() throws Exception {
        // The Body and Target SHOULD be identified by HTTP URIs unless they are embedded within the Annotation.
        checkTestResult("2.1.0. (1) Body and Target Resources", OATestRecords.OA_BLANKNODE_TARGET, "warn", "Blank Node Target");
        checkTestResult("2.1.0. (1) Body and Target Resources", OATestRecords.OA_UUID_BODY, "warn", "UUID Body");
        checkTestResult("2.1.0. (1) Body and Target Resources", OATestRecords.OA_EMBEDDED_BODY, "pass", "Embedded UUID Body");
        checkTestResult("2.1.0. (1) Body and Target Resources", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "HTTP Identifiers");
    }
   
    @Test
    public void annoClass() throws Exception{
        // The oa:Annotation class MUST be associated with each Annotation.
        checkTestResult("2.1.0. (2) Body and Target Resources", OATestRecords.OA_NO_TYPE, "error", "No oa:Annotation class");
        checkTestResult("2.1.0. (2) Body and Target Resources", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "oa:Annotation class asserted");
    }
    
    @Test
    public void annoSubclassing() throws Exception{
        // Additional subclassing is ONLY RECOMMENDED in order to provide additional, community-specific constraints on the model.
        checkTestResult("2.1.0. (3) Body and Target Resources", OATestRecords.OA_CUSTOM_SUBCLASS, "warn", "Custom subclass");
        checkTestResult("2.1.0. (3) Body and Target Resources", OATestRecords.OA_CUSTOM_CLASS, "pass", "Custom annotation type (not oa:Annotation subclass)");
        checkTestResult("2.1.0. (3) Body and Target Resources", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "No subclassing");
    }
    @Test
    public void hasBodyWarn() throws Exception{
        // There SHOULD be 1 or more oa:hasBody relationships associated with an Annotation but there MAY be 0.
        checkTestResult("2.1.0. (4) Body and Target Resources", OATestRecords.OA_NO_BODY, "warn", "No body");
        checkTestResult("2.1.0. (4) Body and Target Resources", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Body exists");
    }
    @Test
    public void hasTarget() throws Exception{
        // There MUST be 1 or more oa:hasTarget relationships associated with an Annotation.
        checkTestResult("2.1.0. (5) Body and Target Resources", OATestRecords.OA_NO_TARGET, "error", "No target");
        checkTestResult("2.1.0. (5) Body and Target Resources", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Target exists");
    }
    @Test
    public void contentClasses() throws Exception{
        // There SHOULD be 1 or more content-based classes associated with the Body and Target resources of an Annotation.
        checkTestResult("2.1.1. (1) Typing of Body and Target", OATestRecords.OA_BASIC, "warn", "No content types");
        checkTestResult("2.1.1. (1) Typing of Body and Target", OATestRecords.OA_CONTENT_TYPES_WARNING, "warn", "Content types but not for all");
        checkTestResult("2.1.1. (1) Typing of Body and Target", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Content types for all");
        // TODO specific resource pass (should be associated with hasSource resource)
        // TODO specific resource fail
    }
    @Test
    public void dctypes() throws Exception {
        // The Dublin Core Types vocabulary is RECOMMENDED.
        checkTestResult("2.1.1. (2) Typing of Body and Target", OATestRecords.OA_BASIC, "warn", "No types");
        checkTestResult("2.1.1. (2) Typing of Body and Target", OATestRecords.OA_CONTENT_TYPES_NOT_DC, "warn", "Uses Types but not DCMI");
        checkTestResult("2.1.1. (2) Typing of Body and Target", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Uses DCMI Types");
    }
    @Test
    public void imageAsText() throws Exception {
        //The advice of the DCMI to encode images of text as dctypes:Text is NOT RECOMMENDED.
        checkTestResult("2.1.1. (3) Typing of Body and Target", OATestRecords.OA_IMAGE_AS_TEXT, "warn", "Image with dctypes Text");
        checkTestResult("2.1.1. (3) Typing of Body and Target", OATestRecords.OA_IMAGE_AS_IMAGE, "pass", "Image with dctypes Image");
    }
    @Test
    public void embeddedContentAsTextClass() throws Exception {
        // The cnt:ContentAsText class SHOULD be assigned to the embedded body resource
        checkTestResult("2.1.2. (1) Embedded Textual Bodies", OATestRecords.OA_EMBEDDED_BODY_NO_CONTENTASTEXT, "warn", "No ContentAsText type");
        checkTestResult("2.1.2. (1) Embedded Textual Bodies", OATestRecords.OA_EMBEDDED_BODY, "pass", "ContentAsText type exists");
        checkTestResult("2.1.2. (1) Embedded Textual Bodies", OATestRecords.OA_BASIC, "skip", "No embedded content");
    }
    @Test
    public void language3066() throws Exception {
        //Each language SHOULD be expressed as a language tag, as defined by RFC 3066
        checkTestResult("2.1.2. (2) Embedded Textual Bodies", OATestRecords.OA_EMBEDDED_BODY_NONRFC3066_LANGUAGE, "warn", "Prop dc:langugae does not conform to RFC3066");
        checkTestResult("2.1.2. (2) Embedded Textual Bodies", OATestRecords.OA_EMBEDDED_BODY, "pass", "Valid dc language");
        checkTestResult("2.1.2. (2) Embedded Textual Bodies", OATestRecords.OA_BASIC, "skip", "No dc:language");
    }
    @Test
    public void tagClass() throws Exception {
        //The type oa:Tag (or subclass e.g. oa:SemanticTag) MUST be associated with the tagging resource
        checkTestResult("2.1.3. (1) Tags and Semantic Tags", OATestRecords.OA_TAGGING_MOTIVATION_NOT_TAG, "error", "SemanticTag class not present");
        checkTestResult("2.1.3. (1) Tags and Semantic Tags", OATestRecords.OA_SEMANTICTAG, "pass", "SemanticTag class present");
        checkTestResult("2.1.3. (1) Tags and Semantic Tags", OATestRecords.OA_BASIC, "skip", "No tags");
    }
    @Test
    public void taggingMotivation() throws Exception {
        //Annotations that tag resources, either with text or semantic tags, SHOULD also have the oa:tagging motivation.
        checkTestResult("2.1.3. (2) Tags and Semantic Tags", OATestRecords.OA_SEMANTICTAG_WRONG_MOTIVATION, "warn", "No tagging Motivation");
        checkTestResult("2.1.3. (2) Tags and Semantic Tags", OATestRecords.OA_SEMANTICTAG, "pass", "Tagging Motivation present");
        checkTestResult("2.1.3. (2) Tags and Semantic Tags", OATestRecords.OA_BASIC, "skip", "No tags");
    }
    @Test
    public void annotatedBy() throws Exception {
        //There SHOULD be exactly 1 oa:annotatedBy relationship per Annotation but MAY be 0 or more than 1
        checkTestResult("2.2.0. (1) Annotation Provenance", OATestRecords.OA_BASIC, "warn", "No oa:annotatedBy");
        checkTestResult("2.2.0. (1) Annotation Provenance", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Prop oa:annotatedBy present");
    }
    @Test
    public void annotatedAtExists() throws Exception {
        //There SHOULD be exactly 1 oa:annotatedAt property per Annotation.
        checkTestResult("2.2.0. (2) Annotation Provenance", OATestRecords.OA_BASIC, "warn", "No annotatedAt");
        checkTestResult("2.2.0. (2) Annotation Provenance", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "One annotatedAt prop");
    }
    @Test
    public void annotatedAtNotMoreThanOne() throws Exception {
        // There MUST NOT be more than 1 oa:annotatedAt property per Annotation.
        checkTestResult("2.2.0. (3) Annotation Provenance", OATestRecords.OA_MULTIPLE_ANNOTATEDAT, "error", "Too many annotatedAt props");
        checkTestResult("2.2.0. (3) Annotation Provenance", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "One annotatedAt");
        checkTestResult("2.2.0. (3) Annotation Provenance", OATestRecords.OA_BASIC, "pass", "No annotatedAt");
    }
    @Test
    public void provDateTime8601() throws Exception {
        // The datetime for oa:annotatedAt and oa:serializedAt MUST be expressed in the xsd:dateTime (ISO 8601) format.
        checkTestResult("2.2.0. (4) Annotation Provenance", OATestRecords.OA_BASIC, "skip", "No annotatedAt or serializedAt properties");
        checkTestResult("2.2.0. (4) Annotation Provenance", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Valid annotatedAt dateTime");
        checkTestResult("2.2.0. (4) Annotation Provenance", OATestRecords.OA_BASIC_INVALID_DATETIME, "error", "Invalid annotatedAt dateTime");
    }
    @Test
    public void provDateTimeTimeZone() throws Exception{
        // The datetime for oa:annotatedAt and oa:serializedAt SHOULD have a timezone specified
        checkTestResult("2.2.0. (5) Annotation Provenance", OATestRecords.OA_BASIC, "skip", "No annotatedAt or serializedAt properties");
        checkTestResult("2.2.0. (5) Annotation Provenance", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Valid annotatedAt time zone");
        checkTestResult("2.2.0. (5) Annotation Provenance", OATestRecords.OA_BASIC_INVALID_DATETIME, "warn", "Invalid annotatedAt time zone");
    }
    @Test
    public void serializedAtNotMoreThanOne(){
        //There MUST NOT be more than 1 oa:serializedAt property per Annotation
    }
    @Test
    public void foafPerson(){
        //It is RECOMMENDED to use foaf:Person as the class of the object of oa:annotatedBy
    }
    @Test
    public void provSoftwareAgent(){
        //It is RECOMMENDED to use prov:SoftwareAgent as the class of the object of oa:serializedBy
    }
    @Test
    public void agentName() throws Exception {
        // Each agent SHOULD have exactly 1 name property.
        checkTestResult("2.2.1. (3) Agents", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Agent name exists");
        checkTestResult("2.2.1. (3) Agents", OATestRecords.OA_NO_AGENT_NAME, "warn", "No Agent name");
        checkTestResult("2.2.1. (3) Agents", OATestRecords.OA_MULTIPLE_AGENT_NAME, "warn", "Too many Agent name props");
    }
    @Test
    public void motivatedBy() throws Exception {
        //Each Annotation SHOULD have at least one oa:motivatedBy relationship.
        checkTestResult("2.3. (1) Motivations", OATestRecords.OA_BASIC, "warn", "No motivatedBy");
        checkTestResult("2.3. (1) Motivations", OATestRecords.OA_BASIC_FULLY_VALID, "pass", "Prop oa:motivatedBy exists");
    }
    @Test
    public void specificResourceClass() throws Exception {
        //The oa:SpecificResource class SHOULD be associated with a Specific Resource
        checkTestResult("3.1.0. (1) Specifiers and Specific Resources", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Specific Resource has class");
        checkTestResult("3.1.0. (1) Specifiers and Specific Resources", OATestRecords.OA_SR_NO_CLASS, "warn", "Specific Resource has no class");
        checkTestResult("3.1.0. (1) Specifiers and Specific Resources", OATestRecords.OA_BASIC, "skip", "No Specific Resources");
    }
    @Test
    public void hasSource() throws Exception {
        //There MUST be exactly 1 oa:hasSource relationship associated with a Specific Resource.
        checkTestResult("3.1.0. (2) Specifiers and Specific Resources", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Specific Resource has one haSelector");
        checkTestResult("3.1.0. (2) Specifiers and Specific Resources", OATestRecords.OA_SPECIFIC_RESOURCE_NO_SOURCE_OR_SELECTOR, "error", "No hasSource");
        checkTestResult("3.1.0. (2) Specifiers and Specific Resources", OATestRecords.OA_INVALID_SR, "error", "Too many hasSource props");
        checkTestResult("3.1.0. (2) Specifiers and Specific Resources", OATestRecords.OA_BASIC, "skip", "No Specific Resources");
    }
    @Test
    public void specificResourceIdentifier() throws Exception {
        //Specific Resource SHOULD be identified by a globally unique URI
        checkTestResult("3.1.0. (3) Specifiers and Specific Resources", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Specific Resource has UUID URN");
        checkTestResult("3.1.0. (3) Specifiers and Specific Resources", OATestRecords.OA_INVALID_SR, "warn", "Specific Resource does not have URN");
        checkTestResult("3.1.0. (3) Specifiers and Specific Resources", OATestRecords.OA_BASIC, "skip", "No Specific Resources");
    }
    @Test
    public void hasSelectorNotMoreThanOne() throws Exception {
        //There MUST be exactly 0 or 1 oa:hasSelector relationship associated with a Specific Resource.
        checkTestResult("3.2.0. (1) Selectors", OATestRecords.OA_SPECIFIC_RESOURCE_NO_SOURCE_OR_SELECTOR, "pass", "No hasSelector");
        checkTestResult("3.2.0. (1) Selectors", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Prop oa:hasSelector exists");
        checkTestResult("3.2.0. (1) Selectors", OATestRecords.OA_FRAGMENT_SELECTOR_INVALID, "error", "Too many hasSelector props");
    }
    @Test
    public void fragmentSelector() throws Exception {
        //It is RECOMMENDED to use oa:FragmentSelector rather than annotating the fragment URI directly.
        checkTestResult("3.2.1. (1) Fragment Selector", OATestRecords.OA_FRAGMENT_TARGET, "warn", "Target with Fragment URI");
        checkTestResult("3.2.1. (1) Fragment Selector", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Fragment Selector");
        checkTestResult("3.2.1. (1) Fragment Selector", OATestRecords.OA_BASIC, "skip", "No fragments");
    }
    @Test
    public void fragmentSelectorValue() throws Exception{
        //The oa:FragmentSelector MUST have exactly 1 rdf:value property
        checkTestResult("3.2.1. (2) Fragment Selector", OATestRecords.OA_FRAGMENT_SELECTOR_INVALID, "error", "No rdf:value");
        checkTestResult("3.2.1. (2) Fragment Selector", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Prop rdf:value exists");
        checkTestResult("3.2.1. (2) Fragment Selector", OATestRecords.OA_BASIC, "skip", "No fragment selector");
    }
    @Test
    public void fragmentSelectorConformsTo() throws Exception {
        //The Fragment Selector SHOULD have a dcterms:conformsTo relationship.
        checkTestResult("3.2.1. (3) Fragment Selector", OATestRecords.OA_FRAGMENT_SELECTOR_INVALID, "warn", "No dcterms:conformsTo");
        checkTestResult("3.2.1. (3) Fragment Selector", OATestRecords.OA_FRAGMENT_SELECTOR, "pass", "Prop dcterms:conformsTo exists");
        checkTestResult("3.2.1. (3) Fragment Selector", OATestRecords.OA_BASIC, "skip", "No fragment selector");
    }
    @Test
    public void textPosSelStart(){
        //Each TextPositionSelector MUST have exactly 1 oa:start property
    }
    @Test
    public void textPosSelEnd(){
        //Each TextPositionSelector MUST have exactly 1 oa:end property
    }
    @Test
    public void textPosSelPlusState(){
        //It is RECOMMENDED that a State be used in addition to a TextPositionSelector.
    }
    @Test
    public void textQuoteSelHasExact(){
        //Each TextQuoteSelector MUST have exactly 1 oa:exact property.
    }
    @Test
    public void textQuoteSelPrefix(){
        //Each TextQuoteSelector SHOULD have exactly 1 oa:prefix property
    }
    @Test
    public void textQuoteSelPrefixNotMoreThanOne(){
        //Each TextQuoteSelector MUST NOT have more than 1 oa:prefix property.
    }
    @Test
    public void textQuoteSelSuffix(){
        //Each TextQuoteSelector SHOULD have exactly 1 oa:suffix property.
    }
    @Test
    public void textQuoteSelSuffixNotMoreThanOne(){
        //Each TextQuoteSelector MUST NOT have more than 1 oa:suffix property.
    }
    @Test
    public void dataPosSelStart(){
        //Each DataPositionSelector MUST have exactly 1 oa:start property.
    }
    @Test
    public void dataPosSelEnd(){
        //Each DataPositionSelector MUST have exactly 1 oa:end property.
    }
    @Test
    public void stateNotMoreThanOne(){
        //There MAY be 0 or 1 oa:hasState relationship for each SpecificResource
    }
    @Test
    public void when8601(){
        //The timestamp for oa:when MUST be expressed in the xsd:dateTime (ISO 8601) format
    }
    @Test
    public void whenTimeZone(){
        //The timestamp for oa:when SHOULD have a timezone specified
    }
    @Test
    public void whenOrCachedSource(){
        //There MUST be at least one of oa:when or oa:cachedSource.
    }
    @Test
    public void httpRequestStateValue(){
        //There MUST be exactly 1 rdf:value property per HTTPRequestState.
    }
    @Test
    public void styledByNotMoreThanOne() throws Exception{
        //There MAY be 0 or 1 styledBy relationships for each Annotation.
        checkTestResult("3.4. (1) Styles", OATestRecords.OA_BASIC_STYLED_BY, "pass", "One oa:styledBy");
        checkTestResult("3.4. (1) Styles", OATestRecords.OA_BASIC_MULTIPLE_STYLED_BY, "error", "Too many oa:styledBy props");
    }
    @Test
    public void multiplicityIdentifier(){
        //Multiplicity Constructs SHOULD have a globally unique URI
    }
    @Test
    public void multiplicityItem(){
        //There MUST be 1 or more item relationships for each multiplicity construct
    }
    @Test
    public void choiceDefault(){
        //There SHOULD be exactly 1 default relationship for each Choice
    }
    @Test
    public void compositeItems(){
        //Each Composite MUST have two or more constituent resources
    }
    @Test
    public void embeddedResourceChars() throws Exception{
        //There MUST be exactly 1 cnt:chars property for a ContentAsText resource.
        checkTestResult("5.2. (1) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_NO_ENCODING_OR_CHARS, "error", "No cnt:chars");
        checkTestResult("5.2. (1) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY, "pass", "One cnt:chars");
        checkTestResult("5.2. (1) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_MULTIPLE_CHARS, "error", "Too many cnt:chars props");
        checkTestResult("5.2. (1) Embedding Resources", OATestRecords.OA_BASIC, "skip", "No embedded resources");
    }
    @Test
    public void embeddedResourceBytes() throws Exception {
        //There MUST be exactly 1 cnt:bytes property for a ContentAsBase64 resource
        checkTestResult("5.2. (2) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_BASE64_NO_BYTES, "error", "Prop cnt:bytes not found");
        checkTestResult("5.2. (2) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_BASE64, "pass", "One cnt:bytes prop");
        checkTestResult("5.2. (2) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_BASE64_MULTIPLE_BYTES, "error", "Too many cnt:bytes props");
        checkTestResult("5.2. (2) Embedding Resources", OATestRecords.OA_BASIC, "skip", "No embedded resources");
    }
    @Test
    public void embeddedResourceEncoding() throws Exception {
        //There SHOULD be exactly 1 cnt:characterEncoding for a ContentAsText or ContentAsBase64 resource
        checkTestResult("5.2. (3) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_NO_ENCODING_OR_CHARS, "warn", "No encoding");
        checkTestResult("5.2. (3) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_MULTIPLE_ENCODING, "warn", "Too many characterEncoding props");
        checkTestResult("5.2. (3) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY, "pass", "Encoding exists");
        checkTestResult("5.2. (3) Embedding Resources", OATestRecords.OA_BASIC, "skip", "No embedded resources");
    }
    @Test
    public void embeddedResourceFormat() throws Exception {
        //There SHOULD be exactly 1 dc:format property associated with each embedded resource
        checkTestResult("5.2. (4) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY_NO_FORMAT, "warn", "No dc:format");
        checkTestResult("5.2. (4) Embedding Resources", OATestRecords.OA_EMBEDDED_BODY, "pass", "Prop dc:format exists");
        checkTestResult("5.2. (4) Embedding Resources", OATestRecords.OA_BASIC, "skip", "No embedded resources");
    }
    @Test
    public void motivationsSubclass() throws Exception{
        //New Motivations MUST be instances of oa:Motivation, which is a subClass of skos:Concept.
        checkTestResult("B. (1) Extending Motivations", OATestRecords.OA_MOTIVATION_NO_INSTANCE, "error", "Motivation is not instance of oa:Motivation");
        checkTestResult("B. (1) Extending Motivations", OATestRecords.OA_MOTIVATION_NO_BROADER, "pass", "Motivation is instance");
        checkTestResult("B. (1) Extending Motivations", OATestRecords.OA_BASIC_FULLY_VALID, "skip", "No new Motivations");
    }
    
    @Test
    public void motivationsBroader() throws Exception {
        //The skos:broader relationship SHOULD be asserted between the new Motivation and at least one existing Motivation
        checkTestResult("B. (2) Extending Motivations", OATestRecords.OA_MOTIVATION_NO_INSTANCE, "warn", "No broader");
        checkTestResult("B. (2) Extending Motivations", OATestRecords.OA_MOTIVATION_VALID, "pass", "Broader exists");
        checkTestResult("B. (2) Extending Motivations", OATestRecords.OA_BASIC_FULLY_VALID, "skip", "No new Motivations");
    }
    
    private void checkTestCounts(String annotation, int passExpect, int warnExpect, int errorExpect, int skipExpect) throws Exception{
        ByteArrayInputStream inputRDF = new ByteArrayInputStream(annotation.getBytes());
        ModelAndView mav = handler.validate(inputRDF, contentType);
        HashMap<String,Object>result = (HashMap<String,Object>) mav.getModelMap().get("result");
        int pass = (Integer) result.get("pass");
        int warn = (Integer) result.get("warn");
        int error = (Integer) result.get("error");
        int skip = (Integer) result.get("skip");
        assertEquals("Number of passes",pass,passExpect);
        assertEquals("Number of warns",warn,warnExpect);
        assertEquals("Number of errors",error,errorExpect);
        assertEquals("Number of skips",skip,skipExpect);
    }
    
    /* Run a validation test and check that the status was as expected */
    private void checkTestResult(String ref, String annotation,String status, String message) throws Exception {
        ByteArrayInputStream inputRDF = new ByteArrayInputStream(annotation.getBytes());
        ModelAndView mav = handler.validate(inputRDF, contentType);
        HashMap<String,Object>result = (HashMap<String,Object>) mav.getModelMap().get("result");
        HashMap<String,Object> ruleResult = findResult(result,ref);
        assertEquals(ref + ": " +  message, status, (String)ruleResult.get("status"));
    }
    /* Find the rule identified by ref in a set of results obj */
    private HashMap<String,Object> findResult(HashMap<String,Object> obj, String ref){
        ArrayList<HashMap<String,Object>> ruleResults = (ArrayList<HashMap<String,Object>>) obj.get("result");
        for (HashMap<String,Object> section : ruleResults){
            for (HashMap<String, Object> ruleResult : (ArrayList<HashMap<String,Object>>) section.get("constraints")){
                if (ruleResult.get("ref").equals(ref)){
                    return ruleResult;
                }
            }
        }
        return null;
    }
}
