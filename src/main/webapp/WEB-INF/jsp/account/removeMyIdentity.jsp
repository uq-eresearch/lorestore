<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Remove Identity</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
      
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Remove Identity</h1>
        </div>
        <div class="row">
          <div class="span10">
          	 <ul class="breadcrumb">
		  	   	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
		     	 <li><a href="index.html">Account</a> <span class="divider">/</span></li>
			 	 <li class="active">Remove identity</li>
		     </ul>
           
           <p>To remove a secondary identity to your account, enter the identity Uri and the domain
		     below and click 'Remove Identity'.  Your password is required as an extra security 
		     measure.</p>
		  <form action="${emmet}" method="post">
		  <input type="hidden" name="action" value="removeIdentity">
		  <table class="table">
			<tr>
		        <td>Current password</td>
		        <td><input type="password" name="oldPassword"></td>
		    </tr>
		    <tr>
		        <td>Identity uri</td>
		        <td><input type="text" name="identityUri"></td>
		    </tr>
		    <tr>
		        <td>Domain</td>
		        <td><select name="domain">
		        <c:forEach items="${props.domainRegistry.domainNamesWithoutPrimary}" var="domain">
		            <option>${domain}</option>
		        </c:forEach>
		        </select></td>
		    </tr>
		    <tr>
				<td colspan="2" class="bt0">
				<button class="btn btn-danger" type="submit">Remove identity</button>
		        <a href="index.html"><button class="btn" type="button">Cancel</button></a>
		        </td>
		    </tr>
		  </table>
		  </form>
        </div>
		
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>
 


  
 
