<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} - lore</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
    <%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>lore</h1>
        </div>
        <div class="row">
          <div class="span10">
            <p>lore is a Firefox extension that supports creation of ORE Resource Maps and Annotations.</p>
            <p><a href="http://itee.uq.edu.au/~eresearch/projects/aus-e-lit/lore.php">Find out more</a></p>
            <p>The current public release of lore on the Firefox Add-Ons site is configured for the older Aus-e-Lit lorestore service, 
                and is not compatible with this version of lorestore. 
                Please contact us if you would like to be involved in beta testing the new version.
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
