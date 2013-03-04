<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE rdf:RDF [
	   <!ENTITY xsd  "http://www.w3.org/2001/XMLSchema#" >
	   <!ENTITY rdf  "http://www.w3.org/1999/02/22-rdf-syntax-ns#" >
	 ]>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:oac="http://www.openannotation.org/ns/"
	xmlns:oa="http://www.w3.org/ns/oa#"
	xmlns:cnt="http://www.w3.org/2008/content#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:sparql="http://www.w3.org/2005/sparql-results#"
	xmlns:q="http://www.openrdf.org/schema/qname#"
	exclude-result-prefixes="xalan java">
	<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<xsl:strip-space elements="*"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>Query Result</title>
				<link type="text/css" rel="stylesheet" href="../stylesheets/bootstrap.min.css"/>
				<link rel="stylesheet" href="../prettify/prettify.css" type="text/css"/>
                <script src="../prettify/prettify.js" type="text/javascript"></script>
			</head>
			<body onload="prettyPrint()" style="padding-top:40px">
				<div class="navbar navbar-fixed-top">
					<div class="navbar-inner">
						<div class="container">
							<a class="brand" href="../index.html">
								<img src="../images/lorestorelogo.png"/>
							</a>
						</div>
					</div>
				</div>
				<div class="container">
					<div class="content">
						<div class="page-header">
							<h1>Query Result</h1>
						</div>
						<table class="table table-striped table-condensed table-bordered">
							<xsl:apply-templates/>
						</table> 
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
	
	
	
	<xsl:template match="sparql:head">
		<thead>
			<xsl:for-each select="sparql:variable">
				<th>
					<xsl:variable name="varname" select="@name"/>
					<xsl:choose>
						<xsl:when test="$varname='g'">Object ID</xsl:when>
						<xsl:when test="$varname='a'">Creator</xsl:when>
						<xsl:when test="$varname='m'">Modified</xsl:when>
						<xsl:when test="$varname='t'">Title</xsl:when>
						<xsl:when test="$varname='priv'">Private</xsl:when>
						<xsl:when test="$varname='v'">Value matched</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$varname"/>
						</xsl:otherwise>
					</xsl:choose>
                  
				</th>
			</xsl:for-each>
		</thead>
	</xsl:template>
	
	<!--  query result display based on openrdf workbench table.xsl -->
	<xsl:template match="sparql:results">
		<tbody>
			<xsl:apply-templates select="*"/>
		</tbody>
	</xsl:template>
    
	<xsl:template match="sparql:result">
		<xsl:variable name="result" select="." />
		<tr>
			<xsl:for-each select="../../sparql:head/sparql:variable">
				<xsl:variable name="name" select="@name" />
				<td style="font-size:small">
					<xsl:apply-templates
						select="$result/sparql:binding[@name=$name]" />
				</td>
			</xsl:for-each>
		</tr>
	</xsl:template>

    

	<xsl:template match="sparql:literal[@datatype]">
		<xsl:value-of
			select="concat('&quot;', text(), '&quot;^^&lt;', @datatype, '&gt;')" />
	</xsl:template>

	<xsl:template match="sparql:literal[@q:qname]">
		<xsl:value-of 
			select="concat('&quot;', text(), '&quot;^^', @q:qname)" />
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;boolean']">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;integer']">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;decimal']">
        
		<xsl:value-of select="." />
        
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;double']">
        
		<xsl:value-of select="." />
        
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;date']">
        
		<xsl:value-of select="." />
        
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;dateTime']">
        
		<xsl:value-of select="." />
        
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;time']">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template match="sparql:literal[@datatype = '&xsd;duration']">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template match="sparql:uri[@q:qname]">
		<xsl:value-of select="@q:qname" />
	</xsl:template>

	<xsl:template
		match="sparql:literal[@datatype = '&rdf;XMLLiteral']">
		<pre class="pre prettyprint">
			<xsl:value-of select="text()" />
		</pre>
	</xsl:template>

	<xsl:template match="sparql:literal[@xml:lang]">
       
		<xsl:value-of
			select="concat('&quot;', text(), '&quot;@', @xml:lang)" />
	</xsl:template>

	<xsl:template match="sparql:literal">
		<xsl:choose>
			<xsl:when test="contains(text(), '&#10;')">
				<pre class="pre prettyprint">
					<xsl:value-of select="text()" />
				</pre>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="concat('&quot;', text(), '&quot;')" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="sparql:bnode">
		<xsl:value-of
			select="concat('_:', text())" />
	</xsl:template>

	<xsl:template match="sparql:uri">
		<a target="_blank" href="{text()}">
			<xsl:value-of
			select="concat('&lt;', text(), '&gt;')" />
		</a>
	</xsl:template>
	
	<xsl:template match="sparql:boolean">
		<tr>
			<td>
				<xsl:value-of select="text()" />
			</td>
		</tr>
	</xsl:template>
    
	<!-- ISO-8859-1 based URL-encoding demo
		   Written by Mike J. Brown, mike@skew.org.
		   Updated 2002-05-20.

		   No license; use freely, but credit me if reproducing in print.
		   -->


	<xsl:template name="url-encode">
		<xsl:param name="str"/>   

		<!-- Characters we'll support.
			   We could add control chars 0-31 and 127-159, but we won't. -->
		<xsl:variable name="ascii"> !"#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~</xsl:variable>
		<xsl:variable name="latin1">&#160;&#161;&#162;&#163;&#164;&#165;&#166;&#167;&#168;&#169;&#170;&#171;&#172;&#173;&#174;&#175;&#176;&#177;&#178;&#179;&#180;&#181;&#182;&#183;&#184;&#185;&#186;&#187;&#188;&#189;&#190;&#191;&#192;&#193;&#194;&#195;&#196;&#197;&#198;&#199;&#200;&#201;&#202;&#203;&#204;&#205;&#206;&#207;&#208;&#209;&#210;&#211;&#212;&#213;&#214;&#215;&#216;&#217;&#218;&#219;&#220;&#221;&#222;&#223;&#224;&#225;&#226;&#227;&#228;&#229;&#230;&#231;&#232;&#233;&#234;&#235;&#236;&#237;&#238;&#239;&#240;&#241;&#242;&#243;&#244;&#245;&#246;&#247;&#248;&#249;&#250;&#251;&#252;&#253;&#254;&#255;</xsl:variable>

		<!-- Characters that usually don't need to be escaped -->
		<xsl:variable name="safe">!'()*-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz~</xsl:variable>

		<xsl:variable name="hex" >0123456789ABCDEF</xsl:variable>


		<xsl:if test="$str">
			<xsl:variable name="first-char" select="substring($str,1,1)"/>
			<xsl:choose>
				<xsl:when test="contains($safe,$first-char)">
					<xsl:value-of select="$first-char"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="codepoint">
						<xsl:choose>
							<xsl:when test="contains($ascii,$first-char)">
								<xsl:value-of select="string-length(substring-before($ascii,$first-char)) + 32"/>
							</xsl:when>
							<xsl:when test="contains($latin1,$first-char)">
								<xsl:value-of select="string-length(substring-before($latin1,$first-char)) + 160"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:message terminate="no">Warning: string contains a character that is out of range! Substituting "?".</xsl:message>
								<xsl:text>63</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="hex-digit1" select="substring($hex,floor($codepoint div 16) + 1,1)"/>
					<xsl:variable name="hex-digit2" select="substring($hex,$codepoint mod 16 + 1,1)"/>
					<xsl:value-of select="concat('%',$hex-digit1,$hex-digit2)"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="string-length($str) &gt; 1">
				<xsl:call-template name="url-encode">
					<xsl:with-param name="str" select="substring($str,2)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
