package net.metadata.openannotation.test.mocks;

import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import au.edu.diasb.emmet.model.EmmetUser;

public class MockEmmetUser implements EmmetUser {

    private static final long serialVersionUID = -8569273755731304214L;
    
    private final String name;
    private final String id;
    private final Collection<GrantedAuthority> authorities;

    public MockEmmetUser(String name, String id, Collection<GrantedAuthority> authorities) {
        this.name = name;
        this.id = id;
        this.authorities = authorities;
    }

    public String getEmail() {
        return null;
    }

    public Timestamp getCreated() {
        return null;
    }

    public Timestamp getExpiry() {
        return null;
    }

    public String getFirstName() {
        return null;
    }

    public String getIdentityUri() {
        return id;
    }

    public String getLastName() {
        return null;
    }

    public String getPrimaryUri() {
        return id;
    }

    public int getUserId() {
        return 0;
    }

    public String getUserName() {
        return name;
    }

    public boolean isActivated() {
        return true;
    }

    public boolean isLocked() {
        return false;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Timestamp getPasswordExpiry() {
        return null;
    }

}
