//package cat.my.lib.restvolley;
//
//import android.content.Context;
//import cat.my.lib.restvolley.RestVolleyDataSource;
//import cat.my.lib.restvolley.RestVolleyDataSource.ISessionData;
//import cat.my.lib.restvolley.pathbuilders.IPathBuilder;
//
//import com.google.gson.Gson;
//
//
//public class RestVolleyManager {
//	private static RestVolleyManager manager = new RestVolleyManager();
//	public static RestVolleyManager getManager() {
//		return manager;
//	}
//	
//	RestVolleyDataSource restVolley;
//	public void init(Context context, IPathBuilder pathBuilder, ISessionData sessionData){
//		restVolley = new RestVolleyDataSource(context, pathBuilder, sessionData);
//	}
//	public void init(Context context, IPathBuilder pathBuilder){
//		restVolley = new RestVolleyDataSource(context, pathBuilder);
//	}
//	
//	public static RestVolleyDataSource getRestVolley() {
//		return getManager().restVolley;
//	}
//}

//	private void setRestVolley(RestVolley restVolley) {
//		this.restVolley = restVolley;
//	}

