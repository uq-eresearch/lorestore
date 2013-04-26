<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="au.edu.diasb.emmet.model.*, org.springframework.security.core.*" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Credentials</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
      
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Credentials</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
		     	 <li><a href="index.html">Account</a> <span class="divider">/</span></li>
			 	 <li class="active">Credentials</li>
		     </ul>
            <strong id="about">Hello <security:authentication property="name"/></strong>
            <c:choose>
        <c:when test="${ empty pageContext.request.userPrincipal }">
          <p>No authentication details are present.</p>
        </c:when>
        <c:otherwise>
          <p>You are 
          
            <security:authorize ifAllGranted="ROLE_ANONYMOUS">NOT</security:authorize> logged in.
            <security:authorize ifAllGranted="ROLE_ADMIN">You have administrator rights.</security:authorize>
          </p>
          <p>Your principal object is: <%= request.getUserPrincipal() %></p>
          
          <emmet:accountExpiry/>
          <emmet:passwordExpiry/>
          <c:if test="${! empty accountExpiry}"></p>
            <p>Your account is due to expire ${accountExpiry}.</p>
          </c:if>
          <c:if test="${! empty passwordExpiry}">
            <p>Your current password is due to expire ${passwordExpiry}.</p>
          </c:if>
        </c:otherwise>
      </c:choose>
      </div>
      </div>
    </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>




    
