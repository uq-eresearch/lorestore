package net.metadata.openannotation.lorestore.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import au.edu.diasb.chico.mvc.BaseView;
import au.edu.diasb.chico.mvc.MimeTypes;
import de.dfki.km.json.JSONUtils;

public class ValidationReportView extends BaseView {
         public ValidationReportView() {
            super(Logger.getLogger(ValidationReportView.class));
        }
         
        @Override
        protected void renderMergedOutputModel(Map<String, Object> map, 
                HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
            ArrayList<Map<String,Object>> result = (ArrayList<Map<String,Object>>)map.get("result");
            PrintWriter out = response.getWriter();
            if (isAcceptable(MimeTypes.HTML_MIMETYPE, request)){
                response.setContentType(MimeTypes.HTML_MIMETYPE);
                out.print("<html><head><link type='text/css' href='/lorestore/stylesheets/bootstrap.min.css' rel='stylesheet'><link type='text/css' href='/lorestore/stylesheets/lorestore.css' rel='stylesheet'></head>");
                out.print("<body><div class='container'><div class='content'><div class='page-header main-page-header'>");
                out.print("<h1>Validation report</h1></div>");
                for (Map<String,Object> section : result){
                    out.print("<h2>" + section.get("section") + "</h2>");
                    // for each of the section.constraints
                    for (Map<String,String> constraint: ((List<Map<String,String>>)section.get("constraints"))){
                        out.print("<p>");
                        if ("pass".equals(constraint.get("status"))){
                            out.print("PASS ");
                        } else if ("warn".equals(constraint.get("status"))){
                            out.print("WARN ");
                        } else {
                            out.print("ERROR ");
                        }
                        out.print(constraint.get("ref") + " " + constraint.get("description") + "</p>");
                    }
                    
                }
                out.print("</div></div></body></html>");
                out.flush();
            } else if (isAcceptable("application/json", request)){
                response.setContentType("application/json");
                out.print(JSONUtils.toString(result));
                out.flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                        "Validation results only available in JSON or HTML formats");
            }
     }

}
