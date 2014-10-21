package cat.my.android.restvolley.users;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.rest.ISessionController;
import cat.my.android.restvolley.rest.RailsRestMapping;
import cat.my.android.restvolley.rest.RestDataSource;
import cat.my.android.restvolley.sync.DummyListeners;
import cat.my.android.restvolley.sync.DummyListeners.ProxyListener;
import cat.my.android.restvolley.sync.SynchManager;

import com.android.volley.Request.Method;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;

public abstract class AbstractUserController<T extends IdentificableModel> {
	SharedPreferences sharedPref;
	private static final String AUTH_TOKEN = "auth_token";
	RestDataSource<T> userDataSource;
	Context context;
	
	public AbstractUserController(Context context, RailsRestMapping<T> restMapping, String preferencesFileKey){
		this.context = context;
		userDataSource = new RestDataSource<T>(restMapping, context);
		sharedPref = context.getSharedPreferences(preferencesFileKey, Context.MODE_PRIVATE);
	}
	
	/**
	 * Creates a guest user.
	 * @return
	 */
	public abstract T createGuestUser();
	
	/**
	 * Should be called just after created.
	 * It connects to the server and authenticates (if authentication stored) or creates a guest user
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void init(Listener<Void> listener, ErrorListener errorListener){
		resetInTesting();
		String token = getAuthToken();
		if(token==null){
			signUpAsGuest(listener, errorListener);
		} else {
			listener.onResponse(null);
		}
	}
	
	/**
	 * Creates a guest user on the server
	 * @param listener
	 * @param errorListener
	 */
	private void signUpAsGuest(final Listener<Void> listener, ErrorListener errorListener){
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response.getId());
				listener.onResponse(null);
			}
		};
		userDataSource.create(createGuestUser(), onCreateListener, errorListener);
	}
	
	/**
	 * Signs up to the application. It will update server guest user with some log-in data.
	 * @param user
	 * @param listener
	 * @param errorListener
	 */
	public void signUp(T user, Listener<T> listener, ErrorListener errorListener){
		user.setId(getAuthToken());
		userDataSource.update(user, listener, errorListener);
	}
	
	/**
	 * Signs in to the application. It will try to sign-in to the server, and update the current user accordingly.
	 * Data stored (if any) will be reloaded.
	 * 
	 * @param user
	 * @param listener
	 * @param errorListener
	 */
	public void signIn(T user, final Listener<T> listener, ErrorListener errorListener){
		Listener<T> onSignInListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response.getId());
				reloadData(new ProxyListener<T, Void>(listener, response));
				listener.onResponse(response);
			}
		};
		userDataSource.executeCollectionOperation(user, Method.POST, "sign_in", null, onSignInListener, errorListener);
	}
	
	/**
	 * Called after a user has signed in to one accout.
	 * Deletes all the user realated data and donwload the new user one. 
	 */
	public void reloadData(Listener<Void> listener){
		RestVolley.getInstance().getSynchManager().reloadData(listener);
	}
	
	
	public Context getContext() {
		return context;
	}
	
	private void resetInTesting() {
		int version = getResetInTestingVersion();
		int current = sharedPref.getInt("dummy_version", -1);
		if(current<version){
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.remove(AUTH_TOKEN);
			editor.putInt("dummy_version", version);
			editor.commit();
		}
		
	}
	
	public int getResetInTestingVersion() {
		return 0;
	}

	/**
	 * Stores the given auth_token in the shared preferences
	 * @param id
	 */
	private void storeAuthToken(String id) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(AUTH_TOKEN, id);
		editor.commit();
	}
	
	/**
	 * @return stored auth_token in the shared preferences
	 */
	public String getAuthToken(){
		return sharedPref.getString(AUTH_TOKEN, null);
	}
	
	public ISessionController getSessionController(){
		return new UserSessionController();
	}
	
	/**
	 * Implementation of IAuthenticationData using the controller
	 */
	private class UserSessionController implements ISessionController{
		@Override
		public Map<String, Object> getSession() {
			Map<String, Object> result = new HashMap<String, Object>();
			String authToken = getAuthToken();
			if(authToken!=null){
				result.put(AUTH_TOKEN, authToken);
			}
			return result;
		}

		@Override
		public void init(Listener<Void> listener, ErrorListener errorListener) {
			AbstractUserController.this.init(listener, errorListener);
		}
		
	}
}
