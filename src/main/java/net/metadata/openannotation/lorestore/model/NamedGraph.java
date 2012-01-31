package net.metadata.openannotation.lorestore.model;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;

public interface NamedGraph {
    /**
     * Get the creator property. A string of the persons name, settable by them.
     * 
     * @return the creator name or identifier or <code>null</code> if it is missing.
     * @throws LoreStoreException 
     */
    String getCreator() throws LoreStoreException;

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
    void setUser(String ownerURI) throws LoreStoreException;

	void assignURI(String newUri) throws LoreStoreException;
	
	/**
	 * Is this a private object?
	 * 
	 * A private object is only readable by the user who created it,
	 * it is hidden from both direct requests and from all searches.
	 * 
	 * @return
	 */
	public boolean isPrivate();
	
	
}
