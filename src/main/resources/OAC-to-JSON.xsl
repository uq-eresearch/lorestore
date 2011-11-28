<?xml version="1.0"?> 
<xsl:stylesheet version="1.0"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:ore="http://www.openarchives.org/ore/terms/"
    xmlns:oac="http://www.openannotation.org/ns/"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--  use non-indented html output to ensure inline body content markup comes through -->
    <xsl:output method="html" indent="no"/>
    
    <xsl:key name="byId" match="rdf:Description" use="@rdf:about"/>
    
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="/">
    <xsl:text>[</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="rdf:Description[rdf:type/@rdf:resource ='http://www.openannotation.org/ns/Annotation' or rdf:type/@rdf:resource='http://www.openannotation.org/ns/Reply']">
    	<xsl:text>{</xsl:text>
    	
    	<!--  annotation id -->
    	<xsl:text>&#10;&#x9;id: </xsl:text>
    	<xsl:text>'</xsl:text><xsl:value-of select="@rdf:about"/><xsl:text>'</xsl:text>
    	
    	<!--  annotation type(s) -->
    	<xsl:text>,&#10;&#9;type: [</xsl:text>
    		<xsl:for-each select="rdf:type">
    			<xsl:text>'</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>'</xsl:text>
    			<xsl:if test="position()!=last()">,</xsl:if>
    		</xsl:for-each>
    	<xsl:text>]</xsl:text>
    	
    	<!--  creator -->
    	
    	<!--  created, modified -->
    	<xsl:if test="dcterms:created">
    	<xsl:text>,&#10;&#9;created: '</xsl:text><xsl:value-of select="dcterms:created[1]"/><xsl:text>'</xsl:text>
    	</xsl:if>
    	<xsl:if test="dcterms:modified">
    	<xsl:text>,&#10;&#9;modified: '</xsl:text><xsl:value-of select="dcterms:modified[1]"/><xsl:text>'</xsl:text>
    	</xsl:if>
    	<!-- targets -->
    	<xsl:text>,&#10;&#9;target: [</xsl:text>
    	<xsl:apply-templates select="oac:hasTarget"/>
    	<xsl:text>&#10;&#9;]</xsl:text>
    	
    	<!--  bodies -->
    	<xsl:text>,&#10;&#9;body: [</xsl:text>
    	<xsl:apply-templates select="oac:hasBody"/>
    	<xsl:text>&#10;&#9;]</xsl:text>
    	
		<xsl:text>&#10;}</xsl:text>
    	<xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>
    

	
    <xsl:template match="oac:hasBody | oac:hasTarget">
    <xsl:text>&#10;&#9;{</xsl:text>
    	<!--  resource id -->
    	<xsl:text>&#10;&#9;&#9;id: '</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>'</xsl:text>
    	<xsl:variable name="body" select="key('byId', @rdf:resource)[1]"/>

    	<!--  part of -->
    	<xsl:if test="$body/dcterms:isPartOF">
    		<xsl:text>,&#10;&#9;&#9;&#9;partOf: '</xsl:text>
    		<xsl:value-of select="$body/dcterms:isPartOf[1]"/>
    		<xsl:text>'</xsl:text>
    	</xsl:if>
    	<!--  resource types -->
    	<xsl:if test="$body/rdf:type">
    		<xsl:text>&#10;&#9;&#9;type: [</xsl:text>
    		<xsl:for-each select="$body/rdf:type">
    			<xsl:text>'</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>'</xsl:text>
    			<xsl:if test="position()!=last()">,</xsl:if>
    		</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>
    	<!--  any other properties -->
    	
    	<xsl:variable name="otherprops">
	    	<xsl:for-each select="$body/*[local-name(.)!='isPartOf' and local-name(.)!='type']">
	    		<xsl:variable name="contents">
	    			<xsl:choose>
	    				<xsl:when test="*">
	    				<xsl:copy-of select="*"/>
	    				</xsl:when>
	    				<xsl:when test="@rdf:resource">
	    					<xsl:value-of select="@rdf:resource"/>
	    				</xsl:when>
	    				<xsl:otherwise>
	    					<xsl:value-of select="."/>
	    				</xsl:otherwise>
	    			</xsl:choose>
	    		</xsl:variable>
	    		<xsl:text>&#10;&#9;&#9;&#9;{name: '</xsl:text><xsl:value-of select="name(.)"/><xsl:text>', value: '</xsl:text>
	    		<xsl:copy-of select="$contents"/><xsl:text>'</xsl:text>
	    		<xsl:if test="position()!=last()">,</xsl:if>
    	</xsl:for-each>
    	</xsl:variable>
    	<xsl:if test="$otherprops != ''">
	    	<xsl:text>&#10;&#9;&#9;properties: [</xsl:text>
	    		<xsl:copy-of select="$otherprops"/>
	    	<xsl:text>&#10;&#9;&#9;]</xsl:text>
    	</xsl:if>
    <xsl:text>&#10;&#9;}</xsl:text>
    <xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>
    
    <!--  suppress default template for other things -->
    <xsl:template match="rdf:Description"/>
    
</xsl:stylesheet>