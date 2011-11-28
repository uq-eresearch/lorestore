package net.metadata.auselit.test.mocks;

import net.metadata.auselit.lorestore.access.LoreStoreIdentityProvider;

public class MockOREIdentityProvider implements LoreStoreIdentityProvider {

	private String userURI = "http://example.com/fakeIdentityURI";
	
	public String obtainUserURI() {
		return userURI;
	}
	
	public void setUserURI(String userURI) {
		this.userURI = userURI;
	}

}
