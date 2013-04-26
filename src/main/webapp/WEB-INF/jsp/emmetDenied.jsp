<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Access Denied</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
          <h1>Access Denied</h1>
        </div>
        <div class="row">
          <div class="span10">
    		<p>You do not have the access rights needed to perform the requested operation.
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
