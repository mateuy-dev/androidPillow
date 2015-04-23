package com.mateuyabar.android.pillow.data.users.guested;

import com.mateuyabar.android.pillow.IdentificableModel;

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
