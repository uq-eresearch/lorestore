package net.metadata.openannotation.lorestore.security.drupal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

public class DrupalSessionAuthenticationFilter extends GenericFilterBean
		implements ApplicationEventPublisherAware {

	public static final String METHOD_LOGIN_CHECK = "check_login";
	
	// Drupal Session Cookies are named according to host name
	// re http://api.drupal.org/api/drupal/includes!bootstrap.inc/function/drupal_settings_initialize/7
	private String drupalCookiePrefix = "SESS";
	private String drupalCookieName;
	private String drupalHostname;
	private String xmlrpcEndpoint = "http://127.0.0.1/xmlrpc.php";
	
	@Override
	public void initFilterBean() {
        Assert.notNull(drupalHostname, "drupalHostname must be specified");
        Assert.notNull(xmlrpcEndpoint, "xmlrpcEndpoint must be specified");
		this.drupalCookieName = drupalCookiePrefix + DigestUtils.sha256Hex(this.drupalHostname).substring(0, 32);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (SecurityContextHolder.getContext().getAuthentication() == null ||
        		SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
        	Authentication drupalAuth = checkDrupalAuth(request);
        	
        	if (drupalAuth != null) {
    			SecurityContextHolder.getContext().setAuthentication(drupalAuth);
	        	if (logger.isDebugEnabled()) {
	        		logger.debug("SecurityContextHolder populated with drupal-auth token: '"
	        				+ SecurityContextHolder.getContext().getAuthentication() + "'");
	        	}
        	}
            chain.doFilter(request, res);
        } else {
        	Authentication drupalAuth = checkDrupalAuth(request);
        	
        	if (drupalAuth == null) {
        		SecurityContextHolder.getContext().setAuthentication(null);
        	}
        	
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated with drupal auth token, as it already contained: '"
                    + SecurityContextHolder.getContext().getAuthentication() + "'");
            }

            chain.doFilter(request, res);
        	
        }
        
	}

	private Authentication checkDrupalAuth(HttpServletRequest request) {
		Cookie cookie = getDrupalSessionCookie(request);
		if (cookie != null) {
			DrupalUser principal = validateSession(cookie.getValue());
			
			if (principal == null) {
				return null;
			}
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new GrantedAuthorityImpl("ROLE_ORE"));
			authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			
			if (principal.isAdministrator()) {
				authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
			}
			
			Authentication auth = new DrupalAuthenticationToken(principal, authorities);
			auth.setAuthenticated(true);
			return auth;
			
		}
		return null;
	}

	private Cookie getDrupalSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
        	return null;
        }
        for (Cookie cookie : cookies) {
        	if (cookie.getName().equals(drupalCookieName)) {
        		return cookie;
        	}
        }
        
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private DrupalUser validateSession(String sid) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(xmlrpcEndpoint));
		} catch (MalformedURLException e) {
			return null;
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params = new Object[]{sid};
	    try {
			Map<String, Object> map = (Map<String, Object>)client.execute(METHOD_LOGIN_CHECK, params);
			Boolean valid = (Boolean) map.get("valid");
			Boolean admin = (Boolean) map.get("admin");
			
			if (valid) {
				Map<String, Object> user = (Map<String, Object>) map.get("user");
				
				DrupalUser principal = new DrupalUser();
				principal.setName((String)user.get("name"));
				principal.setUsername((String)user.get("name"));
				principal.setUid((String)user.get("uid"));
				principal.setAdministrator(admin);
				principal.setDrupalHostname(drupalHostname);
				return principal;
			}
			
			
		} catch (XmlRpcException e) {
			return null;
		}
	      
		return null;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		// TODO Auto-generated method stub

	}
	public String getDrupalCookieName() {
		return drupalCookieName;
	}
	public String getDrupalHostname() {
		return drupalHostname;
	}
	public void setDrupalHostname(String drupalHostname) {
		this.drupalHostname = drupalHostname;
	}
	public String getXmlrpcEndpoint() {
		return xmlrpcEndpoint;
	}
	public void setXmlrpcEndpoint(String xmlrpcEndpoint) {
		this.xmlrpcEndpoint = xmlrpcEndpoint;
	}

}
