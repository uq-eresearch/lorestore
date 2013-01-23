package net.metadata.openannotation.lorestore.security.drupal;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class DrupalAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -3969039549888595073L;

    private final Object principal;
    private final List<DrupalAttribute> attributes;

    public DrupalAuthenticationToken(Object principal, 
    		Collection<GrantedAuthority> authorities, 
    		Object details, List<DrupalAttribute> attributes) {
        super(authorities);
        this.principal = principal;
        this.attributes = attributes;
        this.setDetails(details);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    
    public List<DrupalAttribute> getAttributes() {
    	return attributes;
    }
    
    @Override
    public String toString() {
        return "DrupalAuthenticationToken{principal=" + principal
                + ", details=" + getDetails() + "}";
    }
}
