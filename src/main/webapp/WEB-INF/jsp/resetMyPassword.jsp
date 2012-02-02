<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Reset ${sitename} Password</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Reset Password</h1>
        </div>
        <div class="row">
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
				
			 	 <li class="active">Reset password</li>
		     </ul>
		      <p>When you click the button below, a one-time link will be sent
                 to your registered email address, allowing you to enter a new 
                 password. If you do not remember your username or registered email address,
				 please contact an administrator.
			  </p>
              <form action="${emmet}" method="post">
              <input type="hidden" name="action" value="initiateMyPasswordReset">
              <br>&nbsp;
              <table class="table">
            	<tr>
                    <td>Enter your user name or registered email address</td>
                    <td><input type="text" name="userNameOrEmail"></td>
                </tr>
                <tr>
                    <td class="bt0" colspan="2">
                    <button class="btn btn-primary" type="submit">Reset</button>
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
