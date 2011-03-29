/*
 * Copyright (c) 2008-2010 School of Information Technology and Electrical Engineering, 
 * The University of Queensland.  This software is being developed for the "Data 
 * Integration and Annotation Services in Biodiversity" (DIAS-B) project.  DIAS-B
 * is a NeAT project, funded jointly by ARCS and ANDS, and managed by the Atlas 
 * of Living Australia (ALA).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package oreservlet.model;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oreservlet.common.OREConfigurationException;
import oreservlet.common.ORETypeFactory;

import org.apache.log4j.Logger;

public abstract class TypeFactoryBase implements ORETypeFactory {

    protected final Logger logger;
    private final DatatypeFactory xmlDatatypeFactory;

    public TypeFactoryBase(Logger logger) {
        this.logger = logger;
        try {
            xmlDatatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new OREConfigurationException(ex);
        }
    }


    public final XMLGregorianCalendar parseDateTime(String dateTime) {
        return xmlDatatypeFactory.newXMLGregorianCalendar(dateTime);
    }

    public final XMLGregorianCalendar getDateTimeNow() {
        return xmlDatatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
    }
    
    public final DatatypeFactory getXMLDatatypeFactory() {
        return xmlDatatypeFactory;
    }
    
    protected final String describeClass(Class<?> clazz) {
        if (CompoundObject.class.isAssignableFrom(clazz)) {
            return "compoundObject";
        } else {
            return "objects";
        }
    }

}
