<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Account Management</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header main-page-header">
          <h1>My Account</h1>
        </div>
        <div class="row">
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
			 	 <li class="active">Account</li>
		     </ul>
		     <p>You are logged in as <security:authentication property="principal.username"/></p>
		      <ul>
		        <li><a href="changeMyPassword.html">Change your password</a></li>
		        <li><a href="${emmet}?action=showUserDetails">View your account details</a></li>
		        <li><a href="editMyProfile.html">Edit profile properties</a></li>
		        <li><a href="credentials.html">View technical acccount credentials</a></li>
		      </ul>
		</div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>