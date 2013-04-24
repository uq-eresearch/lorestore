<footer>
<div class="container">
<p class="pull-right">
<c:if test="${sec != 'drupal'}"><a href="${secure}/resetMyPassword.html">Forgotten Your Password?</a></c:if>
</p>
</div>
</footer>
<!--  script type="text/javascript" src="${secure}/jquery-1.7.1.min.js"></script-->
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="${secure}/bootstrap-dropdown.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function(){
       jQuery('.dropdown-toggle').dropdown()
    });
</script>