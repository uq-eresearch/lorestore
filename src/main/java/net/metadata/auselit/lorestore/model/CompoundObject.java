package net.metadata.auselit.lorestore.model;

import javax.xml.datatype.XMLGregorianCalendar;

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
     * Set the resource's creator property to the supplied name.
     * 
     * @param userName
     */
    void setCreator(String userName);
    
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
    void setOwnerId(String ownerURI);

    /**
     * The resource's 'a:created' property; i.e. the notional creation date/time.
     * 
     * @return the creation date or <code>null</code> if it is missing or not a valid date/time.
     */
    XMLGregorianCalendar getCreated();

    /**
     * Set one of the resource's timestamp properties to the supplied timestamp.
     * @param propName the property name
     * @param timestamp the timestamp
     */
    void setDatestamp(String propName, XMLGregorianCalendar timestamp);

    /**
     * The resource's 'a:modified' property; i.e. the notional modification date/time.
     * 
     * @return the modified date or <code>null</code> if it is missing or not a valid date/time.
     */
    XMLGregorianCalendar getModified();

    /**
     * The resource's 'd:date' property.  Note that this is typically updated in step with the
     * 'a:created' and 'a:modified' properties.
     * 
     * @return the date or <code>null</code> if it is missing or not a valid date/time.
     */
    XMLGregorianCalendar getDate();

	void assignURI(String newUri) throws OREException;

}
