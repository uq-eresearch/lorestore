package net.metadata.openannotation.lorestore.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;

public class ExceptionView extends BaseView {
         public ExceptionView() {
            super(Logger.getLogger(ExceptionView.class));
        }
         
        @Override
        protected void renderMergedOutputModel(Map<String, Object> map, 
                HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
            String className = (String) map.get("className");
            String message = (String) map.get("message");
            Integer statusCode = (Integer) map.get("statusCode");
            response.setStatus(statusCode);
            response.setContentType(MimeTypes.HTML_MIMETYPE);
            PrintWriter out = response.getWriter();
            out.print("<html><body>" + message + "<br/>(" + className + ")</body></html>");
            out.flush();
     }

}
