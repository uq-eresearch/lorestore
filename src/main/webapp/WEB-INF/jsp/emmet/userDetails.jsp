<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>User Details</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>User Details</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="${secure}/emmet/index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">Account details</li>
		     </ul>
              <table class="table">
            	<tr class="tr1">
                    <td class="ud1">User identifier</td>
                    <td colspan="2" class="ud2">${userDetails.user.userName}</td>
                </tr>
                <tr class="tr0">
                    <td class="ud1">First name</td>
                    <td colspan="2" class="ud2">${userDetails.user.firstName}</td>
                </tr>
                <tr class="tr1">
                    <td class="ud1">Last name</td>
                    <td colspan="2" class="ud2">${userDetails.user.lastName}</td>
                </tr>
                <tr class="tr0">
                    <td class="ud1">Email address</td>
                    <td colspan="2" class="ud2">${userDetails.user.email}</td>
                </tr>
                <tr class="tr1">
                    <td class="ud1">Activated</td>
                    <td colspan="2" class="ud2">${userDetails.user.activated}</td>
                </tr>
                <tr class="tr0">
                    <td class="ud1">Locked</td>
                    <td colspan="2" class="ud2">${userDetails.user.locked}</td>
                </tr>
                <tr class="tr1">
                    <td class="ud1">Account creation</td>
                    <td colspan="2" class="ud2">${userDetails.user.created}</td>
                </tr>
                <tr class="tr1">
                    <td class="ud1">Account expiry</td>
                    <td colspan="2" class="ud2">${userDetails.user.expiry}</td>
                </tr>
                <c:if test="${ ! empty userDetails.user.passwordExpiry }">
                    <tr class="tr1">
                        <td class="ud1">Password expiry</td>
                        <td colspan="2" class="ud2">${userDetails.user.passwordExpiry}</td>
                    </tr>
                </c:if>
                <c:forEach items="${userDetails.identities}" var="identity">
                    <tr class="tr0">
                        <td class="ud1">Identity - ${identity.domain}</td>
                        <td class="ud2">${identity.identityUri}</td>
                        <td>
                            <form action="${emmet}" method="post">
                                <input type="hidden" name="action" value="removeIdentity"> 
                                <input type="hidden" name="userName" value="${userDetails.user.userName}">
                                <input type="hidden" name="domain" value="${identity.domain}">
                                <input type="hidden" name="identityUri" value="${identity.identityUri}">
                                <c:choose>
                                  <c:when test="${identity.domain == 'primary'}">
                                    <button disabled="disabled" type="submit" class="btn btn-danger"><i class="icon-trash icon-white"></i> Remove</button>
                                  </c:when>
                                  <c:otherwise>
                                    <button type="submit" class="btn btn-danger"><i class="icon-trash icon-white"></i> Remove</button>
                                  </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <tr class="tr1">
                    <td class="ud1">Roles</td>
                    <td colspan="2" class="ud2">
                        <c:forEach items="${userDetails.user.authorities}" var="authority">
                            ${authority.authority}
                        </c:forEach>
                    </td>
                </tr>
                <c:forEach items="${userDetails.profile}" var="entry">
                    <tr class="tr0">
                        <td class="ud1">Property - ${entry.key}</td>
                        <td class="ud2" colspan="2">${entry.value}</td>
                    </tr>
                </c:forEach>
              </table>
              
		</div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>


