<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="userName" value='${props["userName"]}' />
<c:set var="activationCode" value='${props["activationCode"]}' />
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Account Activation</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
         <h1>Account Activation</h1>
        </div>
        <div class="row">
          <div class="span10">
            
		  <p>You have been directed to this page because you 
		     have requested a new ${sitename} account.  If you wish to complete 
		     the account activation process, enter a password below and click
		     the "Activate Account" button.  If you cancel or navigate away without
		     clicking "Activate Account", the account will not be activated.</p>
		
		  <form action="${emmet}" method="post">
		  <input type="hidden" name="action" value="doActivate2">
		  <input type="hidden" name="userName" value="${userName}">
		  <input type="hidden" name="activationCode" value="${activationCode}">
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
		        <button class="btn btn-primary" type="submit">Activate</button>
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

