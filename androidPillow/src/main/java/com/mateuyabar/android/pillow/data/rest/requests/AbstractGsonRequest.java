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

package com.mateuyabar.android.pillow.data.rest.requests;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Listeners.VolleyErrorListener;
import com.mateuyabar.android.pillow.data.rest.Route;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
