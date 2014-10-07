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
package cat.my.android.restvolley.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.rest.IAuthenticationData.NullAuthenticationData;
import cat.my.android.restvolley.rest.requests.GsonCollectionRequest;
import cat.my.android.restvolley.rest.requests.GsonRequest;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class RestDataSource<T extends IdentificableModel> implements IDataSource<T> {
	
	RequestQueue volleyQueue;
	IAuthenticationData authenticationData;
	IRestMapping<T> restMapping;
	boolean serverRequiresAuthentication=false;
	
	
	public RestDataSource(IRestMapping<T> restMapping, Context context) {
		this(restMapping, context, new NullAuthenticationData());
	}
	
	public RestDataSource(IRestMapping<T> restMapping, Context context, IAuthenticationData authenticationData) {
		this.restMapping=restMapping;
		this.volleyQueue = Volley.newRequestQueue(context);
		this.authenticationData = authenticationData;
	}
	
	public IRestMapping<T> getRestMapping() {
		return restMapping;
	}

	/**
	 * @return false if authentication is required but not provided
	 */
	public boolean checkAuthenticationRequired() {
		return !serverRequiresAuthentication || authenticationData.isAuthenticated();
	}
	public void setServerRequiresAuthentication(boolean serverRequiresAuthentication) {
		this.serverRequiresAuthentication = serverRequiresAuthentication;
	}
	
	public void executeCollectionListOperation(int method, String operation, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = restMapping.getCollectionRoute(method, operation);
		executeListOperation(route, params, listener, errorListener);
	}
	
	public void executeMemberOperation(T model, int method, String operation, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMapping.getMemberRoute(model, method, operation);
		executeOperation(model, route, params, listener, errorListener);
	}
	
	public void executeCollectionOperation(T model, int method, String operation, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMapping.getCollectionRoute(method, operation);
		executeOperation(model, route, params, listener, errorListener);
	}
	
	private void executeListOperation(Route route, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener) {
		if(checkAuthenticationRequired()){
			Map<String, Object> map=new HashMap<String, Object>(authenticationData.getSession());
			if(params!=null){
				map.putAll(params);
			}
			GsonCollectionRequest<T> gsonRequest = new GsonCollectionRequest<T>(restMapping.getSerializer(), route, restMapping.getCollectionType(), map, listener, errorListener);
			gsonRequest.setShouldCache(false);
			volleyQueue.add(gsonRequest);
		} else {
			errorListener.onErrorResponse(new AuthenticationRequiredException());
		}
	}
	
	private void executeOperation(T model, Route route, Map<String, Object> params, Listener<T> listener, ErrorListener errorListener) {
		if(checkAuthenticationRequired()){
			Map<String, Object> map=new HashMap<String, Object>(authenticationData.getSession());
			if(params!=null){
				map.putAll(params);
			}
			if(model!=null){
				map.put(restMapping.getModelName(), model);
			}
			
			GsonRequest<T> gsonRequest = new GsonRequest<T>(restMapping.getSerializer(), route, restMapping.getModelClass(), map, listener, errorListener);
			gsonRequest.setShouldCache(false);
			volleyQueue.add(gsonRequest);
		} else {
			errorListener.onErrorResponse(new AuthenticationRequiredException());
		}
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		Route route = restMapping.getIndexPath();
		executeListOperation(route, null, listener, errorListener);
	}

	@Override
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMapping.getShowPath(model);
		executeOperation(model, route, null, listener, errorListener);
	}
	
	@Override
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		Route route = restMapping.getCreatePath(model);
		executeOperation(model, route, null, listener, errorListener);
	}
	
	@Override
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		//@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
		Route route = restMapping.getUpdatePath(model);
		executeOperation(model, route, null, listener, errorListener);
		
	}
	
	@Override
	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener) {
		Route route = restMapping.getDestroyPath(model);
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
	
	public static class AuthenticationRequiredException extends VolleyError{
		
	}
	
}
