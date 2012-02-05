package net.metadata.openannotation.views;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DC_CREATOR;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.DC_TITLE;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_DATA_ANNOTATION_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_REPLY_CLASS;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.OAC_TARGET_PROPERTY;
import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.ORE_USE_STYLESHEET;
import static net.metadata.openannotation.lorestore.common.LoreStoreProperties.DEFAULT_RDF_STYLESHEET_PROP;
import static net.metadata.openannotation.lorestore.servlet.OREResponse.ORE_PROPS_KEY;
import static net.metadata.openannotation.lorestore.servlet.OREResponse.RESPONSE_RDF_KEY;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.metadata.openannotation.lorestore.model.rdf2go.OACAnnotationImpl;
import net.metadata.openannotation.lorestore.servlet.OREResponseView;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.util.TypeConverter;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;
import org.xml.sax.InputSource;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Person;

public class OACNamedGraphsView extends BaseView {
	 	public OACNamedGraphsView() {
	        super(Logger.getLogger(OACNamedGraphsView.class));
	    }
	 	
	 	@Override
	    protected void renderMergedOutputModel(Map<String, Object> map, 
	            HttpServletRequest request, HttpServletResponse response) 
	    throws IOException {
	 		logger.debug("Annotations render ");
	 		ModelSet annotations = (ModelSet) map.get("annotations");
	 		
	 		
	    	if (annotations == null || annotations.size() == 0){
	    		logger.debug("Problem getting all annotations");
	    		
	    	}
	    	logger.debug("render merged output annotations: ");
	        String stylesheetParam = request.getParameter(ORE_USE_STYLESHEET);
	        /*String stylesheetURI = (stylesheetParam == null) ? null :
	                (stylesheetParam.length() == 0) ? props.getProperty(DEFAULT_RDF_STYLESHEET_PROP, null) :
	                    stylesheetParam;
	        */
	        String stylesheetURI = null;
	        
	        OutputStream os = null;
	        // FIXME the mapping of 'accept' types to formats that we understand and hence
	        // to the content types that we use needs to be soft, and a lot smarter.
	        try {
	            if (annotations != null) {
	            	if (isAcceptable(MimeTypes.XML_RDF, request) || isAcceptable("text/html",request)) {	
	                	stylesheetURI = (stylesheetParam == null || stylesheetParam.length() == 0) ?
	                			"/lorestore/stylesheets/OAC.xsl" : stylesheetParam;
	                	os = outputRDF(response, annotations, stylesheetURI, Syntax.RdfXml);
	                	if (isAcceptable("text/html",request)){
	                		// we don't provide HTML as yet, so return XML with stylesheet instead
	                		response.setContentType(MimeTypes.XML_MIMETYPE);
	                	} else {
	                		response.setContentType(MimeTypes.XML_RDF);
	                	}
	        	        response.setCharacterEncoding("UTF-8");
	                } else if (isAcceptable(MimeTypes.XML_MIMETYPE, request) || isAcceptable("application/trix",request)) {
	            		// TriX is preferred format as it has named graph support
	                	// TODO: add default stylesheet for TriX
	            		os = outputRDF(response, annotations, stylesheetURI, Syntax.Trix);
	         	        response.setContentType(MimeTypes.XML_MIMETYPE);
	        	        response.setCharacterEncoding("UTF-8");
	                } else if (isAcceptable("application/x-trig", request)){
	                	os = outputRDF(response, annotations, null, Syntax.Trig);
	                	response.setContentType("application/x-trig");
	                } else if (isAcceptable("application/json",request)){
	                	os = outputJSON(response, annotations);
	                	response.setContentType("application/json");
	                } else {
	                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
	                            "Request response only available in Trix, RDF+XML, Trig and JSON formats");
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
	            if (annotations != null){
	            	try {
	            		annotations.commit();
	            		annotations.close();
	            	} catch (Exception e){
	            		logger.error("OACNamedGraphsView " + e.getMessage());
	            	};
	            }
	        }
	 }

	 private OutputStream outputRDF(HttpServletResponse response,  
	    		ModelSet annotations, String stylesheetURL, Syntax syntax) 
	    throws IOException {

	        if (stylesheetURL == null) {
	            OutputStream os = response.getOutputStream();
	            annotations.writeTo(os,syntax);
	            return os;
	        } else {

	            // I considered tweaking the RDF serializers to add the stylesheet processing
	            // instruction.  Sesame would support this, but it would be difficult with Jena.
	            // Instead, we serialize to a StringBuffer and do some simple surgery to insert
	            // the required stuff.
	            StringWriter sw = new StringWriter();
	            annotations.writeTo(sw, syntax);
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
	 private OutputStream outputJSON(HttpServletResponse response, ModelSet annotations) 
	    throws IOException {
		 
		 OutputStream os = response.getOutputStream();
		 StringBuffer json = new StringBuffer();
	     try{
			DOMParser dp = new DOMParser();
			ClassLoader cl = this.getClass().getClassLoader();
		    java.io.InputStream in = cl.getResourceAsStream("OAC-to-JSON.xsl");
		    Source xslt = new StreamSource(in);
		    Transformer trans = TransformerFactory.newInstance().newTransformer(xslt);
		    //annotations.dump();
	     	//ClosableIterator<Model> itr = annotations.getModels();
			//    while(itr.hasNext()){
			    	
			//     Model annoModel = (Model) itr.next();
			 //    System.out.println("model is empty? " + annoModel.isEmpty());
			     String rdfXML = annotations.serialize(Syntax.RdfXml);
			     //logger.debug(rdfXML);
			     
			     // process Model to produce custom json format
			     StringWriter writer = new StringWriter();
				     
				 dp.parse(new InputSource(new StringReader(rdfXML)));
				 	
				     
				 trans.transform(new DOMSource(dp.getDocument()),  new StreamResult(writer));
			     json.append(writer.toString());
			   
			//    }
	    } catch (Exception e){
	    	 logger.debug(e.getMessage());
	    }

     	PrintStream printStream = new PrintStream(os);
     	printStream.print(json);
     	printStream.close();
     	return os;
	 }
	
}
