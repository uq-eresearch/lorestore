package net.metadata.openannotation.lorestore.views;

import static net.metadata.openannotation.lorestore.common.LoreStoreConstants.LORESTORE_USE_STYLESHEET;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.OVERRIDE_CTYPE;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.MODEL_KEY;
import static net.metadata.openannotation.lorestore.servlet.LorestoreResponse.MODELSET_KEY;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.triplestore.MemoryTripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.OAJSONLDSerializer;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;
import de.dfki.km.json.JSONUtils;
import de.dfki.km.json.jsonld.JSONLD;
import de.dfki.km.json.jsonld.JSONLDProcessor.Options;

public class OANamedGraphsView extends BaseView {

         public OANamedGraphsView() {
                super(Logger.getLogger(OANamedGraphsView.class));
         }
         
         @Override
        protected void renderMergedOutputModel(Map<String, Object> map, 
                HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
             ModelSet annotations = (ModelSet) map.get(MODELSET_KEY);
             Model annotation = (Model)map.get(MODEL_KEY);
             if (annotations == null && annotation == null){
                 logger.error("No content has been set");
                 response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                         "No content has been set");
             } else if (annotations == null){
                 MemoryTripleStoreConnectorFactory mf = new MemoryTripleStoreConnectorFactory();
                 annotations = mf.retrieveConnection();
                 annotations.addModel(annotation);
             }
             String overrideContentType = (String) map.get(OVERRIDE_CTYPE);

            logger.debug("render merged output annotations: ");
            String stylesheetParam = request.getParameter(LORESTORE_USE_STYLESHEET);
            
            String stylesheetURI = null;
            
            OutputStream os = null;
            // FIXME the mapping of 'accept' types to formats that we understand and hence
            // to the content types that we use needs to be soft, and a lot smarter.
            // Think about using ContentNegotiatingViewResolver
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
                if (annotations != null) {
                    logger.info("setting content type to  " + acceptableContentType);
                    response.setContentType(acceptableContentType);
                    if (acceptableContentType.contains(Syntax.RdfXml.getMimeType()) || acceptableContentType.contains(MimeTypes.XML_MIMETYPE)) {    
                        stylesheetURI = (stylesheetParam == null || stylesheetParam.length() == 0) ?
                                "/lorestore/stylesheets/OA.xsl" : stylesheetParam;
                        response.setCharacterEncoding("UTF-8");
                        os = outputRDF(response, annotations, stylesheetURI, Syntax.RdfXml);
                    } else if (acceptableContentType.contains(Syntax.Trix.getMimeType())) {
                        // TODO: add default stylesheet for TriX
                        response.setCharacterEncoding("UTF-8");
                        os = outputRDF(response, annotations, stylesheetURI, Syntax.Trix);
                    } else if (acceptableContentType.contains(Syntax.Trig.getMimeType())){
                        os = outputRDF(response, annotations, null, Syntax.Trig);
                    } else if (acceptableContentType.contains("application/json")){
                        os = outputJSON(response, annotations);
                    }
                    
                } 
            } finally {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (annotations != null){
                    try {
                        annotations.commit();
                        annotations.close();
                    } catch (Exception e){
                        logger.error("OANamedGraphsView " + e.getMessage());
                    };
                }
            }
     }

     private OutputStream outputRDF(HttpServletResponse response,  
                ModelSet annotations, String stylesheetURL, Syntax syntax) 
        throws IOException {
         
            // it only makes sense to add stylesheet to xml types
            if (stylesheetURL == null || !syntax.getMimeType().contains("xml")) {
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
         StringBuffer jsonResult = new StringBuffer();
         try{
             OAJSONLDSerializer serializer = new OAJSONLDSerializer();
             Object json = JSONLD.fromRDF(annotations, serializer);
             Options opt = new Options();
             opt.optimize = true;
             ClassLoader cl = this.getClass().getClassLoader();
             java.io.InputStream in = cl.getResourceAsStream("oa-context.json");
             Object jsonContext = JSONUtils.fromInputStream(in);
             jsonResult.append(JSONUtils.toString(JSONLD.compact(json, jsonContext, opt)));
        } catch (Exception e){
             logger.error("Error " + e.getMessage());
        }

         PrintStream printStream = new PrintStream(os);
         printStream.print(jsonResult);
         printStream.close();
         return os;
     }
    
}
