package net.metadata.auselit.lorestore.model;

import net.metadata.auselit.lorestore.exceptions.OREException;

public interface CompoundObject {
    /**
     * Get the annotation / reply's creator property.
     * 
     * @return the creator name or identifier or <code>null</code> if it is missing.
     * @throws OREException 
     */
    String getCreator() throws OREException;

    /**
     * Get the resource's owner property.
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

}
