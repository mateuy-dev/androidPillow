package cat.my.lib.restvolley;

import android.content.Context;
import cat.my.lib.restvolley.RestVolley;
import cat.my.lib.restvolley.RestVolley.ISessionData;
import cat.my.lib.restvolley.pathbuilders.IPathBuilder;

import com.google.gson.Gson;


public class RestVolleyManager {
	private static RestVolleyManager manager = new RestVolleyManager();
	public static RestVolleyManager getManager() {
		return manager;
	}
	
	RestVolley restVolley;
	public void init(Context context, IPathBuilder pathBuilder, ISessionData sessionData){
		restVolley = new RestVolley(context, pathBuilder, sessionData);
	}
	public void init(Context context, IPathBuilder pathBuilder){
		restVolley = new RestVolley(context, pathBuilder);
	}
	
	public static RestVolley getRestVolley() {
		return getManager().restVolley;
	}


//	private void setRestVolley(RestVolley restVolley) {
//		this.restVolley = restVolley;
//	}
}
