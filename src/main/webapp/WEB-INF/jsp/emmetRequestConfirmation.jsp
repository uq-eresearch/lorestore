<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file="header.jsp" %>
	<title>${sitename} - Operation Requires Confirmation</title>
  </head>
  <body>  
  <%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
		<h1>Operation Requires Confirmation</h1>
        </div>
        <div class="row">
          <div class="span10">
	     <ul class="breadcrumb">
	     	 <li><a href="index.html">Home</a> <span class="divider">/</span></li>
		 <li class="active">Account registration</li>
	     </ul>
             
         <form action="${originalUrl}" method="post">
         <chico:hiddenInputs map="${pageContext.request.parameterMap}" exclude="confirmed"/>
         <table>
          <tr>
            <td colspan="2">${message}</td>
          </tr>
          <tr>
            <td>
              <button class="btn btn-primary" type="submit" name="confirmed" value="yes">Confirm</button>
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
