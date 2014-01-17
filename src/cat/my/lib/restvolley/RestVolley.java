/**
 * Copyright Mateu Yábar (http://mateuyabar.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cat.my.lib.restvolley;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import cat.my.lib.restvolley.RailsPathBuilder.Route;
import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.requests.GsonCollectionRequest;
import cat.my.lib.restvolley.requests.GsonRequest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class RestVolley {
	RailsPathBuilder pathBuilder;
	RequestQueue volleyQueue;
	Gson gson;

	public RestVolley(Context context, RailsPathBuilder pathBuilder, Gson gson) {
		this.volleyQueue = Volley.newRequestQueue(context);
		this.pathBuilder = pathBuilder;
		this.gson = gson;
	}
	
	
	public <T extends IdentificableModel> void index(Class<T> clazz, Type collectionType, Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getIndexPath(clazz);
		GsonCollectionRequest<T> gsonRequest = new GsonCollectionRequest<T>(gson, route, collectionType, null, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}

	public <T extends IdentificableModel> void show(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getShowPath(model);
		addRoute(route, clazz, listener, errorListener);
	}
	
	public <T extends IdentificableModel> void create(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getCreatePath(model);
		addRoute(route, clazz, model, listener, errorListener);
	}
	
	public <T extends IdentificableModel> void update(Class<T> clazz, T model, Listener<Void> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getUpdatePath(model);
		Listener<T> proxyListener = new VoidListenerProxy<T>(listener);
		addRoute(route, clazz, model, proxyListener, errorListener);
		
	}
	
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model, Listener<Void> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getDestroyPath(model);
		Listener<T> proxyListener = new VoidListenerProxy<T>(listener);
		addRoute(route, clazz, proxyListener, errorListener);
	}
	
	private <T> void addRoute(Route route, Class<T> clazz, Listener<T> listener, ErrorListener errorListener){
		addRoute(route, clazz, null, listener, errorListener);
	}

	private <T> void addRoute(Route route, Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener){
		String requestBody=null;
		if(model!=null){
			requestBody = gson.toJson(model);
		}
		GsonRequest<T> gsonRequest = new GsonRequest<T>(gson, route, clazz, requestBody, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}

	public interface OnUpdateListener<T> {
	}

	public interface OnGetListener<T> {
	}
	
	private class VoidListenerProxy<T> implements Listener<T>{
		Listener<Void> listener;
		public VoidListenerProxy(Listener<Void> listener){
			this.listener = listener;
		}
		@Override
		public void onResponse(T response) {
			listener.onResponse(null);
		}
	}
}
