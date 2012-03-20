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
                    tspan, text { max-width:40px; font-size:10px; overflow:hidden; text-overflow:ellipsis;
                   /* white-space: pre; white-space: pre-wrap; white-space: pre-line; white-space: -moz-pre-wrap;*/
                    };
                </style>
                <script type="text/javascript" src="../graphdracula/raphael-min.js"></script>
                <script type="text/javascript" src="../graphdracula/dracula_graffle.js"></script>
                <script src="../jquery-1.7.1.min.js" type="text/javascript"></script>
                <script type="text/javascript" src="../graphdracula/dracula_graph.js"></script>
                <script type="text/javascript" src="../graphdracula/dracula_algorithms.js"></script>
                <script type="text/javascript">
                    var redraw = function(annoid) {
                        $('#' + annoid + '-canvas').data('data-layouter').layout();
                        $('#' + annoid + '-canvas').data('data-renderer').draw();
                    };
                </script>
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
                    <xsl:if test="count(/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org/ns/')]) = 0">
                        <p style="margin-top:2em">No matching annotations</p>
                    </xsl:if>
                    
                    <xsl:for-each select="/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org/ns/Annotation') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/Reply') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/DataAnnotation')]">
                       <xsl:variable name="annoUri" select="@rdf:about"/>
                       <xsl:variable name="shortId" select="generate-id()"/>
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
                       </div>
                       </div>
                       <!--  graph visualisation canvas -->
                       <div><button class="btn btn-mini" onclick="redraw('{$shortId}');">Redraw</button></div>
                       <div id="{$shortId}-canvas"></div>
                       <script type="text/javascript">
                        $(document).ready(function() {
                            var width = 940; //$(document).width();
                            var height = 600; //$(document).height();
                            var g = new Graph();
                            g.edgeFactory.template.style.directed = true;
                            g.addNode("Annotation");
                            <xsl:call-template name="generateNodesAndEdges"/>
                            
                            var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
                            var renderer = new Graph.Renderer.Raphael('<xsl:value-of select="$shortId"/>-canvas', g, width, height);
                            $('#<xsl:value-of select="$shortId"/>-canvas').data('data-layouter',layouter);
                                
                            $('#<xsl:value-of select="$shortId"/>-canvas').data('data-renderer',renderer);
                            redraw('<xsl:value-of select="$shortId"/>');
                            
                        });
                    </script>
                    </xsl:for-each>
                </div>
                <p><small>Use browser <em>View Page Source</em> to see underlying annotation RDF</small></p>
                </div>
                
                <script src="../bootstrap-tab.js" type="text/javascript"></script>
                
            </body>
        </html>

    </xsl:template>
    <xsl:template name="generateNodesAndEdges">
        <xsl:param name="context" select="."/>
        <xsl:param name="sourceName">Annotation</xsl:param>
        <xsl:for-each select="$context/*[not(name()='rdf:type' and (@rdf:resource='http://www.openannotation.org/ns/Body' or @rdf:resource='http://www.openannotation.org/ns/Target'))]">
            <xsl:variable name="valref" select="@rdf:resource | @rdf:nodeID"/>
            <xsl:variable name="valId" select="generate-id()"/>
            <xsl:variable name="propName" select="name()"/>
            <xsl:choose>
                <xsl:when test="$valref">
                    <xsl:variable name="displayName">
                        <xsl:choose>
                            <xsl:when test="starts-with($valref,'http://www.openannotation.org/ns')">oac:<xsl:value-of select="substring-after($valref,'http://www.openannotation.org/ns/')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'http://www.w3.org/2011/content#')">cnt:<xsl:value-of select="substring-after($valref,'http://www.w3.org/2011/content#')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'http://xmlns.com/foaf/0.1/')">foaf:<xsl:value-of select="substring-after($valref,'http://xmlns.com/foaf/0.1/')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and name()='hasBody'">body</xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and name()='hasTarget'">target</xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and name()='constrainedBy'">constraint</xsl:when>
                            <xsl:when test="starts-with($valref,'http') and string-length($valref) &gt; 25">...<xsl:value-of select="substring(substring-after($valref,'http://'),(string-length($valref)-25))"/></xsl:when>
                            <xsl:otherwise><xsl:value-of select="$valref"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    g.addNode("<xsl:value-of select="$valId"/>", {label: '<xsl:value-of select="$displayName"/>'});
                </xsl:when>
                <xsl:otherwise>
                <xsl:variable name="val">
                    <xsl:choose>
                        <xsl:when test="name()='created' or name()='modified'">
                            <xsl:value-of select="substring(.,9,2)"/>/<xsl:value-of select="substring(.,6,2)"/>/<xsl:value-of select="substring(.,1,4)"/>
                        </xsl:when>
                        <xsl:when test="string-length(.) &gt; 25"><xsl:value-of select="substring(.,0,23)"/>...</xsl:when>
                        <xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>    
                    </xsl:choose>
                </xsl:variable>
                    g.addNode("<xsl:value-of select="$valId"/>", {label: '<xsl:value-of select="$val"/>'});
                </xsl:otherwise>
            </xsl:choose>
            <xsl:variable name="labelName">
                <xsl:choose>
                    <xsl:when test="namespace-uri()='http://www.openannotation.org/ns/'">oac:</xsl:when>
                    <xsl:when test="namespace-uri()='http://www.w3.org/2011/content#'">cnt:</xsl:when>
                    <xsl:when test="namespace-uri()='http://xmlns.com/foaf/0.1/'">foaf:</xsl:when>
                    <xsl:when test="namespace-uri()='http://purl.org/dc/terms/'">dct:</xsl:when>
                    <xsl:when test="namespace-uri()='http://purl.org/dc/elements/1.1/'">dc:</xsl:when>
                    
                </xsl:choose>
                <xsl:value-of select="name()"/>
            </xsl:variable>
            g.addEdge("<xsl:value-of select="$sourceName"/>", "<xsl:value-of select="$valId"/>", {label : "<xsl:value-of select="$labelName"/>" });
            <xsl:call-template name="generateNodesAndEdges">
                <xsl:with-param name="sourceName" select="$valId"/>
                <xsl:with-param name="context" select="//rdf:Description[@rdf:about=$valref or @rdf:nodeID=$valref]"/>
            </xsl:call-template>
            
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
