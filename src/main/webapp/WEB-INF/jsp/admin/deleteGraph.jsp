<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>lorestore delete named graph</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
    <script type="text/javascript" src="${secure}/bootstrap-alert.js"></script>
    <script type="text/javascript">
    function deleteGraph() {
        var uri = jQuery('#deleteID').val();
        if (uri){
            jQuery.ajax({
                url: uri,
                type: 'DELETE',
                context: document.body,
                success: function(){
                    jQuery('#deletedURI').text(uri);
                    jQuery('#successMessage').css('display','block');
                },
                error: function(jqXHR, textStatus, errorThrown){
                    jQuery('#failMessage').html("<span class='label label-important'>" + textStatus + "</span> " + errorThrown);
                    jQuery('#failureMessage').css('display','block');
                }
            });
        } else {
         jQuery('#emptyURIMessage').css('display','block');
        }
    }
    </script>
  </head>
  <body>  
  	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Delete named graph</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="../oreadmin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Delete named graph</li>
		     </ul>
             <div id="emptyURIMessage" style="display:none" class="alert">
              <a data-dismiss="alert" href="#" class="close">x</a>
              Please enter the URI of the named graph to be deleted
            </div>
            <div id="successMessage" style="display:none" class="alert alert-success">
              <a href="#" data-dismiss="alert"class="close">x</a>
              <span class="label label-success">Success</span> Named graph <span id="deletedURI"></span> has been deleted
            </div>
            <div id="failureMessage" style="display:none" class="alert alert-error">
              <a href="#" data-dismiss="alert" class="close">x</a>
              <span id="failMessage"></span>
            </div>
            <table class="table">
                <tr>
                    <td>Graph identifier</td>
                    <td><input id="deleteID" type="text"></td>
                </tr>
                <tr>
                    <td colspan="2" class="bt0">
                    <button class="btn btn-danger" onclick="deleteGraph()"><i class="icon-trash icon-white"></i> Delete</button>
                    <a href="../oreadmin/"><button class="btn" type="button">Cancel</button></a>
                    </td>
                </tr>
            </table>
      </div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>