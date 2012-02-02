<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<c:set var="timeout" value='${props["emmet.reset.timeout.minutes"]}' />
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>${sitename} Reset User Password</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Reset User Password</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="index.html">User management</a> <span class="divider">/</span></li>
			 	 <li class="active">Reset user password</li>
		     </ul>
			 
              
            
          <p>When you click the button below, a one-time link will be emailed
             to the user's registered email address, allowing them to enter a new 
             password.  <!--The link will be valid for ${timeout} minutes.--></p>
          <form action="${emmet}" method="post">
          <input type="hidden" name="action" value="initiatePasswordReset">
          <table class="table">
        	<tr>
                <td>User identifier</td>
                <td><input type="text" name="userName"></td>
            </tr>
            <tr>
        		<td colspan="2" class="bt0">
            					<button class="btn btn-primary" type="submit">Reset Password</button>
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


