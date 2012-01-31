<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <%@include file="/WEB-INF/jsp/secure/loginCommonHead.jsp" %>
  </head>
<body class='opaque' onload='document.f.j_username.focus(); addRedirect();'>
<div id="c1">
<h3>Site login</h3>
<emmet:classifyException/>
<c:choose>
  <c:when test="${empty CLASSIFICATION}">
    <%@ include file="/WEB-INF/jsp/secure/siteLoginHeadMessage.jsp" %>
  </c:when>
  <c:when test="${CLASSIFICATION.classification == 'system' }">
    <p>${CLASSIFICATION.description} - ${CLASSIFICATION.remedy}<br>
       ( exception is ${CLASSIFICATION.exception} ) 
    </p>
  </c:when>
  <c:otherwise>
    <p>${CLASSIFICATION.description} - ${CLASSIFICATION.remedy}</p>
  </c:otherwise>
</c:choose>
<h3>Login with Username and Password</h3>
<form name='f' action='${sitecontainer}/j_spring_security_check' method='POST'>
 <table>
    <tr><td>User:</td><td><input class="em-input" type='text' name='j_username' value='' size='40'></td></tr>
    <tr><td>Password:</td><td><input class="em-input" type='password' name='j_password' size='40'/></td></tr>
    <tr><td><input type='checkbox' name='_spring_security_remember_me'/></td>
        <td>Remember me on this computer.</td></tr>
    <tr><td colspan='2'>
        <button name="submit" type="submit">Login</button>
        <button name="reset" type="reset">Reset</button></td></tr>
  </table>
</form>
<h3>Login using Shibboleth</h3>
<p>You can also login using the configured Shibboleth Identity Provider service.
   When you click 'login', you browser will be redirected to the IdP service to 
   check your credentials and establish a Shibboleth session.</p>
<form name='oidf' action='${sitecontainer}/j_spring_shibboleth_security_check' method='POST'>
<button name="submit" type="submit">Login</button>
</form>
<%@ include file="/WEB-INF/jsp/secure/siteLoginFootMessage.jsp" %>
</div>
</body>
</html>
