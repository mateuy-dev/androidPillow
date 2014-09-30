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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import cat.my.lib.mydata.IDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.pathbuilders.IRestMap;
import cat.my.lib.restvolley.pathbuilders.Route;
import cat.my.lib.restvolley.requests.GsonCollectionRequest;
import cat.my.lib.restvolley.requests.GsonRequest;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

public class RestVolleyDataSource<T extends IdentificableModel> implements IDataSource<T> {
	
	RequestQueue volleyQueue;
	ISessionData sessionData = new NoneSessionData();
	
	IRestMap<T> restMap;
	
	public RestVolleyDataSource(IRestMap<T> restMap, Context context) {
		this(restMap, context, new NoneSessionData());
	}
	
	public RestVolleyDataSource(IRestMap<T> restMap, Context context, ISessionData sessionData) {
		this.restMap=restMap;
		this.volleyQueue = Volley.newRequestQueue(context);
		
		this.sessionData = sessionData;
	}
	
	
	
	public void executeListOperation(int method, String operation, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = restMap.getCollectionRoute(method, operation);
		executeListOperation(route, params, listener, errorListener);
	}
	
	private void executeListOperation(Route route, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener) {
		String requestBody=null;
		Map<String, Object> map=new HashMap<String, Object>(sessionData.getData());
		if(params!=null){
			map.putAll(params);
		}
		if(map.size()>0){
			requestBody = restMap.getSerializer().toJson(map);
		}
		
		GsonCollectionRequest<T> gsonRequest = new GsonCollectionRequest<T>(restMap.getSerializer(), route, restMap.getCollectionType(), requestBody, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}
	
	public void executeOperation(T model, int method, String operation, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMap.getMemberRoute(model, method, operation);
		executeOperation(model, route, params, listener, errorListener);
	}
	
	private void executeOperation(T model, Route route, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		String requestBody=null;
		Map<String, Object> map=new HashMap<String, Object>(sessionData.getData());
		if(params!=null){
			map.putAll(params);
		}
		if(model!=null){
			map.put(restMap.getModelName(), model);
		}
		if(map.size()>0){
			requestBody = restMap.getSerializer().toJson(map);
		}
		GsonRequest<T> gsonRequest = new GsonRequest<T>(restMap.getSerializer(), route, restMap.getModelClass(), requestBody, listener, errorListener);
		gsonRequest.setShouldCache(false);
		volleyQueue.add(gsonRequest);
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = restMap.getIndexPath();
		executeListOperation(route, null, listener, errorListener);
	}

	@Override
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMap.getShowPath(model);
		executeOperation(model, route, null, listener, errorListener);
	}
	
	@Override
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMap.getCreatePath(model);
		executeOperation(model, route, null, listener, errorListener);
	}
	
	@Override
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		//@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
		Route route = restMap.getUpdatePath(model);
		executeOperation(model, route, null, listener, errorListener);
		
	}
	
	@Override
	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener) {
		Route route = restMap.getDestroyPath(model);
		Listener<T> proxyListener = new VoidListenerProxy(listener);
		executeOperation(model, route, null, proxyListener, errorListener);
	}
	
	public class VoidListenerProxy implements Listener<T>{
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
