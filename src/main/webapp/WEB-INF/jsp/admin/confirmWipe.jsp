<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Clear Repository</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
  </head>
  <body>  
  	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Clear Repository</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="../admin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Clear repository</li>
		     </ul>

  	<form method="post" enctype="multipart/form-data">
  		<fieldset>
  			<p>
  				Please confirm that you wish to remove all data from the LoreStore repository.
  				<br/><strong>This action cannot be undone.</strong> This will also clear the schema used for validation. 
  				<br/>Reload the webapp after clearing the repository to automatically reload the schema, 
  				 or import the schema manually from file.
            </p>
            
            <p>
            	<button class="btn btn-danger" type="submit"><i class="icon-trash icon-white"></i> Clear Repository</button>
                <a href="../admin/"><button class="btn" type="button">Cancel</button></a>
            </p>
  		</fieldset>
  	</form>
  </div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>