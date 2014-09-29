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
import java.util.concurrent.Future;

import org.json.JSONObject;

import cat.my.lib.restvolley.RestVolley.ISessionData;
import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.pathbuilders.IPathBuilder;
import cat.my.lib.restvolley.pathbuilders.RailsPathBuilder.Route;
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

public class RestVolleySync {
	RestVolley restVolley;
	
	private class ResultWaitListener<T> implements Listener<T>, ErrorListener{
		T result;
		boolean finished;
		VolleyError error;
		
		public synchronized void set(T result){
			this.result = result;
			finished = true;
			notifyAll();
		}
		
		public synchronized void setError(VolleyError error){
			this.error = error;
			finished = true;
			notifyAll();
		}
		
		public synchronized T get() throws InterruptedException, VolleyError{
			if(!finished){
				wait();
			}
			if(error!=null)
				throw error;
			else
				return result;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			setError(error);
		}

		@Override
		public void onResponse(T response) {
			set(response);
		}
	}

	public RestVolleySync(Context context, IPathBuilder pathBuilder) {
		this.restVolley = new RestVolley(context, pathBuilder);
	}
	
	public RestVolleySync(Context context, IPathBuilder pathBuilder, ISessionData sessionData) {
		this.restVolley = new RestVolley(context, pathBuilder, sessionData);
	}
	
	
	
	public <T extends IdentificableModel> Collection<T> index(Class<T> clazz, Type collectionType) throws InterruptedException, VolleyError {
		ResultWaitListener<Collection<T>> listener = new ResultWaitListener<Collection<T>>();
		restVolley.index(clazz, collectionType, listener, listener);
		return listener.get();
	}

	public <T extends IdentificableModel> T show(Class<T> clazz, T model) throws InterruptedException, VolleyError{
		ResultWaitListener<T> listener = new ResultWaitListener<T>();
		restVolley.show(clazz, model, listener, listener);
		return listener.get();
	}
	
	public <T extends IdentificableModel> T create(Class<T> clazz, T model) throws InterruptedException, VolleyError {
		ResultWaitListener<T> listener = new ResultWaitListener<T>();
		restVolley.create(clazz, model, listener, listener);
		return listener.get();
	}
	
	/**
	 * 
	 * @param clazz
	 * @param model
	 * @param listener ATENTION: update operation may return emty result on server. This will result in null T in the listener. Return the T from the server if required
	 * @param errorListener
	 * @throws VolleyError 
	 * @throws InterruptedException 
	 */
	public <T extends IdentificableModel> T update(Class<T> clazz, T model) throws InterruptedException, VolleyError {
		ResultWaitListener<T> listener = new ResultWaitListener<T>();
		restVolley.update(clazz, model, listener, listener);
		return listener.get();
	}
	
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model) throws InterruptedException, VolleyError {
		ResultWaitListener<Void> listener = new ResultWaitListener<Void>();
		restVolley.destroy(clazz, model, listener, listener);
		listener.get();
	}
}
