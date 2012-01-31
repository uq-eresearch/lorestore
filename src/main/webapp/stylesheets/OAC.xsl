<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/">
	<xsl:output method="html" encoding="UTF-8" indent="yes" />

	<xsl:template match="/">
		<html>
			<head>
				<title>OAC Annotations/Replies</title>
				<script type="text/javascript" src="../js/jquery-1.4.3.min.js"/>

			</head>
			<body>
				<div>
					<xsl:for-each select="/rdf:RDF/rdf:Description">
						<div style='color:white;background-color:darkred;width:100%;min-height:18'>
							<strong>
								<xsl:choose>
									<xsl:when test="dc:title/.">
										<xsl:value-of select="dc:title" />
									</xsl:when>
									<xsl:otherwise>
										Untitled
									</xsl:otherwise>
								</xsl:choose>
							</strong>
						</div>
						<span style='font-size:smaller;color:#51666b;'>
							<xsl:value-of select="substring-after(rdf:type/@rdf:resource, '#')" />
							<xsl:text> by </xsl:text>
							<xsl:choose>
								<xsl:when test="purl:creator/.">
									<xsl:value-of select="purl:creator" />
								</xsl:when>
								<xsl:when test="purl11:creator/.">
									<xsl:value-of select="purl11:creator" />
								</xsl:when>
								<xsl:otherwise>
									Anonymous
								</xsl:otherwise>
							</xsl:choose>
						</span>
						<br />
						
                        <div id="insertBodyHere"></div>
                        <script type="text/javascript">
                           $("#insertBodyHere").load("<xsl:value-of select="a:body/@rdf:resource"/>");
                        </script>

						<br />
						
						<span style="font-size:smaller;color:#aaa">
							<xsl:text>Created: </xsl:text>
							<xsl:value-of select="substring-before(a:created, 'T')" />
							<xsl:if test="a:modified">
								<xsl:text> Last modified: </xsl:text>
								<xsl:value-of select="substring-before(a:modified, 'T')" />
							</xsl:if>
							<br />
							<xsl:choose>
              					<xsl:when test="b:root">In reply to </xsl:when>
              					<xsl:when test="a:annotates">Annotates </xsl:when>
            				</xsl:choose>
							<a target="_blank">
								<xsl:attribute name="href">
                  					<xsl:choose>
					                   <xsl:when test="b:root">
					                   	<xsl:value-of select="b:inReplyTo/@rdf:resource" />?danno_useStylesheet=</xsl:when>
					                   <xsl:when test="a:annotates">
					                   	<xsl:value-of select="a:annotates/@rdf:resource" />
					                   </xsl:when>
					                 </xsl:choose>
               					</xsl:attribute>
								<xsl:choose>
									<xsl:when test="b:root">
										<xsl:value-of select="b:inReplyTo/@rdf:resource" />
									</xsl:when>
									<xsl:when test="a:annotates">
										<xsl:value-of select="a:annotates/@rdf:resource" />
									</xsl:when>
								</xsl:choose>
							</a>
							<br />
							<a target="_blank" href="{a:body/@rdf:resource}">View body</a>&#160;&#160;<a target="_blank" href="{@rdf:about}">View RDF</a>
						</span>
						<br />
						<br />
					</xsl:for-each>

				</div>
			</body>
		</html>

	</xsl:template>

</xsl:stylesheet>
