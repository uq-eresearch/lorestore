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
package net.metadata.openannotation.test.mocks;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import au.edu.diasb.chico.mvc.BaseAuthenticationContext;

public class MockAuthenticationContext extends BaseAuthenticationContext {

    private String name;
    private String[] authorities;
    private String id;

    public MockAuthenticationContext(String name, String id, String[] authorities) {
        this.name = name;
        this.id = id;
        this.authorities = authorities;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        return new MockEmmetAuthentication(name, id, authorities);
    }

}
