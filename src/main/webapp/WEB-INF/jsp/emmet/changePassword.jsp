<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>${sitename} Change User Password</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Change User Password</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">Change password</li>
		     </ul>
			 
              
              <form action="${emmet}" method="post">
              <input type="hidden" name="action" value="changePassword">
              <table>
            	<tr>
                    <td>User identifier</td>
                    <td><input type="text" name="userName"></td>
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
                    <td>Password expiry (optional)</td>
                    <td><input type="text" name="passwordExpiry"></td>
                </tr>
                <tr>
            		<td colspan="2" class="bt0">
    					<button class="btn btn-primary" type="submit">Change Password</button>
                        <a href="index.html"><button class="btn" type="button">Cancel</button></a>
                    </td>
                </tr>
              </table>
              </form>
			  <p>If the user is not physically present to enter their new password,  
                 use <a href="resetPassword.html">this form</a> instead.</p>
            
		</div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>



  

