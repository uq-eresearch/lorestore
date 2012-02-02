<%-- Common head declarations for all login forms --%>
    <meta http-equiv="Content-Type" content="text/html;charset=iso-8859-1" >
    <title>${sitename} Sign in</title>
    <link type="text/css" href="${styles}/bootstrap.min.css" rel="stylesheet">
    <link type="text/css" href="${styles}/lorestore.css" rel="stylesheet">
    <script type="text/javascript" src="${container}/jquery-1.7.1.min.js"></script>
    <script type="text/javascript">
      function addRedirect() {
          var res = window.location.toString().match(/.*\?spring-security-redirect=(.*)/);
          if (res) {
              var redirection = '?spring-security-redirect=' + res[1];
              document.f.action += redirection;
          }
      };

      var doResize = function () {
        // Temporary hack until we can figure out how to resize the window 
        // based on the content.
        var h = jQuery.browser.msie ? document.documentElement.clientHeight : 
                 window.innerHeight;
        var w = jQuery.browser.msie ? document.documentElement.window.clientWidth : 
                 window.innerWidth;
        if (h < 200 && w < 200) {
          window.resizeTo(500, 400);
        }
      };
      jQuery(window).bind('load', doResize);
    </script>
    