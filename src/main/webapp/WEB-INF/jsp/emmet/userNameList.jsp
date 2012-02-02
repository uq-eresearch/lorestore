<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} User Index</title> 
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
  	  <h1>User Index</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="${secure}/emmet/index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">User account index</li>
		     </ul>
		
              <table class="table">
                <% int n = 0; %>
            	<c:forEach items="${userNameList}" var="userName">
            		<tr class="tr<%= ++n%2 %>">
            			<td class="td1 td2">${userName}</td>
            			<td class="td2 td2">
            			<form action="${emmet}" method="get">
            			    <input type="hidden" name="action" value="showUserDetails"> 
            			    <input type="hidden" name="userName" value="${userName}">
            			    <button class="btn" type="submit">View details</button>
            			</form>
            			</td>
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
