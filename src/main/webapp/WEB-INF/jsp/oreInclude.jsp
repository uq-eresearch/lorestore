<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="chico" uri="http://metadata.net/tags/chico/chicoTags.tld"%>
<c:set var="home" value='${props["emmet.home.url"]}'/>
<chico:setSecureUrl var="home" url="${props['lorestore.home.url']}"/>
<chico:setSecureUrl var="secure" url="${props['lorestore.base.url']}"/>
<chico:setSecureUrl var="emmet" url="${props['emmet.svc.url']}"/>
<c:set var="styles" value='${props["lorestore.styles"]}'/>
<c:set var="images" value='${props["lorestore.images"]}'/>
<c:set var="container" value='${props["lorestore.container"]}'/>
<c:set var="base" value='${props["lorestore.base.url"]}'/>




