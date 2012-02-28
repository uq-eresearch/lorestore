package net.metadata.openannotation.test.mocks;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class MockEmmetAuthentication implements Authentication {

    private static final long serialVersionUID = -1074412610118482222L;
    private final Collection<GrantedAuthority> authorities;
    private final String id;
    private final String name;

    public MockEmmetAuthentication(String name, String id, String[] authorities) {
        this.name = name;
        this.id = id;
        this.authorities = new ArrayList<GrantedAuthority>(authorities.length);
        for (String authority : authorities) {
            this.authorities.add(new GrantedAuthorityImpl(authority));
        }
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Object getCredentials() {
        return "N/A";
    }

    public Object getDetails() {
        return getPrincipal();
    }

    public Object getPrincipal() {
        return new MockEmmetUser(name, id, authorities);
    }

    public boolean isAuthenticated() {
        return true;
    }

    public void setAuthenticated(boolean isAuthenticated)
            throws IllegalArgumentException {
        // ignore
    }

    public String getName() {
        return name;
    }

}
