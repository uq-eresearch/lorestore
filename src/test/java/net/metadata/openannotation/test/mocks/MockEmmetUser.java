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

import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import au.edu.diasb.emmet.model.EmmetUser;

public class MockEmmetUser implements EmmetUser {

    private static final long serialVersionUID = -8569273755731304214L;
    
    private final String name;
    private final String id;
    private final Collection<GrantedAuthority> authorities;

    public MockEmmetUser(String name, String id, Collection<GrantedAuthority> authorities) {
        this.name = name;
        this.id = id;
        this.authorities = authorities;
    }

    public String getEmail() {
        return null;
    }

    public Timestamp getCreated() {
        return null;
    }

    public Timestamp getExpiry() {
        return null;
    }

    public String getFirstName() {
        return null;
    }

    public String getIdentityUri() {
        return id;
    }

    public String getLastName() {
        return null;
    }

    public String getPrimaryUri() {
        return id;
    }

    public int getUserId() {
        return 0;
    }

    public String getUserName() {
        return name;
    }

    public boolean isActivated() {
        return true;
    }

    public boolean isLocked() {
        return false;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Timestamp getPasswordExpiry() {
        return null;
    }

}
