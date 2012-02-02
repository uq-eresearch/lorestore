<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="userName" value='${props["userName"]}' />
<c:set var="resetCode" value='${props["resetCode"]}' />
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Reset Password</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
         <h1>Reset Password</h1>
        </div>
        <div class="row">
          <div class="span10">
            
		 <p>You have been directed to this page because you 
		     have initiated a password reset on your account.  If you wish to continue
		     the password reset process, enter your new password below and click
		     the "Reset Password" button.  If you cancel or navigate away without
		     clicking "Reset Password" your current password will be left unaltered.</p>
		
		  <form action="${emmet}" method="post">
		  <input type="hidden" name="action" value="doResetPassword2">
		  <input type="hidden" name="userName" value="${userName}">
		  <input type="hidden" name="resetCode" value="${resetCode}">
		  <br>&nbsp;
		  <table class="table">
			<tr>
		        <td>New password</td>
		        <td><input type="password" name="newPassword"></td>
		    </tr>
		    <tr>
		        <td>New password (again)</td>
		        <td><input type="password" name="newPassword2"></td>
		    </tr>
		    <tr>
				<td class="bt0" colspan="2">
				<button class="btn btn-primary" type="submit">Reset Password</button>
		        <a href="index.html"><button class="btn" type="button">Cancel</button></a>
                    
		        </td>
		    </tr>
		  </table>
		  </form>
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>


  
 