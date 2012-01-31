<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ page import="au.edu.diasb.emmet.model.*, org.springframework.security.core.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>lorestore</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Annotation and Resource Map Repository Service</h1>
        </div>
        <div class="row">
          <div class="span10">

            <p>This service has been developed by UQ ITEE eResearch group for the <a href="http://openannotation.org/">Open Annotation Collaboration</a>, to support annotation in the context of electronic scholarly editions.</p>
            <p>lorestore is a repository for OAC annotations and ORE resource maps, providing a REST API for creation, update, deletion and search of annotations or resource maps, as well as a SPARQL endpoint.</p>

    		<p>For further information, see <a href="http://openannotation.metadata.net">Open Annotation at UQ</a>, or for technical assistance with LORE or the annotation service, contact Anna Gerber (<a href="mailto:agerber&#64;itee.uq.edu.au">agerber&#64;itee.uq.edu.au</a>)</p>
    		
    <!-- Props
      <c:forEach items='${props}' var='p'>
         <ul>
            <li>Prop <c:out value='${p}'/></li>
         </ul>
      </c:forEach>
     -->
          </div>
          <%@ include file="sidebar.jsp" %>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
