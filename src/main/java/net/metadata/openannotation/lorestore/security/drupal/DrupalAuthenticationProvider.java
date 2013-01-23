package net.metadata.openannotation.lorestore.security.drupal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import au.edu.diasb.springsecurity.UserIdentityDetailsService;

public class DrupalAuthenticationProvider implements AuthenticationProvider,
        AuthenticationFailureHandler, InitializingBean {
	public static final String DEFAULT_IDENTITY_DOMAIN = "shibboleth";
	
	private UserDetailsService userDetailsService;
	private UserIdentityDetailsService userIdentityDetailsService;
	private String domain;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
    public void afterPropertiesSet() {
        if (!(userDetailsService == null ^ userIdentityDetailsService == null)) {
            throw new IllegalArgumentException(
                    "Exactly one of 'userDetailsService' and 'userIdentityDetailsService' " +
                    "must be set");
        }
        if (domain == null) {
            domain = DEFAULT_IDENTITY_DOMAIN;
        }
    }

    @Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
    	if (!supports(auth.getClass())) {
	        return null;
	    }
	    UserDetails details = userDetailsService != null ? 
	            userDetailsService.loadUserByUsername(auth.getName()) :
	            userIdentityDetailsService.loadUserByIdentityUri(auth.getName(), domain);
	    return new DrupalAuthenticationToken(details, details.getAuthorities(), details,
	    		null);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public UserIdentityDetailsService getUserIdentityDetailsService() {
        return userIdentityDetailsService;
    }

    public void setUserIdentityDetailsService(
            UserIdentityDetailsService userIdentityDetailsService) {
        this.userIdentityDetailsService = userIdentityDetailsService;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
	public boolean supports(Class<? extends Object> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
	}
}
