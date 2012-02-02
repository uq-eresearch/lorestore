<%@ include file="/WEB-INF/jsp/oreBasicInclude.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>SPARQL Query</title>
        <%@ include file="/WEB-INF/jsp/header.jsp" %>
        <script type="text/javascript" src="/lorestore/flintsparql/lib/codemirror.js"></script>
        <script type="text/javascript" src="/lorestore/flintsparql/sparqlmode_ll1.js"></script>
        <script type="text/javascript" src="/lorestore/flintsparql/init-local.js"></script>
        <script type="text/javascript" src="/lorestore/flintsparql/flint-editor.js"></script>
        <link rel="stylesheet" href="/lorestore/flintsparql/lib/codemirror.css"/>
        <link rel="stylesheet" href="/lorestore/flintsparql/css/sparqlcolors.css"/>
        <link rel="stylesheet" href="/lorestore/flintsparql/css/docs.css"/>
    </head>
    <body>  
        <%@ include file="/WEB-INF/jsp/menu.jsp" %>
        <div class="container wide-container">
            <div class="content">
                <div class="page-header">
                    <h1>SPARQL Query</h1>
                </div>
                <div class="row">
                    <div class="span12">
                        <ul class="breadcrumb">
                            <li>
                                <a href="../index.html">Home</a>
                                <span class="divider">/</span>
                            </li>
                            <li>
                                <a href="../oreadmin/">Content management</a>
                                <span class="divider">/</span>
                            </li>
                            <li class="active">SPARQL Query</li>
                        </ul>
                        <div id="flint-test"></div>
                    </div>
                </div>
            </div>
            <%@ include file="/WEB-INF/jsp/footer.jsp" %>
        </div>
    </body>
</html>
