package net.metadata.openannotation.lorestore.views;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USE_STYLESHEET;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.xml.sax.InputSource;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;

public class SPARQLXMLView extends BaseView {
	 	public SPARQLXMLView() {
	        super(Logger.getLogger(SPARQLXMLView.class));
	    }
	 	
	 	@Override
	    protected void renderMergedOutputModel(Map<String, Object> map, 
	            HttpServletRequest request, HttpServletResponse response) 
	    throws IOException {
	 		String result = (String) map.get("sparqlxml");
	        String stylesheetParam = request.getParameter(LORESTORE_USE_STYLESHEET);
	       
	        String stylesheetURI = null;
	        
	        OutputStream os = null;
	        // FIXME the mapping of 'accept' types to formats that we understand and hence
	        // to the content types that we use needs to be soft, and a lot smarter.
	        // Think about using ContentNegotiatingViewResolver
	        try {
	            if (result != null) {
	            	if (isAcceptable(MimeTypes.XML_RDF_MIMETYPES, request)) {	
	                	stylesheetURI = (stylesheetParam == null || stylesheetParam.length() == 0) ?
	                			"/lorestore/stylesheets/SPARQLresult.xsl" : stylesheetParam;
	                	os = outputRDF(response, result, stylesheetURI, Syntax.RdfXml);
	                	if (isAcceptable(MimeTypes.XML_MIMETYPE, request)){
	                		// we don't provide HTML as yet, use XML with stylesheet instead
	                		response.setContentType(MimeTypes.XML_MIMETYPE);
	                	} else {
	                		response.setContentType(MimeTypes.XML_RDF);
	                	}
	        	        response.setCharacterEncoding("UTF-8");
	                } else {
	                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
	                            "Request response only available in RDF+XML format");
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
	        }
	 }

	 private OutputStream outputRDF(HttpServletResponse response,  
	    		String result, String stylesheetURL, Syntax syntax) 
	    throws IOException {
		 	OutputStream os = response.getOutputStream();
		 	PrintStream printStream = new PrintStream(os);
	        if (stylesheetURL != null) {
	            // I considered tweaking the RDF serializers to add the stylesheet processing
	            // instruction.  Sesame would support this, but it would be difficult with Jena.
	            // Instead, we serialize to a StringBuffer and do some simple surgery to insert
	            // the required stuff.
	        	StringBuffer sb = new StringBuffer(result);
	        	
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
	            //os.write(sb.toString().getBytes());
	            printStream.print(sb.toString());
	        } else {
	            printStream.print(result);
	        }
	        printStream.close();
            return os;
	    }
	 
	
}
