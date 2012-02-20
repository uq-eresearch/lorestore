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
        <div class="page-header main-page-header">
          
          <h1>Annotation and Resource Map Repository Service</h1>
        </div>
        <div class="row">
          <div class="span5">

            <p>This service has been developed by UQ ITEE eResearch group for the <a href="http://openannotation.org/">Open Annotation Collaboration</a>, to support annotation in the context of electronic scholarly editions.</p>
            <p>lorestore is a repository for OAC annotations and ORE resource maps, providing a REST API for creation, update, deletion and search of annotations or resource maps, as well as a SPARQL endpoint.</p>

            <p>Register for a user account <a href="${secure}/selfRegister.html">here</a>.</p>
 
    		<p>For further information, see <a href="http://openannotation.metadata.net">Open Annotation at UQ</a>, or for technical assistance with LORE or the annotation service, contact Anna Gerber (<a href="mailto:agerber&#64;itee.uq.edu.au">agerber&#64;itee.uq.edu.au</a>)</p>
			<p><em>Please be aware that this service is currently operating as a sandbox: content and/or accounts may be deleted at any time.</em></p>
          </div>
          <div class="span5 sidebarmenu">
            <h2>Sign up</h2>
            <p><a href="${secure}/selfRegister.html">Register a new user account here</a></p>
            
            
            <h2>Create content</h2>
            <p>Use <a href="${secure}/lore.html">lore</a> to create and query resource maps and annotations</p>
            
            <h2>APIs</h2>
            <p><a href="${secure}/docs.html">Read the developer documentation</a> to get started integrating content from lorestore with your application</p>

            <h2>Get lorestore</h2>
            <p>Download, pull, fork or file issues on lorestore at <a target="_blank" href="https://github.com/uq-eresearch/lorestore">GitHub</a></p>
            
           </ul>
        </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
