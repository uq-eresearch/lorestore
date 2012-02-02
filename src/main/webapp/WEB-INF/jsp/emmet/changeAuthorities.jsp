<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Change User Authorities</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Change User Authorities</h1>
        </div>
        <div class="row">
        
          <div class="span10">
	          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="index.html">User account management</a> <span class="divider">/</span></li>
			 	 <li class="active">Change authorities</li>
		     </ul>
			 <p>Change the authorities for a user account</p>
			 <p>Example authorities: {ROLE_USER}, {ROLE_ORE}, {ROLE_ANNOTATOR}</p>
          <form action="${emmet}" method="post">
          <input type="hidden" name="action" value="changeAuthorities">
          <table>
        	<tr>
                <td>User identifier</td>
                <td><input type="text" name="userName"></td>
            </tr>
            <tr>
                <td>Authorities</td>
                <td><input type="text" name="roles"></td>
            </tr>
            <tr>
        		<td colspan="2" class="bt0">
					<button class="btn btn-primary" type="submit">Update</button>
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


  
