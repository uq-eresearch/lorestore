<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Developer Documentation</title>
    <%@ include file="header.jsp" %>
    <link type="text/css"  href="${secure}/prettify/prettify.css" rel="stylesheet">
    <script type="text/javascript" src="${secure}/prettify/prettify.js"></script>
  </head>
  <body onload="prettyPrint()">  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
		<div class="page-header">
          <h1>Developer Documentation</h1>
        </div>
		<div class="row">
        <div class="span8">
            <section id="crud">
				
                <h2>CRUD API</h2>
				<small>Create, read, update and delete resource maps and annotations</small>
				<hr>
				
			<p>ORE Resource Maps and OAC Annotations are stored within lorestore as RDF Named Graphs, hence they share a REST-based API for Create, Read, Update and Delete (CRUD).</p>
			
			<h3 id="create">Create</h3>
    		<p>Issue a PUT request to <code>${secure}/ore/</code> to create a resource map.</p> 
			<p>Issue a PUT request to <code>${secure}/oac/</code> to create an annotation.</p> 
			<p><span class="label label-info">Preconditions</span> Must be authenticated</p>
			<p>The contents of the request should be the RDF for the Resource Map or Annotation.</p>
			<p>As the URI for the object will be unknown until after the create request succeeds, use <code>${secure}/ore/#unsaved</code> as the Resource Map identifier.</p> 
			<p>Returns HTTP status code 201 and the RDF/XML of the new object on success</p>
			
			<h3 id="read">Read</h3>
			<p>Issue a GET request to the URI that identifies the Annotation or Resource Map.<br/>
				The result is returned in RDF/XML format.</p>
			<p><span class="label label-info">Preconditions</span> Object must be public (only option for non-authenticated users) or visible to your account</p>
			<p>Example:</p>
			<pre class="pre prettyprint">
var xhr = new XMLHttpRequest();
xhr.open("GET", "${secure}/ore/123456789", true);  
xhr.onreadystatechange= function(){
    if (xhr.readyState == 4 && xhr.status == 200) {
        var myResourceMapDoc = xhr.responseXML;
    }
};
xhr.setRequestHeader('Accept','application/rdf+xml')
xhr.send(null);
			</pre>
			
			<h3 id="update">Update</h3>
			
			<p>Issue a PUT request to the URI that identifies the Annotation or Resource Map. 
				The contents of the request should be the updated RDF for the object.
			</p>
			<p><span class="label label-info">Preconditions</span> Must be authenticated using the account that owns the object or as an administrator</p>
			
			<p>Any owner or modified date properties provided in the update will be ignored as these are managed by lorestore.</p>
            <p>Update requests will fail if the object has been marked as locked, except for users with administrator privileges.</p>
            <p>Returns HTTP status code 200 on success</p>
			
			<h3 id="delete">Delete</h3>
			<p>Issue a DELETE request to the resource map or annotation URI</p>
			<p><span class="label label-info">Preconditions</span> Must be authenticated using the account that owns the object or as an administrator</p>
			
			<p>Example:</p>
			<pre class="pre prettyprint">
var xhr = new XMLHttpRequest();
xhr.open("DELETE", "${secure}/ore/123456789");  
xhr.onreadystatechange= function(){
    if (xhr.readyState == 4) {
        if (xhr.status == 204) { 
            // success
        } 
    }
};
xhr.send(null);
			</pre>
			<h3 id="auth">Sign in</h3>
            <p>Some API operations require authentication.</p>
            <p>lorestore uses Emmet to manage authentication, which supports a number of standard authentication schemes.
                Refer to the <a href="http://metadata.net/sites/emmet-0.6-SNAPSHOT/">Emmet documentation</a> for further details of the schemes supported.
            </p>
            <p>Use <code>emmet.svc</code> with the <code>fetchEmmetUrls</code> action to get the configured login, logout and registration URLs.</p>
            <p>Example:</p>
            <pre class="pre prettyprint">
Ext.Ajax.request({
    url: ${secure}/emmet.svc,
    success: function(response){
       var jsObject = Ext.decode(response.responseText);
       var emmetUrls = jsObject.emmetUrls;
       var logoutUrl = emmetUrls['emmet.logout.url'];
       var loginUrl = emmetUrls['emmet.login.url'];
       var registerUrl = emmetUrls['emmet.register.url'];
       // ...
    },
    method: 'GET',
    params: {
             action: 'fetchEmmetUrls',
             format: 'json'
    }
});
            </pre>
            <p>To fetch the authentication status and details for the current request, use <code>emmet.svc</code> with the <code>fetchAuthentication</code> action. Refer to the <a href="http://metadata.net/sites/emmet-0.6-SNAPSHOT/webapis.html#Action_-_fetchAuthentication">Emmet API documentation</a> for further details.</p>
            
			</section>
<section id="queries">
			<h2>Query API</h2>
			<hr>
			<p>Results for several common queries for resource maps and annotations are available via GET. lorestore also provides a <a href="#sparql">SPARQL endpoint</a> for custom queries.</p>
			<h3 id="ore">Resource Maps</h3>
			<p>Issue a GET request to <code>${secure}/ore</code> providing <code>refersTo</code>, <code>matchval</code> and <code>matchpred</code> parameters.
				Parameter values should be URL encoded. The parameters can be used in combination. Results are returned in SPARQL XML format. You do not need to sign in to query public resource maps. 
				However, if you have signed in, private resource maps that you have permission to view will be included in the results.</p> 
			
			
			<h4>refersTo</h4>
			<p>Find Resource Maps that make reference to a given URI (e.g. it identifies the resource map or is aggregated within)</p>
			
			<p>Example: find resource maps that refer to http://example.org/ :</p>
			<p><code>GET ${secure}/ore/?refersTo=http%3A%2F%2Fexample.org%2F</code></p>
			
			<p>Example of refersTo query result:</p>
            <pre class="pre prettyprint">
&lt;?xml version='1.0' encoding='UTF-8'?&gt;
&lt;sparql xmlns='http://www.w3.org/2005/sparql-results#'&gt;
    &lt;head&gt;
        &lt;variable name='g'/&gt; &lt;!-- resource map identifier --&gt;
        &lt;variable name='a'/&gt; &lt;!-- author / creator --&gt;
        &lt;variable name='m'/&gt; &lt;!-- date last modified --&gt;
        &lt;variable name='t'/&gt; &lt;!-- title --&gt;
        &lt;variable name='priv'/&gt; &lt;!-- whether resource map is private --&gt;
    &lt;/head&gt;
    &lt;results&gt;
        &lt;result&gt;
            &lt;binding name='g'&gt;
                &lt;uri&gt;${secure}/ore/123456789&lt;/uri&gt;
            &lt;/binding&gt;
            &lt;binding name='m'&gt;
                &lt;literal datatype='http://purl.org/dc/terms/W3CDTF'&gt;
                2012-02-01T09:18:06+10:00
                &lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='t'&gt;
                &lt;literal&gt;Example Resource Map&lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='a'&gt;
                &lt;literal&gt;Anonymous&lt;/literal&gt;
            &lt;/binding&gt;
        &lt;/result&gt;
    &lt;/results&gt;
&lt;/sparql&gt;
            </pre>
			
			<h4>matchval</h4>
			<p>Find resource maps with predicate values containing the search term. When used without any other parameters, functions like a keyword search.</p>
			<p>Example: match resource maps where any value contains 'Lawson'</p>
			<p>
            <code>GET ${secure}/ore/?matchval=Lawson</code>
            </p>
			<p>Example of matchval query result:</p>
			<pre class="pre prettyprint">
&lt;?xml version='1.0' encoding='UTF-8'?&gt;
&lt;sparql xmlns='http://www.w3.org/2005/sparql-results#'&gt;
    &lt;head&gt;
        &lt;variable name='g'/&gt; &lt;!-- resource map identifier --&gt;
        &lt;variable name='a'/&gt; &lt;!-- author / creator --&gt;
        &lt;variable name='m'/&gt; &lt;!-- date last modified --&gt;
        &lt;variable name='t'/&gt; &lt;!-- title --&gt;
        &lt;variable name='v'/&gt; &lt;!-- value matched --&gt;
        &lt;variable name='priv'/&gt; &lt;!-- whether resource map is private --&gt;
    &lt;/head&gt;
    &lt;results&gt;
        &lt;result&gt;
            &lt;binding name='v'&gt;
                &lt;literal&gt;Henry Lawson AustLit record&lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='g'&gt;
                &lt;uri&gt;${secure}/ore/123456789;/uri&gt;
            &lt;/binding&gt;
            &lt;binding name='m'&gt;
                &lt;literal datatype='http://www.w3.org/2001/XMLSchema#date'&gt;2009-11-17&lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='t'&gt;
                &lt;literal&gt;Lawson Papers&lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='a'&gt;
                &lt;literal&gt;Anna Gerber&lt;/literal&gt;
            &lt;/binding&gt;
        &lt;/result&gt;
        ...
    &lt;/results&gt;
&lt;/sparql&gt;
			</pre>
			<h4>matchpred</h4>
			<p>Must be used in combination with <code>matchval</code>. Returns resource maps where the value of the specified predicate contains <code>matchval</code>. Provide an empty string for matchval to match all possible values.</p>
			<p>Example combining matchpred and matchval: Find all resource maps by a particular creator</p>
			<p>
            <code>GET ${secure}/ore/?matchval=Jane+Doe&matchpred=http%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2Fcreator</code>
            </p>
            <p>Example: find resource maps with any subject</p>
			<p><code>GET ${secure}/ore/?matchval=&matchpred=http%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2Fsubject</code></p>
			
			<h4>exploreFrom</h4>
			<p>Get related resources and resource maps (used by LORE explore view) for a given URI. 
				For convenience, if the exploreFrom URI is a resource map, results are returned for both the resource map and its aggregation.</p>
			<p>Example:</p>
			<p><code>GET ${secure}/ore/?exploreFrom=${secure}/ore/123456789</code></p>
			<p>Example exploreFrom result:</p>
			<pre class="pre prettyprint">
&lt;?xml version='1.0' encoding='UTF-8'?&gt;
&lt;sparql xmlns='http://www.w3.org/2005/sparql-results#'&gt;
    &lt;head&gt;
        &lt;variable name='something'/&gt; &lt;!-- related object --&gt;
        &lt;variable name='somerel'/&gt; &lt;!-- relationship to exploreFrom uri --&gt;
        &lt;variable name='sometitle'/&gt; &lt;!-- title for this object --&gt;
        &lt;variable name='sometype'/&gt; &lt;!-- type (resource maps and annos only) --&gt;
        &lt;!-- the following variables only apply if object is a resource map --&gt;
        &lt;variable name='creator'/&gt; &lt;!-- creator --&gt;
        &lt;variable name='modified'/&gt; &lt;!-- modified date --&gt;
        &lt;variable name='anotherrel'/&gt; &lt;!-- other relationships from object --&gt;
        &lt;variable name='somethingelse'/&gt; &lt;!-- target of other relationships --&gt;
    &lt;/head&gt;
    &lt;results&gt;
        &lt;result&gt;
            &lt;binding name='sometitle'&gt;
                &lt;literal&gt;School of Information Technology and Electrical Engineering - The University of Queensland, Australia&lt;/literal&gt;
            &lt;/binding&gt;
            &lt;binding name='somerel'&gt;
                &lt;uri&gt;http://www.openarchives.org/ore/terms/aggregates&lt;/uri&gt;
            &lt;/binding&gt;
            &lt;binding name='something'&gt;
                &lt;uri&gt;http://www.itee.uq.edu.au/&lt;/uri&gt;
            &lt;/binding&gt;
        &lt;/result&gt;
    &lt;/results&gt;
&lt;/sparql&gt;

			</pre>
			
			<h3 id="oac">Annotations</h3>
			<h4>annotates</h4>
			<p>Fetch annotations that annotate a given web resource (also fetches replies if the queried resource is itself an annotation). Results are returned in TriX format.</p>
			<p><code>GET ${secure}/oac/?annotates=http%3A%2F%2Fwww.example.org</code></p>
			
			</section>
			<section id="sparql">
			<h2>SPARQL endpoint</h2>
			<hr>
			<p>The SPARQL endpoint allows authenticated users with administrator privileges to issue custom queries over the underlying RDF triplestore.</p> 
			<p>Issue a GET request to <code>${secure}/oreadmin/sparql</code>, providing the escaped SPARQL query as the value of the <code>sparql</code> parameter. All matching results (regardless of privacy flags) are returned in SPARQL XML format.</p>
			<p>Example query returns the identifier for all annotations:</p>
			<p><code>${secure}/oreadmin/sparql?sparql=SELECT+*+WHERE+%7B%3Fa+a+%3Chttp%3A%2F%2Fwww.openannotation.org%2Fns%2FAnnotation%3E%7D</code></p>
			<p>Administrators can also use the <a href="${secure}/oreadmin/sparqlPage.html">SPARQL Query UI</a> to develop queries.</p>
			</section>
			<section id="user">
				<h2>User Management API</h2>
				<p>Refer to the <a href="http://metadata.net/sites/emmet-0.6-SNAPSHOT/webapis.html">Emmet web API documentation</a> for details of the user management API.</p>
			</section>
          </div>
          <div class="span2 sidebarmenu">
			<h2>Index</h2>
			<ul>
				<li><a href="#crud">CRUD API</a>
					<ul>
						
						<li><a href="#create">Create</a></li>
						<li><a href="#read">Read</a></li>
						<li><a href="#update">Update</a></li>
						<li><a href="#delete">Delete</a></li>
						<li><a href="#auth">Sign In</a></li>
					</ul>
				</li>
				<li><a href="#queries">Query API</a>
					<ul>
						<li><a href="#ore">Resource Maps</a></li>
						<li><a href="#oac">Annotations</a></li>
					</ul>
				</li>
				<li><a href="#sparql">SPARQL endpoint</a></li>
				<li><a href="#user">User Management</a></li>
			</ul>
		  </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
