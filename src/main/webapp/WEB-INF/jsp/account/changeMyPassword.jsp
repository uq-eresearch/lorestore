<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Change Password</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
      
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Change Password</h1>
        </div>
        <div class="row">
          <div class="span10">
          	 <ul class="breadcrumb">
		  	   	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
		     	 <li><a href="index.html">Account</a> <span class="divider">/</span></li>
			 	 <li class="active">Change password</li>
		     </ul>


  <p>To change your password, enter the new password in the fields below.
     Your current password is required as an additional security measure.</p>
  <form action="${emmet}" method="post">
  <input type="hidden" name="action" value="changePassword">
  <table class="table">
	<tr>
        <td>Current password</td>
        <td><input type="password" name="oldPassword"></td>
    </tr>
    <tr>
        <td>New password</td>
        <td><input type="password" name="newPassword"></td>
    </tr>
    <tr>
        <td>New password (again)</td>
        <td><input type="password" name="newPassword2"></td>
    </tr>
    <tr>
		<td colspan="2" class="bt0">
		<button class="btn btn-primary" type="submit">Change Password</button>
        <a href="index.html"><button class="btn" type="button">Cancel</button></a>
		        
        </td>
    </tr>
  </table>
  </form>
 </div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>