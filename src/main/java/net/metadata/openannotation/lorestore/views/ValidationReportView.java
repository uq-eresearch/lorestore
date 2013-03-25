package net.metadata.openannotation.lorestore.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
            HashMap<String,Object>result = (HashMap<String,Object>) map.get("result");
            if (isAcceptable("application/json", request)){
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(JSONUtils.toString(result));
                out.flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                        "Validation results only available in JSON format");
            }
     }

}
