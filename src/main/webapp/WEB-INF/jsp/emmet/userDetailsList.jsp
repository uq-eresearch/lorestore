<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>User Detail Summary</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
    <%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>User Detail Summary</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="${secure}/emmet/index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">Account detail summary</li>
		     </ul>
			 
          <table class="ud0">
            <thead>
                <tr class="tr3">
                    <th>UserName</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Activated</th>
                    <th>Locked</th>
                    <th colspan="4"></th>
                </tr>
            </thead>
            <% int n = 0; %>
            <c:forEach items="${userDetailsList}" var="userDetails">
                <tr class="tr<%= ++n%2 %>">
                    <td class="td1 td2">${userDetails.user.userName}</td>
                    <td class="td1 td2">${userDetails.user.firstName}</td>
                    <td class="td1 td2">${userDetails.user.lastName}</td>
                    <td class="td1 td2">${userDetails.user.email}</td>
                    <td class="td1">${userDetails.user.activated}</td>
                    <td class="td1">${userDetails.user.locked}</td>
                    <td>
                    <form action="${emmet}" method="get">
                        <input type="hidden" name="action" value="showUserDetails"> 
                        <input type="hidden" name="userName" value="${userDetails.user.userName}">
                        <button type="submit" class="btn">View details</button>
                    </form>
                    </td>
                    <td>
                        <form action="${emmet}" method="post">
                            <input type="hidden" name="action" value="removeUser"> 
                            <input type="hidden" name="userName" value="${userDetails.user.userName}">
                            <button type="submit" class="btn btn-danger">Remove</button>
                        </form>
                    </td>
                    <td>
                    <c:choose>
                        <c:when test="${userDetails.user.locked}">
                            <form action="${emmet}" method="post">
                                <input type="hidden" name="action" value="lockUser"> 
                                <input type="hidden" name="userName" value="${userDetails.user.userName}">
                                <input type="hidden" name="locked" value="no">
                                <button type="submit" class="btn">Unlock</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${emmet}" method="post">
                                <input type="hidden" name="action" value="lockUser"> 
                                <input type="hidden" name="userName" value="${userDetails.user.userName}">
                                <input type="hidden" name="locked" value="yes">
                                <button type="submit" class="btn">Lock</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                    </td>
                    <c:if test="${! userDetails.user.activated}">
                        <td>
                            <form action="${emmet}" method="post">
                                <input type="hidden" name="action" value="activateUser"> 
                                <input type="hidden" name="userName" value="${userDetails.user.userName}">
                                <input type="hidden" name="activated" value="yes">
                                <button type="submit" class="btn">Activate</button>
                            </form>
                        </td>
                    </c:if>
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




