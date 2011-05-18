<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>LoreStore Server</title>
  </head>
  <body>
  	<h1>LoreStore Import</h1>
  	<form:form modelAttribute="uploadItem" method="post" enctype="multipart/form-data">
  		<fieldset>
  			<legend>Import File</legend>
  			
  			<p>
  				<form:label for="filetype" path="name">Name</form:label>
  				<form:input path="name"/>
  			</p>
  			
	        <p>
                <form:label for="fileData" path="fileData">File</form:label><br/>
                <form:input path="fileData" type="file"/>
            </p>
            
            <p>
            	<input type="submit"/>
            </p>
  		</fieldset>
  	</form:form>
  </body>
</html>