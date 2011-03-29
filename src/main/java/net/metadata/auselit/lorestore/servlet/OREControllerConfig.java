package net.metadata.auselit.lorestore.servlet;

import net.metadata.auselit.lorestore.access.OREAccessPolicy;
import net.metadata.auselit.lorestore.triplestore.TripleStoreConnectorFactory;
import net.metadata.auselit.lorestore.util.UIDGenerator;


public class OREControllerConfig {

	private OREAccessPolicy accessPolicy;
	private TripleStoreConnectorFactory cf;
	private String baseUri;
	private UIDGenerator uidGenerator;
	

	public void setAccessPolicy(OREAccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	
	public OREAccessPolicy getAccessPolicy() {
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

	public void setUidGenerator(UIDGenerator uidGenerator) {
		this.uidGenerator = uidGenerator;
	}

	public UIDGenerator getUidGenerator() {
		return uidGenerator;
	}
}
