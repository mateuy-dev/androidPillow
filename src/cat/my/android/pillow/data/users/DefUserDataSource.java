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
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.ISessionController;
import cat.my.android.pillow.data.rest.RailsRestMapping;
import cat.my.android.pillow.data.rest.RestDataSource;
import cat.my.util.exceptions.BreakFastException;

import com.android.volley.Request.Method;
import com.google.gson.Gson;

public class DefUserDataSource<T extends IUser> extends RestDataSource<T>{
	SharedPreferences sharedPref;
	private static final String AUTH_TOKEN = "logged_auth_token";
	private static final String USER_DATA = "logged_user_data";
	
//	RestDataSource<T> userDataSource;
	Context context;
	
	public DefUserDataSource(Context context, IRestMapping<T> restMapping){
		super(restMapping, context);
		this.context = context;
		//userDataSource = new RestDataSource<T>(restMapping, context);
		String preferencesFileKey = Pillow.PREFERENCES_FILE_KEY;
		sharedPref = context.getSharedPreferences(preferencesFileKey, Context.MODE_PRIVATE);
		
	}
	
	/**
	 * Creates a guest user.
	 * @return
	 */
	public T createGuestUser(){
		T user;
		try {
			user = getRestMapping().getModelClass().newInstance();
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		user.setGuest(true);
		return user;
	}
	
	/**
	 * Should be called just after created.
	 * It connects to the server and authenticates (if authentication stored) or creates a guest user
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public IPillowResult<Void> init(){
//		resetInTesting();
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
		final PillowResultListener<T> result = new PillowResultListener<T>(context);
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response);
				result.setResult(response);
			}
		};
		create(createGuestUser()).setListeners(onCreateListener, result);
		return result;
	}
	
	/**
	 * Signs up to the application. (updates user and password for previous guest user )
	 * @param user
	 * @return 
	 */
	public IPillowResult<T> signUp(T user){
		final PillowResultListener<T> result = new PillowResultListener<T>(context);
		user.setId(getAuthToken());
		Listener<T> onSignUpListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response);
				result.setResult(response);
			}
		};
		update(user).setListeners(onSignUpListener, result);
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
		final PillowResultListener<Void> result = new PillowResultListener<Void>(context);
		
		Listener<T> onSignInListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response);
				reloadData().setListeners(result,result);
			}
		};
		executeCollectionOperation(user, Method.POST, "sign_in", null).setListeners(onSignInListener, result);
		return result;
	}
	
	public PillowResult<Void> signOut() {
		final PillowResultListener<Void> result = new PillowResultListener<Void>(context);
		Listener<T> onCreateListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				storeAuthToken(response);
				reloadData().setListeners(result,result);
			}
		};
		create(createGuestUser()).setListeners(onCreateListener, result);
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
	
//	private void resetInTesting() {
//		int version = getResetInTestingVersion();
//		int current = sharedPref.getInt("dummy_version", -1);
//		if(current<version){
//			SharedPreferences.Editor editor = sharedPref.edit();
//			editor.remove(AUTH_TOKEN);
//			editor.putInt("dummy_version", version);
//			editor.commit();
//		}
//		
//	}
//	
//	public int getResetInTestingVersion() {
//		return 0;
//	}

	/**
	 * Stores the given auth_token in the shared preferences
	 * @param id
	 */
	private void storeAuthToken(T user) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(AUTH_TOKEN, user.getId());
		String userJson = new Gson().toJson(user);
		editor.putString(USER_DATA, userJson);
		editor.commit();
	}
	
	/**
	 * @return stored auth_token in the shared preferences
	 */
	public String getAuthToken(){
		return sharedPref.getString(AUTH_TOKEN, null);
	}
	
	public T getLoggedUser(){
		String userJson = sharedPref.getString(USER_DATA, null);
		if(userJson==null)
			return null;
		return new Gson().fromJson(userJson, getRestMapping().getModelClass());
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
