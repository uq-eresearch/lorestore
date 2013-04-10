package net.metadata.openannotation.lorestore.security.drupal;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class DrupalAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -3969039549888595073L;

    private final Object principal;

    public DrupalAuthenticationToken(Object principal, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    
    
    @Override
    public String toString() {
        return "DrupalAuthenticationToken{principal=" + principal
                + ", details=" + getDetails() + "}";
    }
}
