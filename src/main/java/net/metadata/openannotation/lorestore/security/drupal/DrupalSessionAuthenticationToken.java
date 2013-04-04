package net.metadata.openannotation.lorestore.security.drupal;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class DrupalSessionAuthenticationToken extends
		AbstractAuthenticationToken {
    //~ Instance fields ================================================================================================

	private static final long serialVersionUID = 1L;
	private final Object principal;
    private final int keyHash;

    //~ Constructors ===================================================================================================

    /**
     * Constructor.
     *
     * @param key to identify if this object made by an authorised client
     * @param principal the principal (typically a <code>UserDetails</code>)
     * @param authorities the authorities granted to the principal
     *
     * @throws IllegalArgumentException if a <code>null</code> was passed
     */
    public DrupalSessionAuthenticationToken(String key, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        if ((key == null) || ("".equals(key)) || (principal == null) || "".equals(principal)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }

        this.keyHash = key.hashCode();
        this.principal = principal;
        setAuthenticated(true);
    }

    //~ Methods ========================================================================================================

    /**
     * Always returns an empty <code>String</code>
     *
     * @return an empty String
     */
    public Object getCredentials() {
        return "";
    }

    public int getKeyHash() {
        return this.keyHash;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (obj instanceof DrupalSessionAuthenticationToken) {
        	DrupalSessionAuthenticationToken test = (DrupalSessionAuthenticationToken) obj;

            if (this.getKeyHash() != test.getKeyHash()) {
                return false;
            }

            return true;
        }

        return false;
    }

}
