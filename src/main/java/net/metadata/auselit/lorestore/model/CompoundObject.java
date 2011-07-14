package net.metadata.auselit.lorestore.model;

import net.metadata.auselit.lorestore.exceptions.OREException;

public interface CompoundObject {
    /**
     * Get the annotation / reply's creator property. A string of the persons name, settable by them.
     * 
     * @return the creator name or identifier or <code>null</code> if it is missing.
     * @throws OREException 
     */
    String getCreator() throws OREException;

    /**
     * Get the resource's owner property. A string of their assigned user URI,
     * that is used for authentication and ownership of the object.
     * 
     * @return the owner URI or <code>null</code> if it is missing.
     */
    String getOwnerId();

    /**
     * Set the resource's owner property to the supplied URI.
     * 
     * @param ownerURI
     */
    void setUser(String ownerURI) throws OREException;

	void assignURI(String newUri) throws OREException;
	
	/**
	 * Is this a private compound object?
	 * 
	 * A private compound object is only readable by the user who created it,
	 * it is hidden from both direct requests and from all searches.
	 * 
	 * @return
	 */
	public boolean isPrivate();
	
	/**
	 * Is this compound object locked for editing.
	 * 
	 * A locked compound object can no longer be edited (including being unlocked),
	 * except by an administrator.
	 * 
	 * @return
	 */
	public boolean isLocked();
}
