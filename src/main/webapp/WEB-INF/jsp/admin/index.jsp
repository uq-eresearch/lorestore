<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Content Management</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header main-page-header">
          <h1>Content Management</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
			 	 <li class="active">Content management</li>
		     </ul>
             <%
                if (request.getParameter("message") != null) {
                    out.println("<p>" + request.getParameter("message") + "</p>");
                } 
                if (request.getParameter("delta") != null){
                    out.println("<p>" + request.getParameter("delta") + " new statements</p>");
                }
            %>
			 <ul>
          		<li><a href="import.html">Import Data</a></li>
                <li><a href="export">Export Data</a></li>
          		<li><a href="stats.html">Repository Statistics</a></li>
          		<li><a href="wipeDatabase.html">Clear Repository</a></li>
                <li><a href="deleteGraph.html">Delete a named graph</a></li>
          		<li><a href="sparqlPage.html">SPARQL Query</a></li>
          	</ul>
		</div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>