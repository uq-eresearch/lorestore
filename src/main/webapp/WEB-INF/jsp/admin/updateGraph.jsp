<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>lorestore update named graph</title>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
    <script type="text/javascript" src="${secure}/bootstrap-alert.js"></script>
    <script type="text/javascript">
    var lorestore={};
    lorestore.init = function(){
        lorestore.cm = CodeMirror.fromTextArea(document.getElementById("rdfeditor"), {
            mode: {name: "xml", alignCDATA: true},         
            lineNumbers: true
        }); 
    };
    lorestore.loadGraph = function() {
        var uri = jQuery('#updateID').val();
        if (uri){
        jQuery.ajax({
                url: uri,
                context: document.body,
                success: function(data, status, xhr){
                    lorestore.cm.setValue(xhr.responseText);
                    jQuery('#updateID').prop('disabled',true);
                },
                error: function(jqXHR, textStatus, errorThrown){
                    jQuery('#failMessage').html("<span class='label label-important'>" + textStatus + "</span> " + errorThrown);
                    jQuery('#failureMessage').css('display','block');
                }
            });
        } else {
         jQuery('#emptyURIMessage').css('display','block');
        }
    };
    lorestore.updateGraph = function(){
        var uri = jQuery('#updateID').val();
        if (uri){
            jQuery.ajax({
                url: uri,
                type: 'PUT',
                data: cm.getValue(),
                context: document.body,
                success: function(data, status, xhr){
                    jQuery('#successMessage').html("success");
                    jQuery('#successMessage').css('display','block');
                    
                },
                error: function(jqXHR, textStatus, errorThrown){
                    jQuery('#failureMessage').html("<span class='label label-important'>" + textStatus + "</span> " + errorThrown);
                    jQuery('#failureMessage').css('display','block');
                }
            });
        } else {
         jQuery('#emptyURIMessage').css('display','block');
        }
    };
    
    </script>
    <script src="../flintsparql/lib/codemirror.js"></script>
    <script src="../flintsparql/lib/xml.js"></script>
    <link rel="stylesheet" href="../flintsparql/lib/codemirror.css"/>
    <link rel="stylesheet" href="../flintsparql/lib/xml.css"/>
  </head>
  <body onload="lorestore.init()">  
  	<%@ include file="/WEB-INF/jsp/menu.jsp" %>
    <div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Update named graph</h1>
        </div>
        <div class="row">
          <div class="span10">
          <ul class="breadcrumb">
		     	 <li><a href="../index.html">Home</a> <span class="divider">/</span></li>
				 <li><a href="../oreadmin/">Content management</a> <span class="divider">/</span></li>
			 	 <li class="active">Update named graph</li>
		     </ul>
            <div id="successMessage" style="display:none" class="alert alert-success">
              <a href="#" data-dismiss="alert"class="close">x</a>
              <span class="label label-success">Success</span> Named graph <span id="deletedURI"></span> has been updated
            </div>
            <div id="failureMessage" style="display:none" class="alert alert-error">
              <a href="#" data-dismiss="alert" class="close">x</a>
              <span id="failMessage"></span>
            </div>
            <p>Update an Annotation or Resource Map:</p>
            <p>Load a graph by URI, modify the RDF/XML in the editor below and select 'Save' to update the content.</p>
            
            <div class="form-horizontal">
            	<div class="form-actions">
                    <input id="updateID" type="text">
                    <button class="btn" onclick="lorestore.loadGraph()">Load</button>
                    <button class="btn" onclick="lorestore.updateGraph()">Save</button>
                </div> 
            </div>
            <textarea id="rdfeditor" name="rdfeditor" cols="100" rows="1"> 
            </textarea>
      </div>
      </div>
      </div>
      <%@ include file="/WEB-INF/jsp/footer.jsp" %>
    </div>
  </body>
</body>
</html>