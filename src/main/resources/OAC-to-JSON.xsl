<?xml version="1.0"?> 
<xsl:stylesheet version="1.0"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:ore="http://www.openarchives.org/ore/terms/"
    xmlns:oac="http://www.openannotation.org/ns/"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:cnt="http://www.w3.org/2011/content#"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!--  use non-indented html output to ensure inline body content markup comes through -->
    <xsl:output method="html" indent="no"/>
    
    <xsl:key name="byId" match="rdf:Description" use="@rdf:about"/>
    
    <xsl:strip-space elements="*"/>
    
    <xsl:variable name="apos">'  </xsl:variable>
    <xsl:variable name="quot">"&#13;&#10;&#09;</xsl:variable>
        
    <xsl:template match="/">
    <xsl:text>[</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="rdf:Description[rdf:type/@rdf:resource ='http://www.openannotation.org/ns/Annotation' or rdf:type/@rdf:resource='http://www.openannotation.org/ns/Reply']">
        <xsl:text>{</xsl:text>
        
        <!--  annotation id -->
        <xsl:text>&#10;&#x9;id: </xsl:text>
        <xsl:text>"</xsl:text><xsl:value-of select="@rdf:about"/><xsl:text>"</xsl:text>
        
        <!--  annotation type(s) -->
        <xsl:text>,&#10;&#9;type: [</xsl:text>
            <xsl:for-each select="rdf:type">
                <xsl:text>"</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>"</xsl:text>
                <xsl:if test="position()!=last()">,</xsl:if>
            </xsl:for-each>
        <xsl:text>]</xsl:text>
        
        <!--  creator(s) -->
        <xsl:text>,&#10;&#9;creator: [</xsl:text>
        <xsl:apply-templates select="dcterms:creator"/>
        <xsl:text>&#10;&#9;]</xsl:text>
        
        <!--  title -->
        <xsl:if test="dc:title">
        <xsl:text>,&#10;&#9;title: "</xsl:text><xsl:value-of select="translate(dc:title[1],$quot,$apos)"/><xsl:text>"</xsl:text>
        </xsl:if>
        
        <!--  created, modified -->
        <xsl:if test="dcterms:created">
        <xsl:text>,&#10;&#9;created: "</xsl:text><xsl:value-of select="dcterms:created[1]"/><xsl:text>"</xsl:text>
        </xsl:if>
        <xsl:if test="dcterms:modified">
        <xsl:text>,&#10;&#9;modified: "</xsl:text><xsl:value-of select="dcterms:modified[1]"/><xsl:text>"</xsl:text>
        </xsl:if>
        
        <!-- targets -->
        <xsl:text>,&#10;&#9;target: [</xsl:text>
        <xsl:apply-templates select="oac:hasTarget"/>
        <xsl:text>&#10;&#9;]</xsl:text>
        
        <!--  bodies -->
        <xsl:text>,&#10;&#9;body: [</xsl:text>
        <xsl:apply-templates select="oac:hasBody"/>
        <xsl:text>&#10;&#9;]</xsl:text>
        
        <!--  any other properties of the annotation -->
        <xsl:variable name="otherprops">
            <xsl:for-each select="*[local-name(.)!='hasTarget' and local-name(.)!='hasBody' and local-name(.)!='type' and local-name(.)!='creator' and local-name(.)!='created' and local-name(.)!='modified' and local-name(.)!='title']">
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
                <xsl:text>&#10;&#9;&#9;&#9;{name: "</xsl:text><xsl:value-of select="name(.)"/><xsl:text>", value: "</xsl:text>
                <xsl:copy-of select="translate($contents,$quot,$apos)"/><xsl:text>"}</xsl:text>
                <xsl:if test="position()!=last()">,</xsl:if>
        </xsl:for-each>
        </xsl:variable>
        <xsl:if test="$otherprops != ''">
            <xsl:text>,&#10;&#9;&#9;properties: [</xsl:text>
                <xsl:copy-of select="$otherprops"/>
            <xsl:text>&#10;&#9;&#9;]</xsl:text>
        </xsl:if>
        <xsl:text>&#10;}</xsl:text>
        <xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>
    

    <xsl:template match="dcterms:creator">
        <xsl:variable name="creator" select="@rdf:resource | @rdf:nodeID"/>
        <xsl:text>&#10;&#9;{</xsl:text>
            <!--  creator id -->
            <xsl:text>&#10;&#9;&#9;id: "</xsl:text><xsl:value-of select="$creator"/><xsl:text>"</xsl:text>
            <!--  name -->
            <xsl:variable name="cname"><xsl:value-of select="//rdf:Description[@rdf:about=$creator or @rdf:nodeID=$creator]/foaf:name"/></xsl:variable>
            <xsl:if test="$cname">
            <xsl:text>,&#10;&#9;&#9;name: "</xsl:text><xsl:value-of select="translate($cname,$quot,$apos)"/><xsl:text>"</xsl:text>
            </xsl:if>
        <xsl:text>&#10;&#9;}</xsl:text>
    </xsl:template>
    
    <xsl:template match="oac:hasBody | oac:hasTarget">
    <xsl:text>&#10;&#9;{</xsl:text>
        <!--  resource id -->
        <xsl:text>&#10;&#9;&#9;id: "</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>"</xsl:text>
        <xsl:variable name="body" select="key('byId', @rdf:resource)[1]"/>

        <!--  part of -->
        <xsl:if test="$body/dcterms:isPartOF">
            <xsl:text>,&#10;&#9;&#9;&#9;partOf: "</xsl:text>
            <xsl:value-of select="$body/dcterms:isPartOf[1]"/>
            <xsl:text>"</xsl:text>
        </xsl:if>
        
        <!--  resource types -->
        <xsl:if test="$body/rdf:type">
            <xsl:text>,&#10;&#9;&#9;type: [</xsl:text>
            <xsl:for-each select="$body/rdf:type">
                <xsl:text>"</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>"</xsl:text>
            </xsl:for-each>
            <xsl:text>]</xsl:text>
        </xsl:if>
        <xsl:if test="$body/dc:format">
            <xsl:text>,&#10;&#9;&#9;format: "</xsl:text><xsl:value-of select="$body/dc:format"/><xsl:text>"</xsl:text>
        </xsl:if>
        
        <!-- constrains -->
        <xsl:if test="$body/oac:constrains">
            <xsl:text>,&#10;&#9;&#9;constrains: "</xsl:text><xsl:value-of select="$body/oac:constrains/@rdf:resource"/><xsl:text>"</xsl:text>
        </xsl:if>
        <xsl:if test="$body/oac:constrainedBy">
            <xsl:text>,&#10;&#9;&#9;constrainedBy: {</xsl:text>
            <xsl:text>&#10;&#9;&#9;&#9;id: "</xsl:text><xsl:value-of select="$body/oac:constrainedBy/@rdf:resource"/><xsl:text>"</xsl:text>
            <xsl:variable name="constraint" select="key('byId', $body/oac:constrainedBy/@rdf:resource)[1]"/>
            <xsl:if test="$constraint/rdf:type">
                <xsl:text>,&#10;&#9;&#9;&#9;type: [</xsl:text>
                <xsl:for-each select="$constraint/rdf:type">
                    <xsl:text>"</xsl:text><xsl:value-of select="@rdf:resource"/><xsl:text>"</xsl:text>
                    <xsl:if test="position()!=last()">,</xsl:if>
                </xsl:for-each>
                <xsl:text>]</xsl:text>
            </xsl:if>
            <xsl:if test="$constraint/dc:format">
                <xsl:text>,&#10;&#9;&#9;&#9;format: "</xsl:text><xsl:value-of select="$constraint/dc:format"/><xsl:text>"</xsl:text>
            </xsl:if>
            <!-- properties of the constraint -->
            <xsl:variable name="constraintprops">
                <xsl:for-each select="$constraint/*[local-name(.) != 'type' and local-name(.)!='format']">
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
                    <xsl:text>&#10;&#9;&#9;&#9;&#9;{name: "</xsl:text><xsl:value-of select="name(.)"/><xsl:text>", value: "</xsl:text>
                    <xsl:copy-of select="translate($contents,$quot,$apos)"/><xsl:text>"}</xsl:text>
                    <xsl:if test="position()!=last()">,</xsl:if>
                </xsl:for-each>
            </xsl:variable>
            <xsl:if test="$constraintprops != ''">
                <xsl:text>,&#10;&#9;&#9;&#9;properties: [</xsl:text>
                    <xsl:copy-of select="$constraintprops"/>
                <xsl:text>&#10;&#9;&#9;&#9;]</xsl:text>
            </xsl:if>
            <xsl:text>&#10;&#9;&#9;}</xsl:text>
        </xsl:if>
        <!-- inline content -->
        <xsl:if test="$body/cnt:chars">
            <xsl:text>,&#10;&#9;&#9;chars: "</xsl:text><xsl:value-of select="translate($body/cnt:chars,$quot,$apos)"/><xsl:text>"</xsl:text>
        </xsl:if>
        <!--  any other properties of the target or body resource -->
        <xsl:variable name="otherprops">
            <xsl:for-each select="$body/*[local-name(.)!='isPartOf' and local-name(.)!='type' and local-name(.)!='format' and local-name(.)!='constrains' and local-name(.)!='constrainedBy' and local-name(.)!='chars']">
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
                <xsl:text>&#10;&#9;&#9;&#9;{name: "</xsl:text><xsl:value-of select="name(.)"/><xsl:text>", value: "</xsl:text>
                <xsl:copy-of select="translate($contents,$quot,$apos)"/><xsl:text>"}</xsl:text>
                <xsl:if test="position()!=last()">,</xsl:if>
        </xsl:for-each>
        </xsl:variable>
        <xsl:if test="$otherprops != ''">
            <xsl:text>,&#10;&#9;&#9;properties: [</xsl:text>
                <xsl:copy-of select="$otherprops"/>
            <xsl:text>&#10;&#9;&#9;]</xsl:text>
        </xsl:if>
    <xsl:text>&#10;&#9;}</xsl:text>
    <xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>
    
    <!--  suppress default template for other things -->
    <xsl:template match="rdf:Description"/>
    
</xsl:stylesheet>