<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>User Account Management Tools</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>User Account Management Tools</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
			 	 <li class="active">User account management</li>
		     </ul>
              <ul>
                <li><a href="addUser.html">Create a new user account</a></li>
                <li><a href="changePassword.html">Change an account's password</a></li>
                <li><a href="changeUserDetails.html">Change an account's details</a></li>
                <li><a href="changeAuthorities.html">Change an account's authorities</a></li>
                <li><a href="resetPassword.html">Reset a user account password</a></li>
                <li><a href="showUser.html">Show details for a specific account</a></li>
                <li><a href="${emmet}?action=listUserNames&orderBy=userName">List all user account names</a></li>
                <li><a href="${emmet}?action=listUserDetails&orderBy=userName">List all user acount details</a></li>
              </ul>
              
		</div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>

