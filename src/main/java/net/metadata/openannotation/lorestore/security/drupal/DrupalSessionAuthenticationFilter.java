package net.metadata.openannotation.lorestore.security.drupal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class DrupalSessionAuthenticationFilter extends GenericFilterBean
		implements ApplicationEventPublisherAware {

	public static final String METHOD_LOGIN_CHECK = "check_login";
	
	// Drupal Session Cookies are named according to host name
	// re http://api.drupal.org/api/drupal/includes!bootstrap.inc/function/drupal_settings_initialize/7
	private String drupalCookiePrefix = "SESS";
	private String xmlrpcEndpoint = "http://127.0.0.1/xmlrpc.php";
    private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
        	Authentication drupalAuth = checkDrupalAuth(request);
        	
        	if (drupalAuth != null) {
	        	if (logger.isDebugEnabled()) {
	        		logger.debug("SecurityContextHolder populated with drupal-auth token: '"
	        				+ SecurityContextHolder.getContext().getAuthentication() + "'");
	        	}
        	}
            chain.doFilter(request, res);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated with drupal auth token, as it already contained: '"
                    + SecurityContextHolder.getContext().getAuthentication() + "'");
            }

            chain.doFilter(request, res);
        	
        }
        
	}

	private Authentication checkDrupalAuth(HttpServletRequest request) {
		Cookie cookie = getDrupalSessionCookie(request);
		if (validateSession(cookie.getValue())) {
			
		}
		return null;
	}

	private Cookie getDrupalSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
        	if (cookie.getName().startsWith(drupalCookiePrefix)) {
        		return cookie;
        	}
        }
        
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private boolean validateSession(String sid) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(xmlrpcEndpoint));
		} catch (MalformedURLException e) {
			return false;
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params = new Object[]{sid};
	    try {
			Map<String, Object> map = (Map<String, Object>)client.execute(METHOD_LOGIN_CHECK, params);
			Boolean valid = (Boolean) map.get("valid");
			
			if (valid) {
				Map<String, Object> user = (Map<String, Object>) map.get("user");
				return true;
			}
			
			
		} catch (XmlRpcException e) {
			return false;
		}
	      
		return false;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		// TODO Auto-generated method stub

	}

}
