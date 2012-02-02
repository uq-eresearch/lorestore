<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
<title>${sitename} Change User Details</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Change User Details</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">Change details</li>
		     </ul>
			 
              
 
             <p>
                This form allows you to change the user names and/or email address 
                associated with an account.  (Fields that are not filled in will
                not be updated.)
              </p>
            
              <form action="${emmet}" method="post">
              <input type="hidden" name="action" value="changeUserDetails">
              <table class="table">
            	<tr>
                    <td>User identifier</td>
                    <td><input type="text" name="userName"></td>
                </tr>
                <tr>
                    <td>First name</td>
                    <td><input type="text" name="firstName"></td>
                </tr>
                <tr>
                    <td>Last name</td>
                    <td><input type="text" name="lastName"></td>
                </tr>
                <tr>
                    <td>Email address</td>
                    <td><input type="text" name="email"></td>
                </tr>
                <tr>
            		<td colspan="2" class="bt0">
						<button class="btn btn-primary" type="submit">Update Details</button>
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