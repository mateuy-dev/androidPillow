package cat.my.android.pillow.data.users;

import cat.my.android.pillow.IdentificableModel;

public interface IUser extends IdentificableModel{
	public void setGuest(boolean guest);
	public boolean isGuest();
}
