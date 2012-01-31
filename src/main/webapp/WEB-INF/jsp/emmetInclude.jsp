<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="chico" uri="http://metadata.net/tags/chico/chicoTags.tld"%>
<%@ taglib prefix="emmet" uri="http://metadata.net/tags/emmet/emmetTags.tld"%>
<c:set var="home" value='${props["emmet.home.url"]}'/>
<c:set var="sitename" value='${props["emmet.site.title"]}'/>
<c:set var="styles" value='${props["emmet.styles"]}'/>
<c:set var="images" value='${props["emmet.images"]}'/>
<c:set var="container" value='${props["emmet.container"]}'/>
<c:set var="sitecontainer" value='${props["site.container"]}'/>
<chico:setSecureUrl var="emmet" url='${props["emmet.svc.url"]}' />
<c:set var="recaptchaKey" value='${props["emmet.recaptcha.publicKey"]}' />
