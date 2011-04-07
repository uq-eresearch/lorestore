package net.metadata.auselit.test.mocks;

import net.metadata.auselit.lorestore.access.OREIdentityProvider;

public class MockOREIdentityProvider implements OREIdentityProvider {

	public String obtainUserURI() {
		return "http://example.com/fakeIdentityURI";
	}

}
