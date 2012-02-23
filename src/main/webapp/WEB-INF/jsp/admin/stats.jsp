<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>lorestore stats</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
  	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Repository Stats</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>				 
				 <li><a href="../oreadmin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Stats</li>
		     </ul>
		     <table class="table">
		     <tr>
		     <td>
			    <h2>Total number of triples: <c:out value="${numTriples}"></c:out></h2>
				<p>Includes Annotations, Resource Maps and the schemas used to validate them</p>
				
		     </td>
		     </tr>
		     <td>
		     	<p>Number of ORE Resource Maps: <c:out value="${numResourceMaps}"></c:out></p>
		     </td>
		     </tr>
		     <tr>
		     <td>
		     	<p>Number of OAC Annotations: <c:out value="${numAnnotations}"></c:out></p>
		     </td>
		     </table>
          </div>
          
 <!-- Debug
      <c:choose>
         <c:when test='${param.scope == "page"}'>
            <c:set var='scope' value='${pageScope}'/>
         </c:when>
         <c:when test='${param.scope == "request"}'>
            <c:set var='scope' value='${requestScope}'/>
         </c:when>
         <c:when test='${param.scope == "session"}'>
            <c:set var='scope' value='${sessionScope}'/>
         </c:when>
         <c:when test='${param.scope == "application"}'>
            <c:set var='scope' value='${applicationScope}'/>
         </c:when>
      </c:choose>

      <font size='5'>
         <c:out value='${param.scope}'/>-scope attributes:
      </font><p>

      <%-- Loop over the JSTL implicit object, stored in the
           page-scoped attribute named scope that was set above.
           That implicit object is a map --%>
      <c:forEach items='${scope}' var='p'>
         <ul>
            <%-- Display the key of the current item, which
                 represents the parameter name --%>
            <li>Parameter Name: <c:out value='${p.key}'/></li>

            <%-- Display the value of the current item, which
                 represents the parameter value --%>
            <li>Parameter Value: <c:out value='${p.value}'/></li>
         </ul>
      </c:forEach>
      -->
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>


  	
