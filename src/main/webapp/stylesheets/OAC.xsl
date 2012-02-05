<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:oac="http://www.openannotation.org/ns/"
	xmlns:cnt="http://www.w3.org/2008/content#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:java="http://xml.apache.org/xslt/java"
    xmlns:xalan="http://xml.apache.org/xalan"
	exclude-result-prefixes="xalan java">
	<xsl:output method="html" encoding="UTF-8" indent="yes" />

	<xsl:template match="/">
		<html>
			<head>
				<title>OAC Annotations/Replies</title>
				<link type="text/css" rel="stylesheet" href="../stylesheets/bootstrap.min.css"/>
			</head>
			<body style="padding-top:40px">
				    <div class="navbar navbar-fixed-top">
					    <div class="navbar-inner">
					        <div class="container">
					            <a class="brand" href="../index.html"><img src="../images/lorestorelogo.png"/></a>
						    </div>
						</div>
					</div>
					<div class="container">
                    <div class="content">
                    
                    <xsl:if test="count(/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org')]) = 0">
                        <p style="margin-top:2em">No matching annotations</p>
                    </xsl:if>
					<xsl:for-each select="/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org')]">
					    <xsl:variable name="annotype" select="substring-after(rdf:type/@rdf:resource, '/ns/')"/>
					    <div class="page-header">
						<h1>
								<xsl:choose>
									<xsl:when test="dc:title/.">
										<xsl:value-of select="dc:title" />
									</xsl:when>
									<xsl:otherwise>
										Untitled
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
                                   </xsl:when>
                                   <xsl:when test="dc:creator/.">
                                       <xsl:value-of select="dc:creator" />
                                   </xsl:when>
                                   <xsl:otherwise>
                                       Anonymous
                                   </xsl:otherwise>
                               </xsl:choose> 
                        </xsl:variable>
                        
                        <xsl:text>Created </xsl:text>
                            <xsl:value-of select="substring-before(dcterms:created, 'T')" />
                            <xsl:if test="dcterms:modified">
                                <xsl:text>,  Last modified </xsl:text>
                                <xsl:value-of select="substring-before(dcterms:modified, 'T')" />
                            </xsl:if>
                            <xsl:if test="$creator"> by <xsl:value-of select="$creator"/></xsl:if>
                         
                        </small>
                        
                        </div>
                        
                        <xsl:for-each select="oac:hasBody">
                            <xsl:variable name="bodyuri" select="@rdf:resource"/>
                            
	                        <xsl:choose>
	                        <xsl:when test="starts-with($bodyuri,'urn:uuid:')">
							<blockquote>
	                          <xsl:apply-template select="//rdf:Description[@rdf:about=$bodyuri and cnt:rest]"/>
	                          <xsl:copy-of select="//rdf:Description[@rdf:about=$bodyuri]"/>
	                          <xsl:if test="//rdf:Description[@rdf:about=$bodyuri and dcterms:creator]">
	                          <xsl:variable name="bodycreator" select="//rdf:Description[@rdf:about=$bodyuri and dcterms:creator]"/>
	                          <small>
	                          <xsl:value-of select="//rdf:Description[@rdf:resource=$bodycreator/@rdf:resource or @rdf:nodeID=$bodycreator/@rdf:nodeID]/foaf:name"/>
	                          </small>
	                          </xsl:if>
	                        </blockquote>
	                        </xsl:when>
	                        <xsl:otherwise>
	                        <span class="label"><xsl:value-of select="$annotype"/> body</span>&#160;
	                        <a target="_blank">
                                <xsl:attribute name="href">
                                    <xsl:value-of select="@rdf:resource" />
                                </xsl:attribute>
                                <xsl:value-of select="@rdf:resource" />
                            </a>
	                        </xsl:otherwise>
	                        </xsl:choose>
						</xsl:for-each>
						
						
						<p>
              				
            				<xsl:for-each select="oac:hasTarget">
            				<span class="label"><xsl:value-of select="$annotype" /> target</span>&#160;
							<a target="_blank">
								<xsl:attribute name="href">
								    <xsl:value-of select="@rdf:resource" />
								</xsl:attribute>
								<xsl:value-of select="@rdf:resource" />
							</a>
							</xsl:for-each>
						</p>
						
                        <p><a target="_blank" href="?annotates={@rdf:about}">Find replies</a></p>
						<hr/>
					</xsl:for-each>
                </div>
                </div>
			</body>
		</html>

	</xsl:template>
    <xsl:template match="rdf:Description[cnt:rest]">
    <xsl:value-of select="cnt:rest" disable-output-escaping="yes"/>
    </xsl:template>
</xsl:stylesheet>
