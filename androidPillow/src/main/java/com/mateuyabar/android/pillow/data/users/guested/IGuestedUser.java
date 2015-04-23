package com.mateuyabar.android.pillow.data.users.guested;

import com.mateuyabar.android.pillow.IdentificableModel;

public interface IGuestedUser extends IdentificableModel{
	public void setGuest(boolean guest);
	public boolean isGuest();

    public String getEmail();
    public void setEmail(String email);

    public String getPassword();
    public void setPassword(String password);

    public String getAuthToken();
    public void setAuthToken(String authToken);
}
