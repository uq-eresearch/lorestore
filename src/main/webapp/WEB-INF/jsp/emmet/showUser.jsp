<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
<title>Show ${sitename} User Details</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Show User Details</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="index.html">User management</a> <span class="divider">/</span></li>
			 	 <li class="active">Show user details</li>
		     </ul>
              <form action="${emmet}" method="get">
              <input type="hidden" name="action" value="showUserDetails">
              <table>
            	<tr>
                    <td>User identifier</td>
                    <td><input type="text" name="userName"></td>
                </tr>
                <tr>
            		<td colspan="2" class="bt0">
            		<button class="btn primary" type="submit">Show User Details</button>
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

