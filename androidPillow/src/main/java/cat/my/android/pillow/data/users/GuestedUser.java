package cat.my.android.pillow.data.users;

import cat.my.android.pillow.IdentificableModel;

public interface GuestedUser extends IdentificableModel{
	public void setGuest(boolean guest);
	public boolean isGuest();

    public String getEmail();
    public void setEmail(String email);

    public String getPassword();
    public void setPassword(String password);
}
