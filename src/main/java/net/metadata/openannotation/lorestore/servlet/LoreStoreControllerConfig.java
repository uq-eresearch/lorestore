package net.metadata.openannotation.lorestore.servlet;

import net.metadata.openannotation.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.access.LoreStoreIdentityProvider;
import net.metadata.openannotation.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.openannotation.lorestore.util.UIDGenerator;


public class LoreStoreControllerConfig {

	private LoreStoreAccessPolicy accessPolicy;
	private TripleStoreConnectorFactory cf;
	private String baseUri;
	private UIDGenerator uidGenerator;
	private LoreStoreIdentityProvider ip;
	private String defaultStylesheet;
	private String defaultSchema;

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
	
	public String getDefaultSchema(){
		return this.defaultSchema;
	}
	
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
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
