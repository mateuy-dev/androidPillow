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

import com.android.volley.Request.Method;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.core.PillowResultProxyType;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.singleinstance.ISynchLocalSingleInstanceDataSource;
import com.mateuyabar.android.pillow.data.sync.singleinstance.SynchSingleInstanceDataSource;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.util.HashMap;
import java.util.Map;

/**
 * DataSource for a User that enables guest users. #GuestedUserDataSource.getAuthentication returns the authenticationController
 * The authentication works the following:
 *
 * 1. If nothing done it will try to create a guest user (signUpAsGuest).
 * 2. If it registers, the guest account will be 'upgraded' to registered signUp
 * 3. If signed in, the guest user will be discarted
 *
 * @param <T>
 */
public class GuestedUserDataSource<T extends IGuestedUser> extends SynchSingleInstanceDataSource<T> implements IAuthenticationController{
	private static final String AUTH_TOKEN_SESSION_PARAM = "auth_token";

	Context context;

	public GuestedUserDataSource(Class<T> modelClass, Context context, ISynchLocalSingleInstanceDataSource<T> dbSource, IRestMapping<T>restMapping) {
		super(modelClass, dbSource, restMapping, context);

		this.context = context;
		//userDataSource = new RestDataSource<T>(restMapping, context);
	}

	/**
	 * Creates a guest user.
	 * @return
	 */
	private T createGuestUser(){
		T user;
		try {
			user = getModelClass().newInstance();
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		user.setGuest(true);
		return user;
	}

	/**
	 * Should be called just after created.
	 * It connects to the server and authenticates (if authentication stored) or creates a guest user
	 */
	public IPillowResult<Void> init(){
//		resetInTesting();
		String token = getAuthToken();
		if(token==null){
			return new PillowResultProxyType<Void, T>(null, signUpAsGuestNow());
		} else {
			return PillowResult.newVoidResult();
		}
	}

	/**
	 * Creates a guest user on the server
	 * @return
	 */
	private IPillowResult<T> signUpAsGuest(){
		return create(createGuestUser());
	}

	private IPillowResult<T> signUpAsGuestNow(){
		return createNow(createGuestUser());
	}

	/**
	 * Signs up to the application. (updates user and password for previous guest user)
	 * @param user
	 * @return
	 */
	public IPillowResult<T> signUp(final T user){
		final PillowResultListener<T> result = new PillowResultListener<T>();

		final Listener<T> onSignUpListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				cache(response);
				result.setResult(response);
			}
		};

		Listener<Void> onInitListener = new Listener<Void>() {
			@Override
			public void onResponse(Void response) {
				user.setAuthToken(getAuthToken());
				getRestDataSource().executeCollectionOperation(user, Method.POST, "sign_up", null).addListeners(onSignUpListener, result);
			}
		};

		//we need to check that a guest user has been already created
		init().addListeners(onInitListener, result);

		return result;
	}

	/**
	 * Signs in to the application. It will try to sign-in to the server, and update the current user accordingly.
	 * Data stored (if any) will be reloaded.
	 *
	 * @param user
	 * @return
	 */
	public PillowResultListener<Void> signIn(T user){
		final PillowResultListener<Void> result = new PillowResultListener<Void>();

		Listener<T> onSignInListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				cache(response);
				reloadData().addListeners(result, result);
			}
		};
		getRestDataSource().executeCollectionOperation(user, Method.POST, "sign_in", null).addListeners(onSignInListener, result);
		return result;
	}

	public PillowResult<Void> signOut() {
		final PillowResultListener<Void> result = new PillowResultListener<Void>();
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				reloadData().addListeners(result, result);
			}
		};
		signUpAsGuest().addListeners(onCreateListener, result);
		return result;
	}

	/**
	 * Called after a user has signed in to one accout.
	 * Deletes all the user realated data and donwload the new user one.
	 * @return
	 */
	protected IPillowResult<Void> reloadData(){
		return Pillow.getInstance().getSynchManager().reloadData();
	}


	public Context getContext() {
		return context;
	}


	/**
	 * @return stored auth_token in the shared preferences
	 */
	public String getAuthToken(){
		T user = getLoggedUser();
		if(user==null)
			return null;
		else
			return getLoggedUser().getAuthToken();
	}

	public T getLoggedUser(){
		try {
			return get().get();
		} catch (PillowError pillowError) {
			throw new BreakFastException();
		}
	}

	@Override
	public IPillowResult<AuthenticationData> getAuthentication() {
		final PillowResultListener<AuthenticationData> result = new PillowResultListener<AuthenticationData>();

		Listener<Void> onInitListener = new Listener<Void>() {
			@Override
			public void onResponse(Void response) {
				Map<String, Object> session = new HashMap<String, Object>();
				String authToken = getAuthToken();
				if(authToken!=null){
					session.put(AUTH_TOKEN_SESSION_PARAM, authToken);
				}
				result.setResult(new AuthenticationData(session));
			}
		};

		init().addListeners(onInitListener, result);

		return result;
	}

	@Override
	public IPillowResult<Void> resetAuthentication() {
		return remove();
	}

}
