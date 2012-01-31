<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Admin</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
          <h1>Admin</h1>
        </div>
        <div class="row">
          <div class="span10">
            
    		<ul>
		   		<li><a href="oreadmin/">Content Management</a></li>
		   		<li><a href="emmet/index.html">User Management</a></li>
		   	</ul>
          </div>
          <%@ include file="sidebar.jsp" %>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>