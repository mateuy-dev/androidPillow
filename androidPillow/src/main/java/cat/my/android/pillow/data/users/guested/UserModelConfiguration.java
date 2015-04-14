package cat.my.android.pillow.data.users.guested;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.Collection;

import cat.my.android.pillow.conf.DefaultModelConfiguration;

/**
 * Created by mateuyabar on 14/04/15.
 */
public class UserModelConfiguration extends DefaultModelConfiguration<User> {
    public UserModelConfiguration(Context context, String url) {
        super(context, User.class, new TypeToken<Collection<User>>(){}, url);
        GuestedUserDataSource<User> userDataSource = new GuestedUserDataSource<User>(context, getRestMapping());
        setDataSource(userDataSource);
    }

    @Override
    public GuestedUserDataSource<User> getDataSource() {
        return (GuestedUserDataSource<User>) super.getDataSource();
    }
}
