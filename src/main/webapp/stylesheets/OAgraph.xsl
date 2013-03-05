<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:oac="http://www.openannotation.org/ns/"
    xmlns:oa="http://www.w3.org/ns/oa#"
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
                <title>OA Annotations</title>
                <link type="text/css" rel="stylesheet" href="../stylesheets/bootstrap.min.css"/>
                <link rel="stylesheet" href="../prettify/prettify.css" type="text/css"/>
                <style type="text/css">
                    .annotation {padding-bottom:1em; border-bottom:1px dotted #eeeeee;}
                    .page-header {border-bottom: none;}
                    tspan, text { font-size:10px;};
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
                    var renderFunc = function(r, n) {
                        var frame, text, defaultColor, defaultStrokeColor;
                        if (n.nodeType == "literal"){
                            var rectLength = (n.label? (n.label.length * 7) : n.id.length * 7);
                            frame = r.rect(n.point[0] - (rectLength / 2), n.point[1] - 13, rectLength, 34, 5);
                            text = r.text(n.point[0], n.point[1] + 5, (n.label || n.id)); 
                            defaultColor = "#999999";
                        } else if (n.nodeType == "type") {
                            var rectLength = (n.label? (n.label.length * 7) : n.id.length * 7);
                            frame = r.rect(n.point[0] - (rectLength / 2), n.point[1] - 13, rectLength, 34);
                            text = r.text(n.point[0], n.point[1] + 5, (n.label || n.id)); 
                            defaultColor = "white";
                            defaultStrokeColor = "black";
                        } else {
                            defaultColor = (n.nodeType == "target"? "red" : 
                                (n.nodeType == "body"? "blue": 
                                    (n.nodeType == "constraint"? "orange": 
                                        (n.nodeType == "user"? "green" : 
                                            "#eeeeee"))));
                            frame = r.ellipse(0, 0, 30, 20);
                            text = r.text(0, 30, n.label || n.id);
                        }
                        
                        frame.attr({
                            'fill': n.color || defaultColor,
                            'stroke-width' : '1px',
                            'stroke': n.color || defaultStrokeColor || defaultColor
                          });
                        var set = r.set()
                          .push(
                            frame, text
                          );
                        return set;
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
                    <xsl:if test="count(/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'openannotation.org/ns/')]) = 0 and count(/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'w3.org/ns/oa')]) = 0">
                        <p style="margin-top:2em">No matching annotations</p>
                    </xsl:if>
                    
                    <xsl:for-each select="/rdf:RDF/rdf:Description[contains(rdf:type/@rdf:resource,'w3.org/ns/oa#Annotation') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/Annotation') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/Reply') or contains(rdf:type/@rdf:resource,'openannotation.org/ns/DataAnnotation')]">
                       <xsl:variable name="annoUri" select="@rdf:about"/>
                       <xsl:variable name="shortId" select="generate-id()"/>
                       <xsl:variable name="annotype">
                            <xsl:choose>
                                <xsl:when test="contains(rdf:type/@rdf:resource,'openannotation.org/ns/Annotation')">
                                    <xsl:value-of select="substring-after(rdf:type/@rdf:resource, '/ns/')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="substring-after(rdf:type/@rdf:resource, '#')"/>
                                </xsl:otherwise>
                             </xsl:choose>
                        </xsl:variable>
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
                       <!--  graph visualization canvas -->
                       <div><button class="btn btn-mini" onclick="redraw('{$shortId}');">Redraw</button></div>
                       <div id="{$shortId}-canvas"></div>
                       <script type="text/javascript">
                        $(document).ready(function() {
                            var width = 940;
                            var height = 600;
                            var g = new Graph();
                            g.edgeFactory.template.style.directed = true;
                            g.addNode("Annotation", {render: renderFunc, color: "yellow"});
                            <xsl:call-template name="generateNodesAndEdges"/>
                            var layouter = new Graph.Layout.Spring(g);
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
        <!--  used to escape quotes in labels -->
        <xsl:variable name="apos">'  </xsl:variable>
        <xsl:variable name="quot">"&#13;&#10;&#09;</xsl:variable>
        
        <xsl:for-each select="$context/*[not(name()='rdf:type' and (@rdf:resource='http://www.openannotation.org/ns/Body' or @rdf:resource='http://www.openannotation.org/ns/Target' or @rdf:resource='http://www.w3.org/ns/oa#Body' or @rdf:resource='http://www.w3.org/ns/oa#Target'))]">
            <xsl:variable name="valref" select="@rdf:resource | @rdf:nodeID"/>
            <xsl:variable name="valId" select="generate-id()"/>
            <xsl:variable name="propName" select="name()"/>
            <xsl:choose>
                <xsl:when test="$valref">
                <!--  URI or URN -->
                    <xsl:variable name="displayName">
                        <xsl:choose>
                            <xsl:when test="starts-with($valref,'http://www.openannotation.org/ns')">oac:<xsl:value-of select="substring-after($valref,'http://www.openannotation.org/ns/')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'http://www.w3.org/ns/oa#')">oa:<xsl:value-of select="substring-after($valref,'http://www.w3.org/ns/oa#')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'http://www.w3.org/2011/content#')">cnt:<xsl:value-of select="substring-after($valref,'http://www.w3.org/2011/content#')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'http://xmlns.com/foaf/0.1/')">foaf:<xsl:value-of select="substring-after($valref,'http://xmlns.com/foaf/0.1/')"/></xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and local-name()='hasBody'">body</xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and local-name()='hasTarget'">target</xsl:when>
                            <xsl:when test="starts-with($valref,'urn') and local-name()='constrainedBy'">constraint</xsl:when>
                            <xsl:when test="starts-with($valref,'http') and string-length($valref) &gt; 25">...<xsl:value-of select="substring(substring-after($valref,'http://'),(string-length($valref)-25))"/></xsl:when>
                            <xsl:otherwise><xsl:value-of select="$valref"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="nodeType">
                        <xsl:choose>
                            <xsl:when test="local-name()='hasBody'">body</xsl:when>
                            <xsl:when test="local-name()='hasTarget'">target</xsl:when>
                            <xsl:when test="local-name()='constrainedBy'">constraint</xsl:when>
                            <xsl:when test="local-name()='type'">type</xsl:when>
                            <xsl:when test="local-name()='creator'">user</xsl:when>
                            <xsl:otherwise></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    g.addNode("<xsl:value-of select="$valId"/>", {render: renderFunc, nodeType: "<xsl:value-of select="$nodeType"/>", label: "<xsl:value-of select="translate($displayName,$quot,$apos)"/>"});
                </xsl:when>
                <xsl:otherwise>
                <!--  literal -->
                    <xsl:variable name="val">
                        <xsl:choose>
                            <xsl:when test="name()='created' or name()='modified'">
                                <xsl:value-of select="substring(.,9,2)"/>/<xsl:value-of select="substring(.,6,2)"/>/<xsl:value-of select="substring(.,1,4)"/>
                            </xsl:when>
                            <xsl:when test="string-length(.) &gt; 25"><xsl:value-of select="substring(.,0,23)"/>...</xsl:when>
                            <xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>    
                        </xsl:choose>
                    </xsl:variable>
                    g.addNode("<xsl:value-of select="$valId"/>", {render: renderFunc, nodeType: "literal", label: "<xsl:value-of select="translate($val,$quot,$apos)"/>"});
                </xsl:otherwise>
            </xsl:choose>
            <xsl:variable name="labelName">
                <xsl:choose>
                    <xsl:when test="namespace-uri()='http://www.openannotation.org/ns/'">oac:</xsl:when>
                    <xsl:when test="namespace-uri()='http://www.w3.org/ns/oa#'">oa:</xsl:when>
                    <xsl:when test="namespace-uri()='http://www.w3.org/2011/content#'">cnt:</xsl:when>
                    <xsl:when test="namespace-uri()='http://xmlns.com/foaf/0.1/'">foaf:</xsl:when>
                    <xsl:when test="namespace-uri()='http://purl.org/dc/terms/'">dct:</xsl:when>
                    <xsl:when test="namespace-uri()='http://purl.org/dc/elements/1.1/'">dc:</xsl:when>
                    
                </xsl:choose>
                <xsl:value-of select="name()"/>
            </xsl:variable>
  
            g.addEdge("<xsl:value-of select="$sourceName"/>", "<xsl:value-of select="$valId"/>", {label : "<xsl:value-of select="translate($labelName,$quot,$apos)"/>" });
            <xsl:call-template name="generateNodesAndEdges">
                <xsl:with-param name="sourceName" select="$valId"/>
                <xsl:with-param name="context" select="//rdf:Description[@rdf:about=$valref or @rdf:nodeID=$valref]"/>
            </xsl:call-template>
            
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
