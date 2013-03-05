<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Search</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
      <div class="page-header main-page-header">
          <h1>Search</h1>
        </div>
        <div class="row">
          <div class="span10">

            <h2>Annotations</h2>
        <form action="oa/">
        <fieldset>
            <label for="annoInput">Annotates:&nbsp;</label>
            <input class="xlarge" id="annoInput" name="annotates" size="60" type="text">
            <input type="submit" class="btn btn-primary" value="Search">
        </fieldset>
      </form>
        <form action="oa/">
        <fieldset>
            <label for="annoInput">Keyword:&nbsp;</label>
            <input class="xlarge" id="annoInput" name="matchval" size="60" type="text">
            <input type="hidden" name="asTriples" value="false">
            <input type="submit" class="btn btn-primary" value="Search">
        </fieldset>
      </form>
         <h2>Resource maps</h2>
        <form action="ore/">
        <fieldset>
            <label for="refersToInput">Refers to:&nbsp;</label>
              <input class="xlarge" id="refersToInput" name="refersTo" size="30" type="text">
            <input type="submit" class="btn btn-primary" value="Search">
        </fieldset>
      </form>
      <form action="ore/">
        <fieldset>
            <label for="refersToInput">Keyword:&nbsp;</label>
              <input class="xlarge" id="refersToInput" name="matchval" size="30" type="text">
            <input type="submit" class="btn btn-primary" value="Search">
        </fieldset>
      </form>
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
