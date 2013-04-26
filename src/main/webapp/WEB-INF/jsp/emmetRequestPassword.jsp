<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
     <title>${sitename} - Operation Requires Password</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
      <div class="page-header">
          <h1>Operation Requires Password</h1>
        </div>
        <div class="row">
          <div class="span10">
    		<form action="${originalUrl}" method="post">
             <chico:hiddenInputs map="${pageContext.request.parameterMap}" exclude="oldPassword"/>
             <table class="table">
              <tr>
                <td colspan="2">${message}</td>
              </tr>
              <tr>
                <td>Password:</td>
                <td><input type="password" name="oldPassword"></input></td>
                <td>
                  <button class="btn btn-primary" type="submit">Continue</button>
				  <a href="index.html"><button class="btn" type="button">Cancel</button></a>
                </td>
              </tr>
             </table>
             </form>
		  </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>