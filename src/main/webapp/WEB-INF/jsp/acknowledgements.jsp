<%@ include file="/WEB-INF/jsp/oreInclude.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${sitename} Acknowledgements</title>
    <%@ include file="header.jsp" %>
  </head>
  <body>  
	<%@ include file="menu.jsp" %>
    <div class="container">
      <div class="content">
		<div class="page-header main-page-header">
          <h1>Acknowledgements</h1>
        </div>
        <div class="row">
          <div class="span10">
            
            <p>lorestore was developed at UQ ITEE eResearch Group by:</p>
            <ul>
            	<li>Anna Gerber</li>
            	<li>Damien Ayers</li>
            </ul>
            <p>The resource map aspects of lorestore were originally developed as part of the <a href="http://itee.uq.edu.au/~eresearch/projects/aus-e-lit/">Aus-e-Lit</a> project. Aus-e-Lit was funded by the National Collaborative Research Infrastructure Strategy (NCRIS) Platforms for Collaboration, through the National eResearch Architecture Taskforce (NeAT), and by the University of Queensland.</p>
    		<p>lorestore was extended to store and query annotations as part of the <a href="http://openannotation.org">Open Annotation Collaboration</a>. Support for OAC has been provided by the Andrew W. Mellon Foundation and the partners of the Collaboration.</p>

			<p>lorestore makes use of <a href="http://metadata.net/sites/emmet-0.6-SNAPSHOT/">Emmet</a> and <a href="http://metadata.net/sites/chico-0.6-SNAPSHOT/">Chico</a> for authentication, access control and user account management. Emmet was developed by Stephen Crawley and Ron Chernich for the <a href="http://itee.uq.edu.au/~eresearch/projects/diasb/overview.php">DIAS-B and the Atlas of Living Australia</a>.</p>
			<p>Additional open source frameworks used by the lorestore repository include <a href="http://www.openrdf.org/">Sesame 2.0</a> and <a href="http://rdf2go.semweb4j.org/">RDF2Go</a>.</p>
			<p>The web interface makes use of <a href="http://twitter.github.com/bootstrap/">Bootstrap</a>, <a href="http://jquery.com/">jQuery</a> and <a href="https://github.com/TSO-Openup/FlintSparqlEditor">Flint SPARQL editor</a>, as well as icons from <a href="http://glyphicons.com/">Glyphicons</a>, <a href="http://www.famfamfam.com/lab/icons/silk/">FAMFAMFAM</a> and <a href="http://p.yusukekamiyamane.com/">Yusuke Kamiyamane</a>.</p>
				
			<p>The source code for lorestore is released as open source under the terms of the GPL 3.0:</p>
			<ul>
				<li><a href="https://github.com/uq-eresearch/oacwebapp">OAC web app on GitHub</a></li>
				<li><a href="https://github.com/uq-eresearch/lorestore">lorestore on GitHub</a></li>
			</ul>
          </div>
      </div>
      </div>
      <%@ include file="footer.jsp" %>
    </div>
  </body>
</body>
</html>
