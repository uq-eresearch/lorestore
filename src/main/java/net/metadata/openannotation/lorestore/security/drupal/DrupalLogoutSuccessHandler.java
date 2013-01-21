package net.metadata.openannotation.lorestore.security.drupal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import au.edu.diasb.springsecurity.ConditionalLogoutSuccessHandler;

public class DrupalLogoutSuccessHandler extends ConditionalLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if ((authentication instanceof DrupalAuthenticationToken) &&
        		authentication.isAuthenticated()) {
            doRedirect(request, response);
        }
    }

    protected boolean supports(Class<? extends Object> authentication) {
        return DrupalAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
