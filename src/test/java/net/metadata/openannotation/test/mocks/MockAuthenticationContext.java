package net.metadata.openannotation.test.mocks;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import au.edu.diasb.chico.mvc.BaseAuthenticationContext;

public class MockAuthenticationContext extends BaseAuthenticationContext {

    private String name;
    private String[] authorities;
    private String id;

    public MockAuthenticationContext(String name, String id, String[] authorities) {
        this.name = name;
        this.id = id;
        this.authorities = authorities;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        return new MockEmmetAuthentication(name, id, authorities);
    }

}
