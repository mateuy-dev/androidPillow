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

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mateuyabar.android.pillow.conf.DefaultModelConfiguration;
import com.mateuyabar.android.pillow.data.singleinstance.ISynchLocalSingleInstanceDataSource;
import com.mateuyabar.android.pillow.data.singleinstance.SingleInstanceKeyValueDataSource;

import java.util.Collection;

/**
 * Created by mateuyabar on 14/04/15.
 */
public class UserModelConfiguration extends DefaultModelConfiguration<User> {
    public UserModelConfiguration(Context context, String url) {
        super(context, User.class, new TypeToken<Collection<User>>(){}, url);
        setLocalDataSource(new SingleInstanceKeyValueDataSource(User.class, getPreferences()));
        GuestedUserDataSource<User> userDataSource = new GuestedUserDataSource<User>(User.class, context, getLocalDataSource(), getRestMapping());
        setDataSource(userDataSource);
    }

    @Override
    public GuestedUserDataSource<User> getDataSource() {
        return (GuestedUserDataSource<User>) super.getDataSource();
    }

    @Override
    public ISynchLocalSingleInstanceDataSource<User> getLocalDataSource() {
        return (ISynchLocalSingleInstanceDataSource) super.getLocalDataSource();
    }
}
