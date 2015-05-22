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

package com.mateuyabar.android.pillow.view.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.data.sync.CommonListeners.DummyToastListener;
import com.mateuyabar.android.pillow.data.users.guested.GuestedUserDataSource;
import com.mateuyabar.android.pillow.data.users.guested.IGuestedUser;
import com.mateuyabar.android.pillow.data.validator.annotations.NotNull;
import com.mateuyabar.android.pillow.view.forms.TFormView;
import com.mateuyabar.android.pillow.view.reflection.ViewConfig;
import com.mateuyabar.util.exceptions.BreakFastException;

/**
 * Atention: This class is abstract because fragment constructors must not contain params.
 * Child implementations should set the userClass.
 *
 *
 * @param <T>
 */
public abstract class AbstractGuestedUserFragment<T extends IdentificableModel & IGuestedUser> extends Fragment {

    public AbstractGuestedUserFragment(Class<T> userClass){
        this.userClass = userClass;
    }

    Class<T> userClass;
	GuestedUserDataSource<T> userController;
	TextView userTextView;
	Button signUpButton, signInButton, signOutButton;
    TFormView<LoginData> formView;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userController = (GuestedUserDataSource<T>) Pillow.getInstance(getActivity()).getDataSource(userClass);
	}

    public int getLayoutId(){
        return R.layout.user_fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);

        FrameLayout userFormLaout = (FrameLayout) rootView.findViewById(R.id.user_form);
        formView = new TFormView<>(getActivity(), true);
        formView.setModel(new LoginData());
        userFormLaout.addView(formView);


		userTextView = (TextView) rootView.findViewById(R.id.userTextView);
		
		final Context context = getActivity();
		signInButton = (Button)rootView.findViewById(R.id.signin_button);
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                T user = getUser();
                if(user!=null)
				    userController.signIn(user).addListeners(getOnSignedInListener(), CommonListeners.defaultErrorListener);
			}
		});
		
		signUpButton = (Button)rootView.findViewById(R.id.signup_button);
		signUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                T user = getUser();
                if(user!=null)
				    userController.signUp(user).addListeners(getOnSignedUpListener(), CommonListeners.defaultErrorListener);
			}
		});

        signOutButton = (Button) rootView.findViewById(R.id.signout_button);
		signOutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userController.signOut().addListeners(new LogListener(context, "signed out"), CommonListeners.defaultErrorListener);
			}
		});

        userController.init().addListeners(new Listeners.ViewListener<Void>() {
            @Override
            public void onResponse(Void response) {
                refreshView();
            }
        }, CommonListeners.defaultErrorListener);
        refreshView(true); //User not created yet(no internet).
		return rootView;
	}

    protected Listeners.Listener<Void> getOnSignedInListener(){
        return new LogListener(getActivity(), "signed in");
    }

    protected Listeners.Listener<T> getOnSignedUpListener(){
        return new LogListener(getActivity(), "signed up");
    }

	public void refreshView(){
		T user = userController.getLoggedUser();
        refreshView(user.isGuest());
	}
    private void refreshView(boolean isGuest){
        if(isGuest){
            String guest = getString(R.string.guest);
            String text = getString(R.string.logged_as, guest);
            userTextView.setText(text);
            signInButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
            formView.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        } else {
            String text = getString(R.string.logged_as, userController.getLoggedUser().toString());
            userTextView.setText(text);
            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            formView.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        }
    }
	
	public T getUser(){
        LoginData loginData = formView.getModel(true);
        if(loginData!=null) {
            T user = null;
            try {
                user = userClass.newInstance();
            } catch (Exception e) {
                throw new BreakFastException(e);
            }
            user.setEmail(loginData.email);
            user.setPassword(loginData.password);
            return user;
        } else {
            return null;
        }
	}
	
	/*public String getEmail(){
		return emailView.getText().toString();
	}*/
	/*
	public String getPassword(){
		return passwordView.getText().toString();
	}*/
	
	private class LogListener extends DummyToastListener{

		public LogListener(Context arg0, String arg1) {
			super(arg0, arg1);
		}
		
		@Override
		public void onResponse(Object response) {
			super.onResponse(response);
			refreshView();
		}
		
	}

    public static class LoginData{
        @NotNull
        @ViewConfig.ViewType(order = 0)
        public String email;
        @NotNull
        @ViewConfig.ViewType(order = 1)
        public String password;
    }

}
