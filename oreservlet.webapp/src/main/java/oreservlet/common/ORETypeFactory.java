package oreservlet.common;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oreservlet.exceptions.ORETypeException;
import oreservlet.model.CompoundObject;
import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.annotation.danno.model.RDFParserException;
import au.edu.diasb.annotation.danno.model.RDFStatementFilter;

public interface ORETypeFactory {

	CompoundObject createCompoundObject(InputStream inputStream);

	/**
	 * Create a new (empty) container.
	 */
	RDFContainer createContainer();
	
    /**
     * Create a new container and load it from the supplied InputStream.
     * 
     * @param in the input stream to be loaded
     * @return the new container.
     * @throws IOException 
     * @throws RDFParserException
     */
    RDFContainer createContainer(InputStream in) 
    throws RDFParserException, IOException;
    
    /**
     * Apply a filter to the statements in a container to produce a
     * new container.
     *
     * @param container the container to be filtered.
     * @param filter if not {@code null} apply this filter to the RDF statements
     *     that will comprise the object.
     * @return the new container (or the original if the filter is {@code null}).
     * @throws ORETypeException 
     */
    RDFContainer filterContainer(RDFContainer container, RDFStatementFilter filter) 
    throws  ORETypeException;
    
    /**
     * Get this type factory's XML DatatypeFactory.
     * @return the datatype factory.
     */
    DatatypeFactory getXMLDatatypeFactory();
    
    /**
     * Get the XML timestamp for 'now'.
     * @return the timestamp;
     */
    XMLGregorianCalendar getDateTimeNow();
}
