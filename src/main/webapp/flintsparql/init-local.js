$(document)
        .ready(
                function() {
                    var sampleQuery1 = "SELECT * WHERE {\n\t?c a <http://www.openarchives.org/ore/terms/ResourceMap>\n}";
                    var sampleQuery2 = "SELECT * WHERE {\n\t?a a <http://www.w3.org/ns/oa#Annotation>\n}";
                    var flintConfig = {
                        "interface" : {
                            "toolbar" : true,
                            "menu" : true
                        },
                        "namespaces" : [
                                {
                                    "name" : "Friend of a friend",
                                    "prefix" : "foaf",
                                    "uri" : "http://xmlns.com/foaf/0.1/"
                                },
                                {
                                    "name" : "XML schema",
                                    "prefix" : "xsd",
                                    "uri" : "http://www.w3.org/2001/XMLSchema#"
                                },
                                {
                                    "name" : "SIOC",
                                    "prefix" : "sioc",
                                    "uri" : "http://rdfs.org/sioc/ns#"
                                },
                                {
                                    "name" : "Resource Description Framework",
                                    "prefix" : "rdf",
                                    "uri" : "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                                },
                                {
                                    "name" : "Resource Description Framework schema",
                                    "prefix" : "rdfs",
                                    "uri" : "http://www.w3.org/2000/01/rdf-schema#"
                                },
                                {
                                    "name" : "Dublin Core",
                                    "prefix" : "dc",
                                    "uri" : "http://purl.org/dc/elements/1.1/"
                                },
                                {
                                    "name" : "Dublin Core terms",
                                    "prefix" : "dct",
                                    "uri" : "http://purl.org/dc/terms/"
                                },
                                {
                                    "name" : "Creative Commons",
                                    "prefix" : "cc",
                                    "uri" : "http://www.creativecommons.org/ns#"
                                },
                                {
                                    "name" : "Web Ontology Language",
                                    "prefix" : "owl",
                                    "uri" : "http://www.w3.org/2002/07/owl#"
                                },
                                {
                                    "name" : "Simple Knowledge Organisation System",
                                    "prefix" : "skos",
                                    "uri" : "http://www.w3.org/2004/02/skos/core#"
                                },
                                {
                                    "name" : "Geography",
                                    "prefix" : "geo",
                                    "uri" : "http://www.w3.org/2003/01/geo/wgs84_pos#"
                                },
                                {
                                    "name" : "Geonames",
                                    "prefix" : "geonames",
                                    "uri" : "http://www.geonames.org/ontology#"
                                },
                                {
                                    "name" : "DBPedia property",
                                    "prefix" : "dbp",
                                    "uri" : "http://dbpedia.org/property/"
                                },
                                {
                                    "name" : "Open Provenance Model Vocabulary",
                                    "prefix" : "opmv",
                                    "uri" : "http://purl.org/net/opmv/ns#"
                                },
                                {
                                    "name" : "Functional Requirements for Bibliographic Records",
                                    "prefix" : "frbr",
                                    "uri" : "http://purl.org/vocab/frbr/core#"
                                }

                        ],
                        "defaultEndpointParameters" : {
                            "queryParameters" : {
                                "format" : "output",
                                "query" : "sparql",
                                "update" : "update"
                            },
                            "selectFormats" : [ /*{
                                "name" : "Plain text",
                                "format" : "text",
                                "type" : "text/plain"
                            },*/ {
                                "name" : "SPARQL-XML",
                                "format" : "sparql",
                                "type" : "application/sparql-results+xml"
                            }/*, {
                                "name" : "JSON",
                                "format" : "json",
                                "type" : "application/sparql-results+json"
                            } */],
                            "constructFormats" : [ /*{
                                "name" : "Plain text",
                                "format" : "text",
                                "type" : "text/plain"
                            }, */{
                                "name" : "RDF/XML",
                                "format" : "rdfxml",
                                "type" : "application/rdf+xml"
                            }/*, {
                                "name" : "Turtle",
                                "format" : "turtle",
                                "type" : "application/turtle"
                            } */]
                        },
                        "endpoints" : [
                                {"name": "LORESTORE",
                                    "uri": "sparql",
                                    "modes": ["sparql10", "sparql11query"],
                                       queries: [
                                           {"name": "Resource Maps", 
                                            "description": "List all ORE ResourceMaps", 
                                            "query": sampleQuery1},
                                            {"name": "Annotations", 
                                                "description": "List all OA Annotations", 
                                                "query": sampleQuery2}
                                
                                       ]
                                   }
                                ],
                        "defaultModes" : [ {
                            "name" : "SPARQL 1.0",
                            "mode" : "sparql10"
                        }, {
                            "name" : "SPARQL 1.1 Query",
                            "mode" : "sparql11query"
                        }, {
                            "name" : "SPARQL 1.1 Update",
                            "mode" : "sparql11update"
                        } ]
                    };

                    var flintEd = new FlintEditor("flint-test",
                            "../flintsparql/images", flintConfig);
                });
