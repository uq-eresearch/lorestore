<%-- Common head declarations for all login forms --%>
    <meta http-equiv="Content-Type" content="text/html;charset=iso-8859-1" >
    <link rel="stylesheet" href="${styles}/emmet.css" type="text/css">
    <title>${sitename} Login Page</title>
    <script type="text/javascript" src="${container}/js/jquery-1.4.3.min.js"></script>
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
        var h = $.browser.msie ? document.documentElement.clientHeight : 
                 window.innerHeight;
        var w = $.browser.msie ? document.documentElement.window.clientWidth : 
                 window.innerWidth;
        if (h < 200 && w < 200) {
          window.resizeTo(500, 300);
        }
      };
      $(window).bind('load', doResize);
    </script>
    