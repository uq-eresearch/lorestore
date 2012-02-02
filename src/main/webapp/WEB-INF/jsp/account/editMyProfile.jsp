<%@ include file="/WEB-INF/jsp/emmetInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="au.edu.diasb.emmet.model.*, org.springframework.security.core.*" %>
<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Edit Profile</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
      
	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Edit Profile Properties</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
		     	 <li><a href="index.html">Account</a> <span class="divider">/</span></li>
			 	 <li class="active">Edit Profile</li>
		     </ul>
  <p>To set a profile property, enter the property name and value,
     and then click the button below. </p>
  <form action="${emmet}" method="post">
  <table>
	<tr>
        <td>Profile property</td>
        <td>
            <select name="propertyName">
                <c:forEach items="${props.profileSchema.descriptors}" var="desc">
                    <option value="${desc.propName}" label="${desc.readableName}">${desc.description}</option>
                </c:forEach>
            </select>
        </td>
    </tr>
    <tr>
        <td>New property value</td>
        <td><input type="text" name="propertyValue"></td>
    </tr>
    <tr>
		<td colspan="2" class="bt0">
		<button class="btn btn-primary" type="submit">Set Property</button>
        <a href="index.html"><button class="btn" type="button">Cancel</button></a>
        
        </td>
    </tr>
  </table>
  </form>
  </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>

