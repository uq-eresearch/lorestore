<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<c:set var="secure" value="${requestScope['javax.servlet.forward.context_path']}"/>
<c:set var="styles" value="${secure}/stylesheets"/>
<c:set var="images" value="${secure}/images"/>
