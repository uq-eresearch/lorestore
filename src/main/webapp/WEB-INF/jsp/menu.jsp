<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="${secure}/index.html">
                <img src="${images}/lorestorelogo.png"/>
            </a>
            <div>
                <ul class="nav">
                    <li><a href="${secure}/search.html">Search</a></li>
                    <li><a href="${secure}/validate.html">Validation</a></li>
                    <li><a href="${secure}/acknowledgements.html">Acknowledgements</a></li>
                    <li><a href="${secure}/docs.html">Documentation</a></li>
                </ul>
                <c:choose>
                    <c:when test='${pageContext.request.userPrincipal == null}'>
                      <c:if test='${sec == "drupal"}'>
                        <ul class="pull-right nav secondary-nav"><li><a href="http://${drupal}/user">Sign in</a></li></ul>
                      </c:if>
                      <c:if test='${sec == "lorestore" }'>
                        <form name="f1" action='${secure}/j_spring_security_check' method='POST' class="navbar-form pull-right">
                            <input class="input-small" type="text" name='j_username' placeholder="Username"/>
                            <input class="input-small" type="password" name='j_password' placeholder="Password"/>
                            <button class="btn" type="submit">Sign in</button>
                        </form>
                       </c:if>
                    </c:when>
                    <c:otherwise>
                        <security:authorize ifAllGranted="ROLE_USER">
                            <ul class="pull-right nav secondary-nav">
                                <li class="dropdown" data-dropdown="dropdown">
                                    <a class="dropdown-toggle" href="#">
                                        <security:authentication property="principal.username"/>
                                        <b class="caret"></b>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a href="${secure}/account/index.html">My Account</a></li>
                                        <security:authorize ifAllGranted="ROLE_ADMIN">
                                            <li class="divider"></li>
                                            <li><a href="${secure}/oreadmin/">Content Management</a></li>
                                            <c:if test="${sec != 'drupal'}"><li><a href="${secure}/emmet/index.html">User Management</a></li></c:if>
                                        </security:authorize>
                                        <c:if test="${sec != 'drupal'}">
                                        <li class="divider"></li>
                                        <li><a href="${emmet}?action=initiateLogout">Sign out</a></li>
                                        </c:if>
                                    </ul>
                                </li>
                            </ul>
                        </security:authorize>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
