<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/emmetInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
 	<title>${sitename} - Captcha</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
    	
      <div class="content">
      <div class="page-header">
          <h1>${sitename} - Captcha</h1>
        </div>
        <div class="row">
          <div class="span10">
            
    		 <form action="${originalUrl}" method="post">
			 <chico:hiddenInputs map="${pageContext.request.parameterMap}" exclude="recaptcha.*"/>
			 <table>
			  <tr>
			    <td colspan="2">${message}</td>
			  </tr>
			  <tr>
			    <td colspan="2">
			    <script type="text/javascript"
			            src="http://api.recaptcha.net/challenge?k=${recaptchaKey}">
			    </script>
			    <noscript>
			      <iframe src="http://api.recaptcha.net/noscript?k=${recaptchaKey}"
			              height="300" width="500" frameborder="0"></iframe><br>
			      <textarea name="recaptcha_challenge_field" rows="3" cols="40">
			      </textarea>
			      <input type="hidden" name="recaptcha_response_field" 
			             value="manual_challenge">
			    </noscript>
			    </td>
			  </tr>
			  <tr>
			    <td>Please complete the challenge above and click 'Continue'</td>
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


  
 


