package net.metadata.auselit.lorestore.access;
import net.metadata.auselit.lorestore.model.CompoundObject;

import org.ontoware.rdf2go.model.Model;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import au.edu.diasb.chico.mvc.AuthenticationContext;
import au.edu.diasb.chico.mvc.DefaultAuthenticationContext;

public class DefaultOREAccessPolicy implements OREAccessPolicy, InitializingBean {
	
	private AuthenticationContext ac;
	private OREIdentityProvider ip;
	private String[] adminAuthorities;
    private String[] readAuthorities;
    private String[] writeAuthorities;


	public void afterPropertiesSet() throws Exception {
        if (ac == null) {
            ac = new DefaultAuthenticationContext();
        }
        if (ip == null) {
        	ip = new DefaultOREIdentityProvider(ac);
        }
	}
	
	public void checkRead(CompoundObject obj) {
		ac.checkAuthority(null, readAuthorities);

		if (obj.isPrivate()) {
			Authentication auth = ac.checkAuthority(null, readAuthorities);
			checkObjectOwner(obj, auth);
		}
		
	}

	public void checkCreate( Model res) {
		ac.checkAuthority(null, writeAuthorities);
	}

	/**
	 * Allowed if user is an admin or (has write authority, owns the object, and the object isn't locked)
	 */
	public void checkUpdate( CompoundObject obj) {
		if (ac.hasAuthority(null, adminAuthorities)) {
			return;
		}
		Authentication auth = ac.checkAuthority(null, writeAuthorities);
		checkObjectOwner(obj, auth);
		checkNotLocked(obj);

	}
	/**
	 * Allowed if user is an admin or has write authority and owns the object
	 */
	public void checkDelete( CompoundObject obj) {
		if (ac.hasAuthority(null, adminAuthorities)) {
			return;
		}
		Authentication auth = ac.checkAuthority(null, writeAuthorities);
		checkObjectOwner(obj, auth);
	}

	private void checkObjectOwner(CompoundObject obj, Authentication auth) {
		String ownerId = obj.getOwnerId();
		if (ownerId == null) {
			return;
		}
		String userUri = ip.obtainUserURI();
		if (userUri == null) {
			throw new AccessDeniedException("Ooops ... cannot establish your identity");
		} else if (!ownerId.equals(userUri)) {
			throw new AccessDeniedException("You do not own this object");
		}
		
	}

	private void checkNotLocked(CompoundObject obj) {
		if (obj.isLocked()) {
			throw new AccessDeniedException("Object is locked, must be administrator to modify");
		}
	}
	
	public void checkAdmin() {
		ac.checkAuthority(null, adminAuthorities);

	}

	public void setAuthenticationContext(AuthenticationContext ac) {
		this.ac = ac;
	}

	public AuthenticationContext getAuthenticationContext() {
		return ac;
	}
    public String[] getAdminAuthorityList() {
        return adminAuthorities;
    }

    /**
     * Set the name of the admin authority; e.g. "ROLE_ADMIN".
     * @param adminAuthority
     */
    public void setAdminAuthorities(String adminAuthorities) {
        this.adminAuthorities = 
            StringUtils.commaDelimitedListToStringArray(adminAuthorities);
    }

    public String[] getReadAuthorityList() {
        return readAuthorities;
    }

    /**
     * Set the names of the read authorities; e.g. "ROLE_USER".
     * @param readAuthorities
     */
    public void setReadAuthorities(String readAuthorities) {
        this.readAuthorities = 
            StringUtils.commaDelimitedListToStringArray(readAuthorities);
    }

    public String[] getWriteAuthorityList() {
        return writeAuthorities;
    }

    /**
     * Set the names of the write authorities; e.g. "ROLE_ORE".
     * @param writeAuthorities
     */
    public void setWriteAuthorities(String writeAuthorities) {
        this.writeAuthorities = 
            StringUtils.commaDelimitedListToStringArray(writeAuthorities);
    }

	public void setIdentityProvider(OREIdentityProvider ip) {
		this.ip = ip;
	}

	public OREIdentityProvider getIdentityProvider() {
		return ip;
	}
}
