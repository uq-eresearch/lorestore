<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:oac="http://www.openannotation.org/ns/"
	xmlns:cnt="http://www.w3.org/2011/content#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:java="http://xml.apache.org/xslt/java"
    xmlns:xalan="http://xml.apache.org/xalan"
	exclude-result-prefixes="xalan java">
	<xsl:output method="html" encoding="UTF-8" indent="yes" />
    <xsl:strip-space elements="*"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>OAC Annotations/Replies</title>
				<link type="text/css" rel="stylesheet" href="../stylesheets/bootstrap.min.css"/>
				<link rel="stylesheet" href="../prettify/prettify.css" type="text/css"/>
                <style type="text/css">
                	.annotation {padding-bottom:1em; border-bottom:1px dotted #eeeeee;}
                	.page-header {border-bottom: none;}
                </style>
			</head>
			<body onload="prettyPrint()" style="padding-top:40px">
				    <div class="navbar navbar-fixed-top">
					    <div class="navbar-inner">
					        <div class="container">
					            <a class="brand" href="../index.html"><img src="../images/lorestorelogo.png"/></a>
						    </div>
						</div>
					</div>
					<div class="container">
                    <div class="content">
                    <script src="../jquery-1.7.1.min.js" type="text/javascript"></script>
                    <xsl:if test="count(/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org/ns/')]) = 0">
                        <p style="margin-top:2em">No matching annotations</p>
                    </xsl:if>
                    
					<xsl:for-each select="/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org/ns/Annotation') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/Reply') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/DataAnnotation')]">
					   <xsl:variable name="annoUri" select="@rdf:about"/>
					    <xsl:variable name="annotype" select="substring-after(rdf:type/@rdf:resource, '/ns/')"/>
					    <div class="annotation">
					    <div class="page-header">
						<h1>
								<xsl:choose>
									<xsl:when test="dc:title/.">
										<xsl:value-of select="dc:title" />
									</xsl:when>
									<xsl:otherwise>
										Untitled <xsl:value-of select="$annotype"/>
									</xsl:otherwise>
								</xsl:choose>
						</h1>
						<small style="font-size:smaller;color:#aaa">
                       <xsl:variable name="creator">
                            <xsl:choose>
                                   <xsl:when test="dcterms:creator/.">
                                       <xsl:variable name="creator" select="dcterms:creator/@rdf:resource | dcterms:creator/@rdf:nodeID"/>
                                       <xsl:value-of select="//rdf:Description[@rdf:about=$creator or @rdf:nodeID=$creator]/foaf:name" />
                                       <xsl:choose>
                                          <xsl:when test="dcterms:creator/@rdf:nodeID"> (no associated user account)</xsl:when>
                                          <xsl:otherwise> (<xsl:value-of select="$creator"/>)</xsl:otherwise>
                                       </xsl:choose>
                                       <xsl:variable name="mbox" select="//rdf:Description[@rdf:about=$creator or @rdf:nodeID=$creator]/foaf:mbox"/>
                                       <xsl:if test="$mbox">
                                       <xsl:text> </xsl:text><xsl:value-of select="$mbox"/>
                                       </xsl:if>
                                   </xsl:when>
                                   <xsl:when test="dc:creator/.">
                                       <xsl:value-of select="dc:creator" />
                                   </xsl:when>
                                   <xsl:otherwise>
                                       Anonymous
                                   </xsl:otherwise>
                               </xsl:choose> 
                        </xsl:variable>
                        <a target="_blank" href="{$annoUri}"><xsl:value-of select="$annotype" /></a>
                        <xsl:text> created </xsl:text>
                            <xsl:value-of select="substring-before(dcterms:created, 'T')" />
                            <xsl:if test="dcterms:modified">
                                <xsl:text>,  Last modified </xsl:text>
                                <xsl:value-of select="substring-before(dcterms:modified, 'T')" />
                            </xsl:if>
                            <xsl:if test="$creator"> by <xsl:value-of select="$creator"/></xsl:if>
                         
                        </small>
                        
                        </div>

						<xsl:apply-templates select="oac:hasBody"/>
            			<xsl:apply-templates select="oac:hasTarget"/>
						
						<!--  display other annotation properties from the RDF -->
						<xsl:for-each select="//rdf:Description[@rdf:about = $annoUri]/*">
						<xsl:if test="local-name() != 'creator' and local-name() != 'created' and local-name() != 'modified' and local-name() != 'type' and local-name() != 'hasBody' and local-name() != 'hasTarget' and local-name() != 'title'">
						  <p><strong><xsl:value-of select="local-name()"/>:&#160;</strong><xsl:value-of select="@rdf:resource | ."/></p>
						</xsl:if>
						</xsl:for-each>
                        <p><a class="btn btn-mini" target="_blank" href="../oac/?annotates={@rdf:about}">Find replies</a>&#160;&#160;<a class="btn btn-mini" target="_blank" href="#" onclick="downloadTrig('{@rdf:about}')">Get TriG</a>
                        &#160;&#160;<a class="btn btn-mini" target="_blank" href="#" onclick="downloadTrix('{@rdf:about}')">Get TriX</a>
                        &#160;&#160;<a class="btn btn-mini" target="_blank" href="#" onclick="downloadRdfXml('{@rdf:about}')">Get RDF/XML</a>
                        </p>
						
						</div>
					</xsl:for-each>
                </div>
                <p><small>Use browser <em>View Page Source</em> to see underlying annotation RDF</small></p>
                </div>
                <script src="../prettify/prettify.js" type="text/javascript"></script>
                
                <script src="../bootstrap-tab.js" type="text/javascript"></script>
                <script type="text/javascript">
                    function downloadTrig(annoID) {
                        jQuery.ajax({
                            url: annoID,
                            headers: { 
                                Accept : "application/x-trig"
                            },
                            success: function(data, status, jqXHR){
                                window.location.href='data:application/x-trig,' + encodeURIComponent(jqXHR.responseText);
                            }
                        });
                    }
                    function downloadTrix(annoID) {
                        jQuery.ajax({
                            url: annoID,
                            headers: { 
                                Accept : "application/trix"
                            },
                            success: function(data, status, jqXHR){
                                window.location.href='data:application/trix,' + encodeURIComponent(jqXHR.responseText);
                            }
                        });
                    }
                    function downloadRdfXml(annoID) {
                        jQuery.ajax({
                            url: annoID,
                            headers: { 
                                Accept : "application/rdf+xml"
                            },
                            success: function(data, status, jqXHR){
                                window.location.href='data:application/rdf+xml,' + encodeURIComponent(jqXHR.responseText);
                            }
                        });
                    }
                </script>
			</body>
		</html>

	</xsl:template>
	
	
	<xsl:template match="oac:hasTarget | oac:hasBody">
 		<xsl:variable name="resourceUri" select="@rdf:resource"/>
 		<xsl:variable name="bodyOrTarget">
 			<xsl:choose>
 				<xsl:when test="local-name() = 'hasTarget'">Target</xsl:when>
 				<xsl:otherwise>Body</xsl:otherwise>
 			</xsl:choose>
 		</xsl:variable>
    	<div style="padding-bottom:1em">
     		<span class="label">
     			<xsl:choose>
     				<xsl:when test="//rdf:Description[@rdf:about = $resourceUri]/rdf:type[@rdf:resource='http://www.openannotation.org/ns/ConstrainedTarget'or @rdf:resource='http://www.openannotation.org/ns/ConstrainedBody']">
     				<xsl:text>Constrained </xsl:text>
     				</xsl:when>
     				<xsl:when test="$bodyOrTarget='Body' and starts-with(//rdf:Description[@rdf:about=$resourceUri]/rdf:type/@rdf:resource,'http://www.w3.org/2011/content#')">
     				<xsl:text>Inline </xsl:text>
     				</xsl:when>
     			</xsl:choose>
     			<xsl:value-of select="$bodyOrTarget"/>
     		</span>&#160;
     		<!--  test for urn or http -->
     		<xsl:choose>
     			<!--  display and link URI for body or target -->
     			<xsl:when test="starts-with($resourceUri,'http://')">
		     		<a target="_blank">
						<xsl:attribute name="href">
						    <xsl:value-of select="$resourceUri" />
						</xsl:attribute>
						<xsl:value-of select="$resourceUri" />
					</a>
				</xsl:when>
     			<xsl:otherwise>
     				<!-- Embed Inline Body-->
     				<xsl:apply-templates select="//rdf:Description[@rdf:about=$resourceUri and (cnt:rest or cnt:chars)]"/>
     				<!-- Display Constrained Body or Target -->
     				<xsl:apply-templates select="//rdf:Description[@rdf:about=$resourceUri and (rdf:type/@rdf:resource='http://www.openannotation.org/ns/ConstrainedTarget' or rdf:type/@rdf:resource='http://www.openannotation.org/ns/ConstrainedBody')]"/>
     			</xsl:otherwise>
     		</xsl:choose>
     		<!--  dcterms:isPartOf for body or target -->
			<xsl:variable name="resourcePartOf" select="//rdf:Description[@rdf:about=$resourceUri]/dcterms:isPartOf"/>
			<xsl:if test="$resourcePartOf">
				<p style="margin-left:2em"><strong>isPartOf:&#160;</strong><a href="{$resourcePartOf/@rdf:resource}"><xsl:value-of select="$resourcePartOf/@rdf:resource"/></a></p>
			</xsl:if>
			<!-- creator property attached to body or target -->
			<xsl:variable name="resourceCreator" select="//rdf:Description[@rdf:about=$resourceUri and dcterms:creator]/dcterms:creator"/>
			
            <xsl:if test="$resourceCreator">
                 <p style="margin-left:2em"><strong>creator:&#160;</strong><xsl:value-of select="//rdf:Description[@rdf:about=$resourceCreator/@rdf:resource or @rdf:nodeID=$resourceCreator/@rdf:nodeID]/foaf:name"/></p>
             </xsl:if>
		</div>				
	</xsl:template>
	
	<!--  constrained body or target -->
	<xsl:template match="rdf:Description[rdf:type/@rdf:resource='http://www.openannotation.org/ns/ConstrainedTarget' or rdf:type/@rdf:resource='http://www.openannotation.org/ns/ConstrainedBody']">
		<p style="margin-left:2em">
			<strong>constrains:&#160;</strong>
			<a href="{oac:constrains/@rdf:resource}"><xsl:value-of select="oac:constrains/@rdf:resource"/></a>
		</p>
		<xsl:variable name="constraint" select="oac:constrainedBy/@rdf:resource"/>
		<p style="margin-left:2em">
			<strong>constrainedBy:&#160;</strong>
			
			<xsl:if test="starts-with($constraint,'http://')">&#160;<a href="{$constraint}"><xsl:value-of select="$constraint"/></a></xsl:if>
			(<xsl:apply-templates select="//rdf:Description[@rdf:about=$constraint]/rdf:type" mode="constraint"/>)
		</p>
		<!--  constraint details -->
		<xsl:apply-templates select="//rdf:Description[@rdf:about=$constraint]" mode="constraint"/>
	</xsl:template>
	
	<!-- Display constraint type :  use a mode rather than template matching because constraint could be any type -->
	<xsl:template match="rdf:Description/rdf:type" mode="constraint">
		<xsl:variable name="constraintType" select="@rdf:resource"/>
		<xsl:for-each select="$constraintType">
		<xsl:choose>
			<xsl:when test="starts-with(.,'http://www.w3.org/2011/content#')">
			Inline
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
		</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<!--  Display constraint details -->
	<xsl:template match="rdf:Description" mode="constraint">
		<!--  display properties -->
		<xsl:for-each select="*[local-name()!='type' and namespace-uri(.)!='http://www.w3.org/2011/content#']">
			<p style="margin-left:4em"><strong><xsl:value-of select="local-name()"/>:&#160;</strong><xsl:value-of select="@rdf:resource | ."/></p>		
		</xsl:for-each>
		<xsl:if test="cnt:rest | cnt:chars">
			<div style="margin-left:4em"><xsl:apply-templates select="."/></div>
		</xsl:if>
	</xsl:template>
	
	<!--  inline content -->
    <xsl:template match="rdf:Description[cnt:rest or cnt:chars]">
    	<xsl:variable name="bodyId" select="substring-after(@rdf:about,'urn:uuid:')"/>
        <ul style="padding-top:0.5em" class="nav nav-tabs">
          <li class="active"><a data-toggle="tab" href="#bs{$bodyId}">Source</a></li>
          <li><a id="bt{$bodyId}" data-toggle="tab" href="#bp{$bodyId}">Presentation</a></li>
        </ul>
 
		<div class="tab-content">
		  <div class="tab-pane active" id="bs{$bodyId}">
		      <pre class="pre prettyprint">
                <xsl:value-of select="cnt:rest | cnt:chars"/>
              </pre></div>
		  <div class="tab-pane" id="bp{$bodyId}">
		      <!-- This does not work in Firefox, so we use a script instead (see below)
		      xsl:value-of select="cnt:rest" disable-output-escaping="yes"/-->
		  </div>
		  <script>
		      <!--   Update the presentation tab when first activated -->
			  jQuery('#bt<xsl:value-of select="$bodyId"/>').on('shown', function (e){
			     var presentationArea = jQuery('#bp<xsl:value-of select="$bodyId"/>');
			     if (presentationArea.html() == ''){
			       <xsl:variable name="apos">'  </xsl:variable>
                   <xsl:variable name="quot">"&#13;&#10;&#09;</xsl:variable>
                   <xsl:variable name="translated" select="translate(normalize-space(cnt:rest|cnt:chars),$quot,$apos)"/>
			     <xsl:choose>
                    <xsl:when test="starts-with(cnt:rest,'&lt;rdf:RDF')">
                    <!--  Script inserts turtle syntax for RDF body into presentation tab.
                        This is done via a script because the content has been escaped as ContentAsXML -->
                     var result="";
                     try{
                     var xmlDoc = jQuery.parseXML("<xsl:value-of select="$translated"/>");
                     
                        console.log("child",jQuery(xmlDoc).find("rdf\\:RDF"))
                     // Chrome doesn't seem to like the namespace, while Firefox requires it
                     jQuery(xmlDoc).find("Description, rdf\\:Description").each(function(){
                        result += "&lt;pre&gt;&amp;lt;" + jQuery(this).attr('about') + "&amp;gt;\n";
                        jQuery(this).children().each(function(){
                            result += "\t" + this.tagName + " ";
                            var resourceAttr = jQuery(this).attr('resource');
                            if(resourceAttr != ''){
                                result += "&amp;lt;" + resourceAttr + "&amp;gt;;\n";
                            } else {
                                result +=  "\"" + jQuery(this).text() + "\";\n";
                            }
                        });
                        result += "&lt;/pre&gt;";
                        
                        
                     });
                     presentationArea.html(result);
                     } catch (ex) {
                        alert(ex);
                     }
                    </xsl:when>
			        <xsl:otherwise>
			        
			        <!-- Insert HTML : content would have already been sanitized by the server, so no problem to display as is -->
			         <xsl:text>presentationArea.html("</xsl:text><xsl:value-of select="$translated"/><xsl:text>");</xsl:text>
			        </xsl:otherwise>
			    </xsl:choose>
			         
			     }
			  });
		  </script>
		</div>
    </xsl:template>
    <xsl:template match="*">
    </xsl:template>
</xsl:stylesheet>
