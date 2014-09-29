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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import cat.my.lib.mydata.IDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.pathbuilders.IPathBuilder;
import cat.my.lib.restvolley.pathbuilders.Route;
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

public class RestVolleyDataSource implements IDataSource{
	IPathBuilder pathBuilder;
	RequestQueue volleyQueue;
	ISessionData sessionData = new NoneSessionData();

	public RestVolleyDataSource(Context context, IPathBuilder pathBuilder) {
		this(context, pathBuilder, new NoneSessionData());
	}
	
	public RestVolleyDataSource(Context context, IPathBuilder pathBuilder, ISessionData sessionData) {
		this.volleyQueue = Volley.newRequestQueue(context);
		this.pathBuilder = pathBuilder;
		this.sessionData = sessionData;
	}
	
	public <T extends IdentificableModel> void executeListOperation(Class<T> clazz, Type collectionType, Route route, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener) {
		String requestBody=null;
		Map<String, Object> map=new HashMap<String, Object>(sessionData.getData());
		if(params!=null){
			map.putAll(params);
		}
		if(map.size()>0){
			requestBody = pathBuilder.getSerializer().toJson(map);
		}
		
		GsonCollectionRequest<T> gsonRequest = new GsonCollectionRequest<T>(pathBuilder.getSerializer(), route, collectionType, requestBody, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}
	
	public <T extends IdentificableModel> void executeOperation(Class<T> clazz, T model, Route route, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		String requestBody=null;
		Map<String, Object> map=new HashMap<String, Object>(sessionData.getData());
		if(params!=null){
			map.putAll(params);
		}
		if(model!=null){
			map.put(pathBuilder.getModelName(clazz), model);
		}
		if(map.size()>0){
			requestBody = pathBuilder.getSerializer().toJson(map);
		}
		GsonRequest<T> gsonRequest = new GsonRequest<T>(pathBuilder.getSerializer(), route, clazz, requestBody, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}
	
	@Override
	public <T extends IdentificableModel> void index(Class<T> clazz, Type collectionType, Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getIndexPath(clazz);
		executeListOperation(clazz, collectionType, route, null, listener, errorListener);
	}

	@Override
	public <T extends IdentificableModel> void show(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getShowPath(model);
		executeOperation(clazz, model, route, null, listener, errorListener);
	}
	
	@Override
	public <T extends IdentificableModel> void create(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getCreatePath(model);
		executeOperation(clazz, model, route, null, listener, errorListener);
	}
	
	@Override
	public <T extends IdentificableModel> void update(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		//@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
		Route route = pathBuilder.getUpdatePath(model);
		executeOperation(clazz, model, route, null, listener, errorListener);
		
	}
	
	@Override
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model, Listener<Void> listener, ErrorListener errorListener) {
		Route route = pathBuilder.getDestroyPath(model);
		Listener<T> proxyListener = new VoidListenerProxy<T>(listener);
		executeOperation(clazz, model, route, null, proxyListener, errorListener);
	}
	
	public class VoidListenerProxy<T> implements Listener<T>{
		Listener<Void> listener;
		public VoidListenerProxy(Listener<Void> listener){
			this.listener = listener;
		}
		@Override
		public void onResponse(T response) {
			listener.onResponse(null);
		}
	}
	
	public static interface ISessionData{
		public Map<String, Object> getData();
	}
	
	private static class NoneSessionData implements ISessionData{
		@Override
		public Map<String, Object> getData() {
			return new HashMap<String, Object>();
		}
	}
}