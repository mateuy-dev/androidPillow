package cat.my.android.pillow.data.users;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.PillowResult;
import cat.my.android.pillow.data.core.PillowResultListener;
import cat.my.android.pillow.data.core.PillowResultProxyType;
import cat.my.android.pillow.data.rest.ISessionController;
import cat.my.android.pillow.data.rest.RailsRestMapping;
import cat.my.android.pillow.data.rest.RestDataSource;

import com.android.volley.Request.Method;

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
	public IPillowResult<Void> init(){
		resetInTesting();
		String token = getAuthToken();
		if(token==null){
			return new PillowResultProxyType<Void, T>(context, null, signUpAsGuest());
		} else {
			return PillowResult.newVoidResult(context);
		}
	}
	
	/**
	 * Creates a guest user on the server
	 * @return 
	 */
	private IPillowResult<T> signUpAsGuest(){
		final PillowResult<T> result = new PillowResult<T>(context);
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response.getId());
				result.setResult(response);
			}
		};
		userDataSource.create(createGuestUser()).addListener(onCreateListener);
		return result;
	}
	
	/**
	 * Signs up to the application. It will update server guest user with some log-in data.
	 * @param user
	 * @return 
	 */
	public IPillowResult<T> signUp(T user){
		user.setId(getAuthToken());
		return userDataSource.update(user);
	}
	
	/**
	 * Signs in to the application. It will try to sign-in to the server, and update the current user accordingly.
	 * Data stored (if any) will be reloaded.
	 * 
	 * @param user
	 * @return 
	 */
	public PillowResultListener<Void> signIn(T user){
		final PillowResultListener<Void> result = new PillowResultListener<Void>(context);
		
		Listener<T> onSignInListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response.getId());
				reloadData().setListeners(result,result);
			}
		};
		userDataSource.executeCollectionOperation(user, Method.POST, "sign_in", null).setListeners(onSignInListener, result);
		return result;
	}
	
	public PillowResult<Void> signOut() {
		final PillowResultListener<Void> result = new PillowResultListener<Void>(context);
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response.getId());
				reloadData().setListeners(result,result);
			}
		};
		userDataSource.create(createGuestUser()).addListener(onCreateListener);
		return result;
	}
	
	/**
	 * Called after a user has signed in to one accout.
	 * Deletes all the user realated data and donwload the new user one. 
	 * @return 
	 */
	public IPillowResult<Void> reloadData(){
		return Pillow.getInstance().getSynchManager().reloadData();
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
		public IPillowResult<SessionData> getSession() {
			final PillowResultListener<SessionData> result = new PillowResultListener<SessionData>(context);
			
			Listener<Void> onInitListener = new Listener<Void>() {
				@Override
				public void onResponse(Void response) {
					Map<String, Object> session = new HashMap<String, Object>();
					String authToken = getAuthToken();
					if(authToken!=null){
						session.put(AUTH_TOKEN, authToken);
					}
					result.setResult(new SessionData(session));
				}
			};
			
			
			
			init().setListeners(onInitListener, result);
			
			return result;
		}
	}
}
