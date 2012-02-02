<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file="header.jsp" %>
<title>${sitename} User Account Self-Registration</title>
  </head>
  <body>  
  <%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
	    <h1>Account Registration</h1>
        </div>
        <div class="row">
          <div class="span10">
	     <ul class="breadcrumb">
	     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
		 <li class="active">Account registration</li>
	     </ul>
             <div class="mn1">
              <p>To register for a ${sitename} account, fill in all of the details below
                 and click "Register Account".  An account activation link and instructions for 
		 setting your password will be sent to you via email.</p>

            
              <form action="${emmet}" method="post">
              <input type="hidden" name="action" value="selfRegister">
              <c:if test="${! empty recaptchaKey}">
                  <input type="hidden" name="recaptcha_challenge_field" 
                         value="${recaptchaChallenge}">
                  <input type="hidden" name="recaptcha_response_field" 
                         value="${recaptchaResponse}">
              </c:if>
              <br>&nbsp;
              <table>
              <tr>
                    <td>Desired username</td>
                    <td><input type="text" name="userName"></td>
                </tr>
                <tr>
                    <td>First name</td>
                    <td><input type="text" name="firstName"></td>
                </tr>
                <tr>
                    <td>Last name</td>
                    <td><input type="text" name="lastName"></td>
                </tr>
                <tr>
                    <td>Email address</td>
                    <td><input type="text" name="email"></td>
                </tr>
                <tr>
			<td class="bt0" colspan="2">
            		<button class="btn btn-primary" type="submit">Register Account</button>
                    <a href="index.html"><button class="btn" type="button">Cancel</button></a>

                    </td>
                </tr>
              </table>
              </form>
             </div>
          </div>

      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
