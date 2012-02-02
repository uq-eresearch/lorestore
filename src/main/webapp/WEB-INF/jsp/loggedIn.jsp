<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page import="au.edu.diasb.emmet.model.*" %>
<%@ page import="au.edu.diasb.emmet.util.*" %>
<%@ page import="org.springframework.security.core.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>${sitename} Signed in</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="row">
          <div class="span10">
            <p>You have successfully signed in.</p>
		    <emmet:accountExpiry/>
		    <emmet:passwordExpiry/>
		    <c:if test="${! empty accountExpiry}">
		     <p>Your account is due to expire ${accountExpiry}.  
		        Please contact the site administrators.</p>
		    </c:if>
		    <c:if test="${! empty passwordExpiry}">
		     <p>Your current password is due to expire ${passwordExpiry}.
		        Please use the "Change My Password" or "Reset My Password" function
		        to set a new password.</p>
		    </c:if>
		    <p>Manage <a href="${secure}/account/index.html">your account</a></p>
          </div>
          
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
