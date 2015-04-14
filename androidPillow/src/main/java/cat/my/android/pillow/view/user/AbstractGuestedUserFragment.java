package cat.my.android.pillow.view.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.R;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.data.sync.CommonListeners.DummyToastListener;
import cat.my.android.pillow.data.users.guested.IGuestedUser;
import cat.my.android.pillow.data.users.guested.GuestedUserDataSource;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.android.pillow.view.forms.TFormView;
import cat.my.android.pillow.view.reflection.ViewConfig;
import cat.my.util.exceptions.BreakFastException;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.user_fragment, container, false);

        formView = new TFormView<>(getActivity(), true);
        formView.setModel(new LoginData());
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        formView.setLayoutParams(params);
        rootView.addView(formView, 1);


		userTextView = (TextView) rootView.findViewById(R.id.userTextView);
		
		final Context context = getActivity();
		signInButton = (Button)rootView.findViewById(R.id.signin_button);
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                T user = getUser();
                if(user!=null)
				    userController.signIn(user).setListeners(new LogListener(context, "signed in"), CommonListeners.defaultErrorListener);
			}
		});
		
		signUpButton = (Button)rootView.findViewById(R.id.signup_button);
		signUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                T user = getUser();
                if(user!=null)
				    userController.signUp(user).setListeners(new LogListener(context, "signed up"), CommonListeners.defaultErrorListener);
			}
		});

        signOutButton = (Button) rootView.findViewById(R.id.signout_button);
		signOutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userController.signOut().setListeners(new LogListener(context, "signed out"), CommonListeners.defaultErrorListener);
			}
		});

        userController.init().setListeners(new Listeners.ViewListener<Void>() {
            @Override
            public void onResponse(Void response) {
                refreshView();
            }
        }, CommonListeners.defaultErrorListener);
		
		return rootView;
	}
	
	public void refreshView(){
		T user = userController.getLoggedUser();


		if(user.isGuest()){
            String guest = getString(R.string.guest);
            String text = getString(R.string.logged_as, guest);
			userTextView.setText(text);
			signInButton.setVisibility(View.VISIBLE);
			signUpButton.setVisibility(View.VISIBLE);
            formView.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
		}
		else{
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
