<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>LoreStore Server</title>
  </head>
  <body>
  	<h1>LoreStore Wipe</h1>
  	<form method="post" enctype="multipart/form-data">
  		<fieldset>
  			<legend>Wipe Triplestore</legend>
  			
  			<p>
  				This will wipe all data from the LoreStore!
            </p>
            
            <p>
            	<input type="submit"/>
            </p>
  		</fieldset>
  	</form>
  </body>
</html>