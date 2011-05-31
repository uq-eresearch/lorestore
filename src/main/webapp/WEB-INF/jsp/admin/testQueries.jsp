<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>LoreStore Server</title>
  </head>
  <body>
  	<h1>Test Queries</h1>
  	<form action="../ method="get">
  		<fieldset>
  			<legend>Refers To</legend>
  			
  			<p>
  				<label for="refersTo" path="name">Refers To:</label>
  				<input type="text" name="refersTo"/>
  			</p>
  			
            <p>
            	<input type="submit"/>
            </p>
  		</fieldset>
  	</form>
  	
	<form action="../ method="get">
  		<fieldset>
	        <p>
  				<label for="refersTo" path="name">Keyword:</label>
  				<input type="text" name="refersTo"/>
            </p>
            
  </body>
</html>