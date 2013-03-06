package net.metadata.openannotation.lorestore.views;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USE_STYLESHEET;
import static net.metadata.openannotation.lorestore.common.LoreStoreProperties.DEFAULT_RDF_STYLESHEET_PROP;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.LOCATION_HEADER;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.ORE_PROPS_KEY;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.MODEL_KEY;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.RETURN_STATUS;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.OVERRIDE_CTYPE;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.util.OAJSONLDSerializer;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;

import de.dfki.km.json.JSONUtils;
import de.dfki.km.json.jsonld.JSONLD;
import de.dfki.km.json.jsonld.JSONLDProcessor.Options;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;

public class OREResponseView extends BaseView {
    public OREResponseView() {
        super(Logger.getLogger(OREResponseView.class));
    }
    
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, 
            HttpServletRequest request, HttpServletResponse response) 
    throws IOException {
    	Model model = (Model) map.get(MODEL_KEY);
        Properties props = (Properties) map.get(ORE_PROPS_KEY);
        String overrideContentType = (String) map.get(OVERRIDE_CTYPE);
        
        String stylesheetParam = request.getParameter(LORESTORE_USE_STYLESHEET);
        // FIXME: remove hardcoded stylesheet
        String stylesheetURI = (stylesheetParam == null) ? "/lorestore/stylesheets/CompoundObjectDetail.xsl" :
                (stylesheetParam.length() == 0) ? props.getProperty(DEFAULT_RDF_STYLESHEET_PROP, null) :
                    stylesheetParam;
        
        setResponseHeaders(map, response);
        
        OutputStream os = null;
        
        String acceptableContentType = overrideContentType;
        if (overrideContentType == null || overrideContentType.equals("")){
            // set acceptable content type by looking at request
            if (isAcceptable(MimeTypes.XML_RDF_MIMETYPES, request)){
                if (isAcceptable(MimeTypes.XML_MIMETYPE, request)){
                    acceptableContentType = MimeTypes.XML_MIMETYPE;
                } else {
                    acceptableContentType = Syntax.RdfXml.getMimeType();
                }
            } else if (isAcceptable(Syntax.Trix.getMimeType(),request)){
                acceptableContentType = Syntax.Trix.getMimeType();
            } else if (isAcceptable(Syntax.Trig.getMimeType(), request)){
                acceptableContentType = Syntax.Trig.getMimeType();
            } else if (isAcceptable("application/json", request)) {
                acceptableContentType = "application/json";
            }
        }
        try {
            if (acceptableContentType == null) {
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                        "Request response only available in RDF+XML, JSON-LD, TriG or TriX formats");
            }
            if (model != null) {
                if (acceptableContentType.equals(Syntax.RdfXml.getMimeType()) || acceptableContentType.equals(MimeTypes.XML_MIMETYPE)){
                    response.setCharacterEncoding("UTF-8");
                    os = outputRDF(response, model, stylesheetURI, Syntax.RdfXml);
                } else if (acceptableContentType.equals(Syntax.Trix.getMimeType())) {
                    // TODO: add default stylesheet for TriX
                    response.setCharacterEncoding("UTF-8");
                    os = outputRDF(response, model, stylesheetURI, Syntax.Trix);
                } else if (acceptableContentType.equals(Syntax.Trig.getMimeType())){
                    os = outputRDF(response, model, null, Syntax.Trig);
                } else if (acceptableContentType.equals("application/json")){
                    os = outputJSON(response, model); 
                } 
                response.setContentType(acceptableContentType);
            } else if (props != null) {
                if (isAcceptable(MimeTypes.XML_MIMETYPE, request)) {
                    os = outputProps(response, props);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                            "Request response is only available as application/xml");
                }
            } else {
                logger.error("No content has been set");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                        "No content has been set");
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (model != null) {
            	model.close();
            }
        }
    }
    
    private void setResponseHeaders(Map<String, Object> map,
			HttpServletResponse response) {
		Object loc = map.get(LOCATION_HEADER);
		if (loc != null) {
			response.setHeader("Location", (String)loc);
		}
		Object status = map.get(RETURN_STATUS);
		if (status != null) {
			response.setStatus((Integer)status);
		}
		
	}

	private OutputStream outputProps(HttpServletResponse response, Properties props) 
    throws IOException {
        response.setContentType(MimeTypes.XML_MIMETYPE);
        OutputStream os = response.getOutputStream();
        props.storeToXML(os, "Lorestore properties", "UTF-8");
        return os;
    }

    private OutputStream outputRDF(HttpServletResponse response,  
    		Model model, String stylesheetURL, Syntax contentType) 
    throws IOException {
        response.setCharacterEncoding("UTF-8");
        if (stylesheetURL == null || !contentType.equals(Syntax.RdfXml.getMimeType())) {
            OutputStream os = response.getOutputStream();
            // serialize  (without stylesheet PI)
            model.writeTo(os, contentType);
            return os;
        } else { 
            // inject stylesheeet PI for RDF/XML only
            
            // I considered tweaking the RDF serializers to add the stylesheet processing
            // instruction.  Sesame would support this, but it would be difficult with Jena.
            // Instead, we serialize to a StringBuffer and do some simple surgery to insert
            // the required stuff.
            StringWriter sw = new StringWriter();
            model.writeTo(sw, Syntax.RdfXml);
            StringBuffer sb = sw.getBuffer();
            // The insertion point will be immediately after the '?>' of the xml declaration
            // (if present) or at the start of the document.
            int insertionPoint = 0;
            int xmlDeclStart = sb.indexOf("<?xml ");
            if (xmlDeclStart != -1) {
                insertionPoint = sb.indexOf("?>", xmlDeclStart);
                if (insertionPoint == -1) {
                    logger.error("Malformed XML decl");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                            "Malformed XML decl");
                    return null;
                } else {
                    insertionPoint += "?>".length();
                }
            }
            // The inserted text may include an xml declaration.  (Some RDF/XML serializers
            // don't include one by default.)
            String insertion = ((insertionPoint == 0) ? "<?xml version=\"1.0\" encoding=\"UTF-8\"?>": "") +
                    "\n<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheetURL + "\"?>\n";
            sb.insert(insertionPoint, insertion);
            OutputStream os = response.getOutputStream();
            os.write(sb.toString().getBytes());
            return os;
        }
    }

    private OutputStream outputJSON(HttpServletResponse response, Model model) 
            throws IOException {
        OutputStream os = response.getOutputStream(); 
        
        // TODO implement JSON-LD serialization for resource maps
        
        PrintStream printStream = new PrintStream(os);
        
        printStream.print("{\"@id\":\"" + model.getContextURI() + "\"}");
        printStream.close();
        return os;
    }
}
