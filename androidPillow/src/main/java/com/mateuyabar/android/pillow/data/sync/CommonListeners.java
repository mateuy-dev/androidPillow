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

package com.mateuyabar.android.pillow.data.sync;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.google.gson.Gson;
import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.view.message.DisplayMessages;
import com.mateuyabar.util.exceptions.BreakFastException;

public class CommonListeners {
	public static ErrorListener dummyErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(PillowError error) {
			error.printStackTrace();
		}
	};
	public static ErrorListener breakFastListener = new ErrorListener() {
		@Override
		public void onErrorResponse(PillowError error) {
			throw new BreakFastException(error);
		}
	};
	public static ErrorListener silentErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(PillowError error) {
			Log.w(Pillow.LOG_ID, error.getMessage());
		}
	};
	
	public static final ErrorListener defaultErrorListener= new ErrorListener(){
		@Override
		public void onErrorResponse(PillowError error) {
			if(error.getCause() instanceof NoConnectionError){
				Log.i(Pillow.LOG_ID, error.getMessage());
			} else {
				throw new BreakFastException(error.getCause());
			}
		}
	};

	public static class ErrorListenerWithNoConnectionToast  implements Listeners.ViewErrorListener {
		Context context;

		public ErrorListenerWithNoConnectionToast(Context context) {
			this.context = context;
		}

		@Override
		public void onErrorResponse(PillowError error) {
			if(error.getCause() instanceof NoConnectionError){
				DisplayMessages.error(context, R.string.no_connection_error);
			} else {
				throw new BreakFastException(error.getCause());
			}
		}
	}
	
	public static ErrorListener getDefaultThreadedErrorListener(){
		return new DefaultThreadedErrorListener();
	}
	
	public static class DefaultThreadedErrorListener implements ErrorListener{
		long initialThreadId;
		StackTraceElement[] originalStack;
		public DefaultThreadedErrorListener() {
			Thread current = Thread.currentThread();
			initialThreadId = current.getId();
			originalStack = current.getStackTrace();
		}
		
		@Override
		public void onErrorResponse(PillowError error) {
			if(error.getCause() instanceof NoConnectionError){
				Log.i(Pillow.LOG_ID, error.getMessage());
				return;
			} /*else if(error.getCause() instanceof ServerError) {
				ServerError serverError = (ServerError) error.getCause();
				if(serverError.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){

				}
			}*/
			BreakFastException throwable = new BreakFastException(error.getCause());
			if(Thread.currentThread().getId()!=initialThreadId){
				throwable.setStackTrace(originalStack);
			}
			throw throwable;
		}
	};
	
	public static Listener dummyListener = new Listener() {
		@Override
		public void onResponse(Object response) {
		}
	};
	
	public static class DummyToastListener<T> implements ViewListener<T>{
		String text;
		Context context;
		public DummyToastListener(Context context, String text) {
			this.context = context;
			this.text= text;
		}
		@Override
		public void onResponse(T response) {
			DisplayMessages.info(context, text);
		}
	}
		
	public static Listener dummyLogListener = new Listener(){
		@Override
		public void onResponse(Object response) {
			System.out.println(new Gson().toJson(response).toString());
		}
	};
	
	
	public static class ProxyListener<T, D> implements Listener<D>{
		Listener<T> listener;
		T response;
		public ProxyListener(Listener<T> listener, T response){
			this.listener = listener;
			this.response = response;
		}

		@Override
		public void onResponse(D dummyResponse) {
			listener.onResponse(response);
		}
	}
	
	public static abstract class ExecuteOnMainThreadListener<T> implements Listener<T>{
		Context context;
		public ExecuteOnMainThreadListener(Context context){
			this.context = context;
		}
		public void onResponse(T response) {
			Handler mainHandler = new Handler(context.getMainLooper());
			Runnable myRunnable = new MainThreadExecutor(response);
			mainHandler.post(myRunnable);
		}
		public abstract void onResponseInMainThread(T response);
		
		private class MainThreadExecutor implements Runnable{
			T response;
			public MainThreadExecutor(T response) {
				this.response = response;
			}
			@Override
			public void run() {
				onResponseInMainThread(response);
			}
		}
	}
	
	public static class ExecuteOnMainThreadProxyListener<T> extends ExecuteOnMainThreadListener<T>{
		Listener<T> listener;
		Context context;
		
		public ExecuteOnMainThreadProxyListener(Context context, Listener<T> listener) {
			super(context);
			this.listener = listener;
		}
		
		@Override
		public void onResponseInMainThread(T response) {
			listener.onResponse(response);
		}
	}
	
	/**
	 * Used when you want to wait for more than one task to finish.
	 */
	public static class MultipleTasksListener<T> implements Listener<T>{
		int tasks, finished;
		Listener<T> listener;
		/**
		 * 
		 * @param tasks number of tasks to finish
		 * @param listener listener to execute once all the tasks have finished
		 */
		public MultipleTasksListener(int tasks, Listener<T> listener) {
			super();
			this.tasks = tasks;
			this.listener=listener;
		}
		@Override
		public synchronized void onResponse(T response) {
			finished ++;
			if(finished == tasks){
				listener.onResponse(response);
			}
		}
	}
	
	/**
	 * Used when you want to wait for more than one task to finish.
	 */
	public static class MultipleTasksViewListener<T> extends MultipleTasksListener<T> implements ViewListener<T>{
		public MultipleTasksViewListener(int tasks, Listener listener) {
			super(tasks, listener);
		}

	}
	
	public static class MultipleTasksProxyListener<T, D> extends MultipleTasksListener<D>{
		T response;
		public MultipleTasksProxyListener(int tasks, Listener<T> listener, T response) {
			super(tasks, new ProxyListener<T, D>(listener, response));
		}
	}
	
	public static abstract class MultipleResultTasksListener<T> implements Listener<T>{
		PillowResult<T> result;

		public MultipleResultTasksListener(PillowResult<T> result) {
			this.result = result;
		}
		@Override
		public void onResponse(T response) {
			result.setResult(subTask(response));
		}

		public abstract T subTask(T response);
	}

	public static class DisplayMessageListener<T> implements ViewListener<T>{
		int messageId;
		Context context;

		public DisplayMessageListener(Context context, int messageId) {
			this.context = context;
			this.messageId = messageId;
		}

		@Override
		public void onResponse(T response) {
			DisplayMessages.info(context, messageId);
		}
	}
}
