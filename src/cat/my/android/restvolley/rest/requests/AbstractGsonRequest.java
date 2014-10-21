package cat.my.android.restvolley.rest.requests;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import cat.my.android.restvolley.rest.Route;

import com.android.volley.DefaultRetryPolicy;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.VolleyErrorListener;

import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

public abstract class AbstractGsonRequest <T> extends JsonRequest<T>{
	private Gson gson;
	Map<String, Object> params;
	
	private static String getResourceBody(Gson gson, Map<String, Object> map){
		String result=null;
		if(!map.isEmpty()){
			result = gson.toJson(map);
		}
		return result;
	}
	
	public AbstractGsonRequest(Gson gson, Route route, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener, int initialTimeOutMs) {
		super(route.getMethod(), route.getUrl(), getResourceBody(gson, params), listener, new VolleyErrorListener(errorListener));
		this.gson=gson;
		this.params = params;
		setRetryPolicy(new DefaultRetryPolicy(
				initialTimeOutMs, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}
	
	public Gson getGson() {
		return gson;
	}
	
	 @Override
	    public String getUrl() {
	    	if((getMethod()==Method.GET || getMethod()==Method.DELETE) && !params.isEmpty()){
	    		List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
	    		for(Entry<String, Object> param:params.entrySet()){
	    			Object value = param.getValue();
	    			if(!(value instanceof String))
	    				value= gson.toJson(value);
	    			nameValues.add(new BasicNameValuePair(param.getKey(), (String)value));
	    		}
	    		
	    		//URLEncodedUtils.format(parameters, encoding)
	    		
	    		return super.getUrl() + "?"+URLEncodedUtils.format(nameValues, "UTF-8");
	    	}
	        return super.getUrl();
	    }

}
