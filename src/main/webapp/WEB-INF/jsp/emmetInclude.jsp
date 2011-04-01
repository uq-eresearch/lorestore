<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<c:set var="home" value='${props["emmet.home.url"]}'/>
<c:set var="sitename" value='${props["emmet.site.title"]}'/>
<c:set var="styles" value='${props["emmet.styles"]}'/>
<c:set var="images" value='${props["emmet.images"]}'/>
<c:set var="container" value='${props["emmet.container"]}'/>
<c:set var="sitecontainer" value='${props["site.container"]}'/>
<c:set var="emmet" value='${props["emmet.svc.url"]}' />

