<%@ include file="/WEB-INF/jsp/oreInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>lorestore import</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
  	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Import Data</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="../admin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Import bulk data</li>
		     </ul>
            <%
                if (request.getParameter("mymessage") != null) {
                    out.println("<p>" + request.getParameter("mymessage") + "</p>");
                } 
            %>
            <p>This function is provided to enable restoring the state of the underlying repository from data previously exported from lorestore. 
                <br/>Data will be imported as-is directly to the triplestore and should be provided in a format that supports Named Graphs, such as TriG.
                <br/>Imported data adds to, but does not overwrite existing named graphs : to bulk replace existing objects, delete those objects from the repository first.
                The file extension of the uploaded file must match the data format (e.g. <em>.trig</em> for TriG files).
            </p>
                
		  	<form:form modelAttribute="uploadItem" method="post" enctype="multipart/form-data">
		  		<fieldset>
		  			<table class="table">	
		  			<tr>
		                <td><form:label for="fileData" path="fileData">File</form:label></td>
		                <td><form:input path="fileData" type="file"/></td>
		            </tr>
		            <tr>
		            	<td colspan="2"><button class="btn btn-primary" type="submit">Import</button></td>
		            </tr>
		           </table>
		  		</fieldset>
		  	</form:form>
      </div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>