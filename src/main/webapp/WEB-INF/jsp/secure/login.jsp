<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <%@include file="/WEB-INF/jsp/secure/loginCommonHead.jsp" %>
  </head>
<body style="padding-top:0" class='opaque' onload='document.f.j_username.focus(); addRedirect();'>
<div style="width:400px" class="container">
<div style="padding-top:0" class="content">
<div class="span6">
<h3>Sign in</h3>
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
<form name='f' action='${sitecontainer}/j_spring_security_check' method='POST'>
 <table>
    <tr><td>User:</td><td><input class="em-input" type='text' name='j_username' value='' size='40'></td></tr>
    <tr><td>Password:</td><td><input class="em-input" type='password' name='j_password' size='40'/></td></tr>
    <tr><td><input type='checkbox' name='_spring_security_remember_me'/></td>
        <td>Remember me on this computer.</td></tr>
    <tr><td colspan='2'>
        <button class="btn btn-primary" name="submit" type="submit">Sign in</button>
        <button class="btn" name="reset" type="reset">Clear</button></td></tr>
  </table>
</form>
<%@ include file="/WEB-INF/jsp/secure/siteLoginFootMessage.jsp" %>
</div>
</div>
</div>
</body>
</html>
