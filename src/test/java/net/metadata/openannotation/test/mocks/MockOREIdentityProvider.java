package net.metadata.openannotation.test.mocks;

import net.metadata.openannotation.lorestore.access.LoreStoreIdentityProvider;

public class MockOREIdentityProvider implements LoreStoreIdentityProvider {

	private String userURI = "http://example.com/fakeIdentityURI";
	private String userName = "Test User";
	
	public String obtainUserURI() {
		return userURI;
	}
	
	public void setUserURI(String userURI) {
		this.userURI = userURI;
	}

	@Override
	public String obtainUserName() {
		return userName;
	}

}
