package net.metadata.auselit.lorestore.servlet;

import net.metadata.auselit.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.auselit.lorestore.access.LoreStoreIdentityProvider;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.util.UIDGenerator;


public class LoreStoreControllerConfig {

	private LoreStoreAccessPolicy accessPolicy;
	private TripleStoreConnectorFactory cf;
	private String baseUri;
	private UIDGenerator uidGenerator;
	private LoreStoreIdentityProvider ip;
	private String defaultStylesheet;

	public void setAccessPolicy(LoreStoreAccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	
	public LoreStoreAccessPolicy getAccessPolicy() {
		return accessPolicy;
		
	}

	public void setContainerFactory(TripleStoreConnectorFactory cf) {
		this.cf = cf;
	}

	public TripleStoreConnectorFactory getContainerFactory() {
		return cf;
	}

	public String getBaseUri() {
		return this.baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public String getDefaultStylesheet(){
		return this.defaultStylesheet;
	}
	
	public void setDefaultStylesheet(String defaultStylesheet){
		this.defaultStylesheet = defaultStylesheet;
	}
	
	public void setUidGenerator(UIDGenerator uidGenerator) {
		this.uidGenerator = uidGenerator;
	}

	public UIDGenerator getUidGenerator() {
		return uidGenerator;
	}

	public void setIdentityProvider(LoreStoreIdentityProvider ip) {
		this.ip = ip;
	}

	public LoreStoreIdentityProvider getIdentityProvider() {
		return ip;
	}

}
