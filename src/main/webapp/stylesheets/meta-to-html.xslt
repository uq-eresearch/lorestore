<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<html>
		<body>
			<table border="1" cellspacing="0" cellpadding="3">
			<caption><b>Metadata</b></caption>
			<xsl:for-each select="/rdf:RDF/rdf:Description/*">
				<xsl:if test="not(local-name()='type')">
				<tr>
					<th><xsl:value-of select="local-name()"/></th>
					<td><xsl:value-of select="."/></td>
				</tr>
				</xsl:if>
			</xsl:for-each>

			</table>
		</body></html>
	</xsl:template>
</xsl:stylesheet>