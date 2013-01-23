package net.metadata.openannotation.lorestore.security.drupal;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class DrupalAuthenticationDetails {

    private static final Logger LOGGER = 
        Logger.getLogger(DrupalAuthenticationFilter.class);
    
	private HttpServletRequest request;

	public DrupalAuthenticationDetails(HttpServletRequest request) {
		this.request = request;
	}

	public Properties extractAttributes() {
	    Properties props = new Properties();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Extracted drupal attributes: " + props);
        }
        return props;
    }

    @Override
    public String toString() {
        return "DrupalAuthenticationDetails{request=" + request + "}";
    }
}
