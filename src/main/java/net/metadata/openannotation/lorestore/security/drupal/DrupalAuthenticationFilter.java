package net.metadata.openannotation.lorestore.security.drupal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import au.edu.diasb.springsecurity.UserIdentityDetailsService;

public class DrupalAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private UserIdentityDetailsService userDetailsService;
    private DrupalDBConnection drupalDBConnection;

	private Map<String,String> realmMapping = Collections.emptyMap();
    private Set<String> returnToUrlParameters = Collections.emptySet();
    private String drupalCookiePrefix = "SESS";
    
    public DrupalAuthenticationFilter() {
        super("/j_spring_drupal_security_check");
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        if (returnToUrlParameters.isEmpty() &&
                getRememberMeServices() instanceof AbstractRememberMeServices) {
            returnToUrlParameters = new HashSet<String>();
            returnToUrlParameters.add(((AbstractRememberMeServices)getRememberMeServices()).getParameter());
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        String sid = request.getParameter("sid");
       
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
        	if (cookie.getName().startsWith(drupalCookiePrefix)) {
        		sid = cookie.getValue();
        	}
        }
        
        if (!StringUtils.hasText(sid)) {
            response.sendRedirect("http://localhost/lorestoreLogin/");

            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Supplied Drupal session identity is " + sid);
        }

        String[] queryResults = drupalDBConnection.getUserDetails(sid);
        
        String uid = queryResults[0];
        String name = queryResults[1];
        String mail = queryResults[2];
        
        UserDetails user = null;
		AbstractAuthenticationToken token;
		
		try{
			user = userDetailsService.loadUserByUsername(userDetailsService.loadUserByIdentityUri(
					uid, "drupal").getUsername());		
			
			token = new PreAuthenticatedAuthenticationToken(user, user.getAuthorities());
			token.setDetails(new DrupalAuthenticationDetails(request));
		} catch (UsernameNotFoundException e){
			ArrayList<DrupalAttribute> attrs = new ArrayList<DrupalAttribute>();
			attrs.add(new DrupalAttribute("uid", uid));
			attrs.add(new DrupalAttribute("name", name));
			attrs.add(new DrupalAttribute("mail", mail));
			// Otherwise it is loaded as a default shibboleth user
			token = new DrupalAuthenticationToken(name, null, null, attrs);
			token.setDetails(new DrupalAuthenticationDetails(request));
		}

		Authentication ar = this.getAuthenticationManager().authenticate(token);
        return ar;
    }

    protected String lookupRealm(String returnToUrl) {
        String mapping = realmMapping.get(returnToUrl);

        if (mapping == null) {
            try {
                URL url = new URL(returnToUrl);
                int port = url.getPort();

                StringBuilder realmBuffer = new StringBuilder(returnToUrl.length())
                        .append(url.getProtocol())
                        .append("://")
                        .append(url.getHost());
                if (port > 0) {
                    realmBuffer.append(":").append(port);
                }
                realmBuffer.append("/");
                mapping = realmBuffer.toString();
            } catch (MalformedURLException e) {
                logger.warn("returnToUrl was not a valid URL: [" + returnToUrl + "]", e);
            }
        }

        return mapping;
    }

    protected String buildReturnToUrl(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();

        Iterator<String> iterator = returnToUrlParameters.iterator();
        boolean isFirst = true;

        while (iterator.hasNext()) {
            String name = iterator.next();
            // Assume for simplicity that there is only one value
            String value = request.getParameter(name);

            if (value == null) {
                continue;
            }

            if (isFirst) {
                sb.append("?");
                isFirst = false;
            }
            sb.append(name).append("=").append(value);

            if (iterator.hasNext()) {
                sb.append("&");
            }
        }

        return sb.toString();
    }

    public void setRealmMapping(Map<String,String> realmMapping) {
        this.realmMapping = realmMapping;
    }

    public UserIdentityDetailsService getUserDetailsService() {
		return userDetailsService;
	}
    
	public void setUserDetailsService(UserIdentityDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

    public void setDrupalDBConnection(DrupalDBConnection drupalDBConnection) {
		this.drupalDBConnection = drupalDBConnection;
	}
	    
    public void setReturnToUrlParameters(Set<String> returnToUrlParameters) {
        Assert.notNull(returnToUrlParameters, "returnToUrlParameters cannot be null");
        this.returnToUrlParameters = returnToUrlParameters;
    }
}