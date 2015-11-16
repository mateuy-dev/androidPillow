/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.users.guested;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;

public class User implements IdentificableModel, IGuestedUser {
	String id;
	String email;
	String password;
	boolean guest;
    String authToken;

	
	public User() {
	}
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		this.guest = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void setGuest(boolean guest) {
		this.guest = guest;
		if(guest){
			//we create a fake password so server does not complain
			password = "FAKE_PASSWORD";
		}
	}
	
	@Override
	public boolean isGuest() {
		return guest;
	}
	
	@Override
	public String toString() {
		return email;
	}

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
