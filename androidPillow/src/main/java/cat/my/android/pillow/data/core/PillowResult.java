package cat.my.android.pillow.data.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.os.Handler;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Listeners.ViewErrorListener;
import cat.my.android.pillow.Listeners.ViewListener;
import cat.my.android.pillow.PillowError;

public class PillowResult<T> implements IPillowResult<T>{
	CountDownLatch lock;
	T result;
	PillowError error;
	List<Listener<T>> listeners = new ArrayList<Listener<T>>();
	List<ErrorListener> errorListeners = new ArrayList<ErrorListener>();
	Context context;
	
	public PillowResult(Context context){
		this.context = context;
		lock = new CountDownLatch(1);
	}
	
	/**
	 * Used by async methods
	 * @param result
	 */
	public PillowResult(Context context, T result){
		//No waiting
		this.context = context;
		this.result = result;
		lock = new CountDownLatch(0);
	}
	
	/**
	 * Used by async methods
	 */
	public PillowResult(Context context, PillowError error){
		//No waiting
		this.context = context;
		this.error = error;
		lock = new CountDownLatch(0);
	}
	
	public PillowResult(Context context, Exception exception){
		this(context, new PillowError(exception));
	}
	
	/**
	 * Listener set is synchonized
	 */
	@Override
	public synchronized PillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener){
		if(listener!=null)
			listeners.add(listener);
		
		if(errorListener!=null)
			errorListeners.add(errorListener);
		
		if(lock.getCount()==0){
			//Result already finished so we call the listeners
			callListeners();
		}
		return this;
	}
	
	public IPillowResult<T> addListener(Listener<T> listener){
		return setListeners(listener, null);
	}
	public IPillowResult<T> addErrorListener(ErrorListener errorListener){
		return setListeners(null, errorListener);
	}

	
	
	
	/**
	 * Result must be present when called
	 */
	private void callListeners(){
		if(error!=null){
			for(Iterator<ErrorListener> it=errorListeners.iterator(); it.hasNext();){
				ErrorListener errorListener = it.next(); 
				if(!(errorListener instanceof ViewErrorListener)){
					errorListener.onErrorResponse(error);
				}else{
					Handler mainHandler = new Handler(context.getMainLooper());
					mainHandler.post(new ErrorListenerRunnable(errorListener));
				}
				it.remove();
			}
		} else {
			for(Iterator<Listener<T>> it=listeners.iterator(); it.hasNext();){
				Listener<T> listener = it.next(); 
				if(!(listener instanceof ViewListener)){
					listener.onResponse(result);
					listener=null;
				}else{
					Handler mainHandler = new Handler(context.getMainLooper());
					mainHandler.post(new ListenerRunnable(listener));
				}
				it.remove();
			}
		}
	}
	
	public synchronized PillowResult<T> setResult(T result) {
		this.result = result;
		lock.countDown();
		callListeners();
		return this;
	}
	
	public synchronized PillowResult<T> setError(PillowError error) {
		this.error = error;
		lock.countDown();
		callListeners();
		
		return this;
	}
	
	public void await() throws PillowError{
		try {
			lock.await();
		} catch (InterruptedException e) {
			throw new PillowError(e);
		}
	}
	
	private void controlledAwait(){
		try {
			await();
		} catch (PillowError exception) {
			if(error!=null)
				setError(exception);
		}
	}

	@Override
	public T getResult() throws PillowError {
		controlledAwait();
		if(error!=null)
			throw error;
		return result;
	}
	
	@Override
	public PillowError getError() {
		controlledAwait();
		return error;
	}
	
	public static PillowResult<Void> newVoidResult(Context context){
		return new PillowResult<Void>(context, (Void) null);
	}

	private class ListenerRunnable implements Runnable{
		Listener<T> listener;
		public ListenerRunnable(Listener<T> listener) {
			this.listener = listener;
		}
		@Override
		public void run() {
			if(listener!=null){
				listener.onResponse(result);
				listener = null;
			}
		}
	}
	private class ErrorListenerRunnable implements Runnable{
		ErrorListener errorListener;
		public ErrorListenerRunnable(ErrorListener errorListener) {
			this.errorListener = errorListener;
		}	
		@Override
		public void run() {
			if(errorListener!=null){
				errorListener.onErrorResponse(error);
				errorListener = null;
			}
		}
	}
}
