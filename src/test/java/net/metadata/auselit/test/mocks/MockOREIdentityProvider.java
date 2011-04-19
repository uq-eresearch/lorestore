package net.metadata.auselit.test.mocks;

import net.metadata.auselit.lorestore.access.OREIdentityProvider;

public class MockOREIdentityProvider implements OREIdentityProvider {

	private String userURI = "http://example.com/fakeIdentityURI";
	
	public String obtainUserURI() {
		return userURI;
	}
	
	public void setUserURI(String userURI) {
		this.userURI = userURI;
	}

}
