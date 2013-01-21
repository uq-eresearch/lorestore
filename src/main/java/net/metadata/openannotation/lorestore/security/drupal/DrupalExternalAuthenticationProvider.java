package net.metadata.openannotation.lorestore.security.drupal;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import au.edu.diasb.springsecurity.AbstractExternalAuthenticationProvider;
import au.edu.diasb.springsecurity.ExternalProviderConfiguration;
import au.edu.diasb.springsecurity.ExternalUserDetails;

public class DrupalExternalAuthenticationProvider 
extends AbstractExternalAuthenticationProvider {

	@Override
	protected Authentication createToken(Authentication authentication,
			ExternalProviderConfiguration config) {
		DrupalAuthenticationToken token = (DrupalAuthenticationToken) authentication;
		Properties attributes = extractAttributes(token);
		UserDetails details = new ExternalUserDetails(token.getName(),
				attributes.getProperty("uid"), config.getDefaultAuthorityList(), attributes);
		return new DrupalAuthenticationToken(details, 
				config.getDefaultAuthorityList(), token.getName(), token.getAttributes());
	}

	@Override
	protected Properties extractAttributes(Authentication authentication) {
		List<DrupalAttribute> attributes =
	    	((DrupalAuthenticationToken) authentication).getAttributes();
		Properties props = new Properties();
		for (DrupalAttribute attribute : attributes) {
			String value = attribute.getValue();
			if (value != null) {
				props.setProperty(attribute.getName(), value);
			}
		}
		return props;
	}
	
	@Override
    public void setProviderConfigurations(List<ExternalProviderConfiguration> configs) {
		super.setProviderConfigurations(new ArrayList<ExternalProviderConfiguration>(configs));
	}

	@Override
	protected String extractIdentity(Authentication authentication) {
		return (String) ((DrupalAuthenticationToken) authentication).getPrincipal();
	}
	
	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return DrupalAuthenticationToken.class.isAssignableFrom(authentication);
	}

}