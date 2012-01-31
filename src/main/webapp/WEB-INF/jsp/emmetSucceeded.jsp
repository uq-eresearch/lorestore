<%@ include file="/WEB-INF/jsp/emmetInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>${sitename} User Account Operation Succeeded</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
          <h1>User Account Operation Succeeded</h1>
        </div>
        <div class="row">
          <div class="span10">
            
    		<p>${message}</p>
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
