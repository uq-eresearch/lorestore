<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
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
				 <li><a href="../oreadmin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Import data</li>
		     </ul>

  
		  	<form:form modelAttribute="uploadItem" method="post" enctype="multipart/form-data">
		  		<fieldset>
		  			<table>
	            	<tr>
	                    <td><form:label for="filetype" path="name">Name</form:label></td>
	                    <td><form:input path="name"/></td>
	                </tr>
		  				
		  			<tr>
		                <td><form:label for="fileData" path="fileData">File</form:label></td>
		                <td><form:input path="fileData" type="file"/></td>
		            </tr>
		            <tr>
		            	<td colspan="2"><input class="btn primary" type="submit"/></td>
		            </tr>
		           </table>
		  		</fieldset>
		  	</form:form>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>