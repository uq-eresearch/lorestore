package net.metadata.openannotation.lorestore.common;

import java.util.Properties;

import au.edu.diasb.chico.config.ConfigurationProperties;

/**
 * This is a wrapper for java.util.Properties that ensures that Ore's required properties
 * are present.
 * 
 * Based on DannoProperties
 * 
 */
public class LoreStoreProperties extends ConfigurationProperties {

	private static final long serialVersionUID = 3290631375861688974L;

	// These are properties in the property file
    public static final String DEFAULT_RDF_STYLESHEET_PROP = "lorestore.oreStylesheet";

    public LoreStoreProperties(Properties props) {
		super(props);
	}
}
