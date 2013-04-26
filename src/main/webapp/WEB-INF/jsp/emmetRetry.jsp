<%@ include file="/WEB-INF/jsp/emmetInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title>${sitename} User Account Operation - Input Error</title>
    <%@ include file="header.jsp" %>
    
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
         <h1>User Account Operation - Input Error</h1>
        </div>
        <div class="row">
          <div class="span10">
        <p>There is a problem with the value in the ${parameterName} form field.
         </p>
         <p>${message}
         </p>
         <table class="table">
          <tr>
            <td><button class="btn btn-primary" onclick="doTryAgain();">Try again</button></td>
          </tr>
         </table>
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
      <script type="text/javascript">
        function doTryAgain() {
            window.history.back();
        };
      </script>
    </div>
  </body>
</body>
</html>