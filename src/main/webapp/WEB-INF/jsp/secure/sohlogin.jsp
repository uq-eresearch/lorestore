<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <%@include file="/WEB-INF/jsp/secure/loginCommonHead.jsp" %>
  </head>
<body class='opaque' onload='document.f.j_username.focus(); addRedirect(); '>
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
<h3>Login with OpenID Identity</h3>
<p>You can also login using your credentials with a remote OpenID service.  Either
   click one of the common provider icons, or enter a provider URL or your 
   personal OpenID URL into the type-in the "OpenID URL" textbox.  When you 
   click 'login', you browser will be redirected to the OpenID service to check 
   your credentials, and to ask for your permission to use your identity in 
   conjunction with this site.</p>
<form name='oidf' action='${sitecontainer}/j_spring_openid_security_check' method='POST'>
 <img style='vertical-align: middle' src='${images}/google.png' alt='use Google'
      onclick='setOpenIDUrl("https://www.google.com/accounts/o8/id")'/>
 <img style='vertical-align: middle' src='${images}/yahoo.png' alt='use Yahoo'
      onclick='setOpenIDUrl("https://www.yahoo.com")'/>
 <img style='vertical-align: middle' src='${images}/myspace.png' alt='use MySpace'
      onclick='setOpenIDUrl("https://www.myspace.com")'/>
 <img style='vertical-align: middle' src='${images}/flickr.png' alt='use Flickr'
      onclick='setOpenIDUrl("https://www.flickr.com")'/>
 <table>
    <tr><td>OpenID URL:</td><td><input id='openid-url' type='text' name='openid_identifier' size='40'/></td></tr>
    <tr><td><input type='checkbox' name='_spring_security_remember_me'></td>
        <td>Remember me on this computer.</td></tr>
    <tr><td colspan='2'>
        <button name="submit" type="submit">Login</button>
        <button name="reset" type="reset">Reset</button></td></tr>
  </table>
</form>
<%@ include file="/WEB-INF/jsp/secure/siteLoginFootMessage.jsp" %>
</div>
</body>
</html>
