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
package com.mateuyabar.android.pillow.data.rest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowConfigXml;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.db.MultiThreadDbDataSource.OperationRunnable;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController.AuthenticationData;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController.NullAuthenticationController;
import com.mateuyabar.android.pillow.data.rest.requests.GsonCollectionRequest;
import com.mateuyabar.android.pillow.data.rest.requests.GsonRequest;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RestDataSource<T> implements IDataSource<T> {
    private static ThreadPoolExecutor dbThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static final String LOG_ID = Pillow.LOG_ID +" - RestDataSource";
    public static boolean SIMULATE_OFFLINE_CONNECTIVITY_ON_TESTING = false;

    Context context;
    RequestQueue volleyQueue;
    IRestMapping<T> restMapping;

    IAuthenticationController authenticationController;


    public RestDataSource(IRestMapping<T> restMapping, Context context) {
        this(restMapping, context, new NullAuthenticationController());
    }

    public RestDataSource(IRestMapping<T> restMapping, Context context, IAuthenticationController authenticationController) {
        this.restMapping=restMapping;
        this.volleyQueue = VolleyFactory.newRequestQueue(context);
        if(authenticationController==null)
            authenticationController = new NullAuthenticationController();
        this.authenticationController = authenticationController;
        this.context = context;
    }

    public IRestMapping<T> getRestMapping() {
        return restMapping;
    }

    private PillowConfigXml getConfig(){
        return Pillow.getInstance(context).getConfig();
    }

//	/**
//	 * @return false if authentication is required but not provided
//	 */
//	public boolean checkAuthenticationRequired() {
//		return !serverRequiresAuthentication || authenticationController.isAuthenticated();
//	}
//	public void setServerRequiresAuthentication(boolean serverRequiresAuthentication) {
//		this.serverRequiresAuthentication = serverRequiresAuthentication;
//	}

    public IPillowResult<Collection<T>> executeCollectionListOperation(int method, String operation, Map<String, Object> params) {
        Route route = restMapping.getCollectionRoute(method, operation);
        return executeListOperation(route, params);
    }

    public IPillowResult<T> executeMemberOperation(T model, int method, String operation, Map<String, Object> params) {
        Route route = restMapping.getMemberRoute(model, method, operation);
        return executeOperation(model, route, params);
    }

    public IPillowResult<T> executeCollectionOperation(T model, int method, String operation, Map<String, Object> params) {
        Route route = restMapping.getCollectionRoute(method, operation);
        return executeOperation(model, route, params);
    }

    protected IPillowResult<Collection<T>> executeListOperation(final Route route, final Map<String, Object> params) {
        final PillowResultListener<Collection<T>> result = new PillowResultListener<Collection<T>>();

        Log.d(LOG_ID, "Executing operation "+route.method + " "+route.url + " "+params);
        if(SIMULATE_OFFLINE_CONNECTIVITY_ON_TESTING){
            return result.setError(new PillowError(new NoConnectionError()));
        }

        Listener<AuthenticationData> onSessionStarted = new Listener<AuthenticationData>() {
            @Override
            public void onResponse(AuthenticationData sessionData) {
                Map<String, Object> map = sessionData.getData();
                if(params!=null){
                    map.putAll(params);
                }
                Listeners.ErrorListener errorListener = new WrappSessionFailListener(result, authenticationController);
                GsonCollectionRequest<T> gsonRequest = new GsonCollectionRequest<T>(restMapping.getSerializer(), route, restMapping.getCollectionType(), map, result, errorListener, getConfig().getDownloadTimeInterval());
                gsonRequest.setShouldCache(false);
                volleyQueue.add(gsonRequest);
            }
        };

        IPillowResult<AuthenticationData> sessionData = authenticationController.getAuthentication();
        sessionData.addListeners(onSessionStarted, result);

        return result;
    }

    protected IPillowResult<T> executeOperation(final T model, final Route route, final Map<String, Object> params) {
        final PillowResultListener<T> result = new PillowResultListener<T>();
        Log.d(LOG_ID, "Executing operation "+route.method + " "+route.url + " "+params);

        if(SIMULATE_OFFLINE_CONNECTIVITY_ON_TESTING){
            return result.setError(new PillowError(new NoConnectionError()));
        }
        Listener<AuthenticationData> onSessionStarted = new Listener<AuthenticationData>() {
            @Override
            public void onResponse(AuthenticationData sessionData) {
                Map<String, Object> map = sessionData.getData();
                if(params!=null){
                    map.putAll(params);
                }
                if(model!=null){
                    map.put(restMapping.getModelName(), model);
                }
                Listeners.ErrorListener errorListener = new WrappSessionFailListener(result, authenticationController);
                GsonRequest<T> gsonRequest = new GsonRequest<T>(restMapping.getSerializer(), route, restMapping.getModelClass(), map, result, errorListener, getConfig().getDownloadTimeInterval());
                gsonRequest.setShouldCache(false);
                volleyQueue.add(gsonRequest);
            }
        };
        IPillowResult<AuthenticationData> sessionData = authenticationController.getAuthentication();
        sessionData.addListeners(onSessionStarted, result);

        return result;
    }

    private <K> IPillowResult<K> execute(OperationRunnable<K> runnable){
        dbThreadPool.execute(runnable);
        return runnable.getProxyResult();
    }

    @Override
    public IPillowResult<Collection<T>> index() {
        Route route = restMapping.getIndexPath();
        return executeListOperation(route, null);
    }

    @Override
    public IPillowResult<T> show(T model) {
        Route route = restMapping.getShowPath(model);
        return executeOperation(model, route, null);
    }

    @Override
    public IPillowResult<T> create(T model) {
        Route route = restMapping.getCreatePath(model);
        return executeOperation(model, route, null);
    }

    @Override
    public IPillowResult<T> update(T model) {
        //@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
        Route route = restMapping.getUpdatePath(model);
        return executeOperation(model, route, null);

    }

    @Override
    public IPillowResult<Void> destroy(T model) {
        Route route = restMapping.getDestroyPath(model);
        IPillowResult result = executeOperation(model, route, null);
        //Ugly way of changing T to Void...
        return result;
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

    public static class WrappSessionFailListener implements Listeners.ViewErrorListener{
        Listeners.ErrorListener mainListener;
        IAuthenticationController authenticationController;

        public WrappSessionFailListener(Listeners.ErrorListener mainListener, IAuthenticationController authenticationController) {
            this.mainListener = mainListener;
            this.authenticationController = authenticationController;
        }

        @Override
        public void onErrorResponse(PillowError error) {
            if(error.getCause() instanceof AuthFailureError) {
//                ServerError serverError = (ServerError) error.getCause();
//                if (serverError.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    authenticationController.resetAuthentication();
//                }
            }
            mainListener.onErrorResponse(error);
        }
    }

    public static class AuthenticationRequiredException extends VolleyError{
    }


/*
    public IPillowResult<List<IPillowResult<T>>> cursoredIndex(){

    }*/


    public Context getContext() {
        return context;
    }
}
