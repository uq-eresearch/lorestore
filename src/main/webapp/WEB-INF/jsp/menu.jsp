 <div class="topbar">
  <div class="fill">
    <div class="container">
      <a class="brand" href="${secure}/index.html"><img src="${images}/lorestorelogo.png"/></a>
      <ul class="nav">
        <li><a href="${secure}/search.html">Search</a></li>
        <li><a href="${secure}/acknowledgements.html">Acknowledgements</a></li>
        
		</ul>
		<c:choose>
         <c:when test='${pageContext.request.userPrincipal == null}'>
             <form name="f1" action='${secure}/j_spring_security_check' method='POST' class="pull-right">
                <input class="input-small" type="text" name='j_username' placeholder="Username">
                <input class="input-small" type="password" name='j_password' placeholder="Password">
                <button class="btn" type="submit">Sign in</button>
        		<!--input type='checkbox' name='_spring_security_remember_me'/-->
              </form>
         </c:when>
        <c:otherwise>
			<security:authorize ifAllGranted="ROLE_USER">
				<ul class="pull-right nav secondary-nav">
                <li class="dropdown" data-dropdown="dropdown">
					<a class="dropdown-toggle" href="#">
						<security:authentication property="principal.username"/>
					</a>
                  <ul class="dropdown-menu">
                    <li><a href="${secure}/account/index.html">My Account</a></li>
					<!--li><a href="${secure}/help.html">Help</a></li-->
					<security:authorize ifAllGranted="ROLE_ADMIN">
						<li class="divider"></li>
            			<li><a href="${secure}/oreadmin/">Content Management</a></li>
            		   	<li><a href="${secure}/emmet/index.html">User Management</a></li>
            		</security:authorize>
                    <li class="divider"></li>
                    <li><a href="${emmet}?action=initiateLogout">Sign out</a></li>
                  </ul>
                </li>
              </ul>
				
            </security:authorize>
		</c:otherwise>
		</c:choose>
      
	  
    </div>
  </div>
</div>