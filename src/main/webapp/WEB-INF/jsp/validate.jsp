<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ include file="/WEB-INF/jsp/emmetUIInclude.jsp"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Validate Annotation</title>
   <%@ include file="header.jsp" %>
   <link rel="stylesheet" href="/lorestore/flintsparql/lib/codemirror.css"/>
   <link rel="stylesheet" href="/lorestore/flintsparql/lib/javascript.css"/>
   <link rel="stylesheet" href="/lorestore/flintsparql/lib/xml.css"/>
   <link type="text/css" href="/lorestore/stylesheets/font-awesome.min.css" rel="stylesheet">
  </head>
  <body>  
    <%@ include file="menu.jsp" %>
    
    <div class="container">
      <div class="content">
        <div class="page-header main-page-header">
          <h1>Validate Annotation</h1>
        </div>
          
          <textarea class="input-block-level span10" rows="15" name="data" id="data">
@prefix oa: &lt;http://www.w3.org/ns/oa#&gt; .
&lt;http://localhost:8080/lorestore/oa/sopabasic&gt; {
    &lt;http://localhost:8080/lorestore/oa/sopabasic&gt; a oa:Annotation ;
    oa:hasBody &lt;http://www.youtube.com/watch?v=uPh81LIe7B8&gt; ;
    oa:hasTarget &lt;http://en.wikipedia.org/&gt; .
}
          </textarea>
          <label class="radio">
            <input type="radio" name="contentType" value="application/json"> JSON-LD
          </label>
          <label class="radio">
            <input type="radio" name="contentType" value="application/rdf+xml"> RDF/XML
          </label>
          <label class="radio">
            <input type="radio" name="contentType" value="application/trix"> TriX
          </label>
          <label class="radio">
            <input type="radio" name="contentType" value="application/x-trig" checked> TriG
          </label>
          <div class="form-actions">
          <button id="validate" class="btn">Validate</button>
          </div>
          
          <div id="result">
          </div>
      </div>
     <%@ include file="footer.jsp" %>
     <script type="text/javascript" src="/lorestore/mustache.js"></script>
     <script type="text/javascript" src="/lorestore/flintsparql/lib/codemirror.js"></script>
     <script type="text/javascript" src="/lorestore/flintsparql/lib/xml.js"></script>
     <script type="text/javascript" src="/lorestore/flintsparql/lib/javascript.js"></script>
     <script type="text/javascript">
     // TODO support other modes and switch between for xml, TriX, TriG
     var cmEditor = CodeMirror.fromTextArea(document.getElementById('data'), {
         mode: "",
         lineNumbers: true,
         tabMode: "indent"
     });
     var sectionTemplate = "<h2>Validating against Section {{section}}</h2>" +
       "{{#constraints}}" +
       "<div><h3 class='{{status}}' title='{{status}}'><i class='icon-{{status}}'></i> " + 
       "{{ref}}</h3><p>{{description}}</p></div>" +
       "{{/constraints}}" +
       "<hr class='mute'>";
     jQuery("input:radio[name=contentType]").change(function(){
         var contentType = jQuery('input:radio[name=contentType]:checked').val();
         if (contentType == "application/rdf+xml" || contentType=="application/trix"){
             cmEditor.setOption("mode","xml");
         } else if (contentType == "application/json"){
             cmEditor.setOption("mode",{name: "javascript", json: true});
         } else {
             cmEditor.setOption("mode","");
         }
     });
     jQuery('#validate').click(function(ev){
         var data = jQuery('#data').val();
         var contentType = jQuery('input:radio[name=contentType]:checked').val();
         jQuery.ajax({
             type: 'POST',
             url: '/lorestore/oa/validate/',
             headers: {
                 'Accept': 'application/json',
                 'Content-Type': contentType
             },
             data: data.toString(),
             success: function(d){
                 console.log("got data", d);
                 var result = "";
                 for(var i =0; i< d.length; i++){
                     var section = d[i];
                     jQuery('#result').append(Mustache.render(sectionTemplate,section));
                 }
                 jQuery('#result').append("<p>Legend: <span class='ok'><i class='icon-ok'></i> Passed</span> <span class='skip'><i class='icon-skip'></i> Skipped</span> <span class='error'><icon class='icon-error'></i> Error</span> <span class='warn'><icon class='icon-warn'></i> Warning</span></p>");
             },
             error: function(e){
                 console.log("error",e);
             }
         })
         return false;
     })
     </script>
    </div>
  </body>
</body>
</html>