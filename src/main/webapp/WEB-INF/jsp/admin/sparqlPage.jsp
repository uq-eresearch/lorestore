<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>SPARQL Query</title>
  </head>
  <body>
  	<h1>Test Queries</h1>
  	<form method="get">
  		<fieldset>
  			<legend>Refers To</legend>
  			
  			<p>
  				<label for="sparql" path="name">Sparql Query:</label>
  				<textarea rows="15" cols="100" name="sparql"></textarea>
  			</p>
  			
            <p>
            	<input type="submit"/>
            </p>
  		</fieldset>
  	</form>
  	
  </body>
</html>