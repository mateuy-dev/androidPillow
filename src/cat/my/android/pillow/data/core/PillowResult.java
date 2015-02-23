package cat.my.android.pillow.data.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.os.Handler;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.PillowError;
import cat.my.util.exceptions.ToImplementException;

public class PillowResult<T> implements IPillowResult<T>{
	CountDownLatch lock;
	T result;
	PillowError error;
	Listener<T> listener;
	ErrorListener errorListener;
	Context context;
	boolean viewListeners = false;
	List<Listener<T>> systemListeners = new ArrayList<Listener<T>>();
	
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
	
	public synchronized PillowResult<T> addSystemListener(Listener<T> listener){
		systemListeners.add(listener);
		if(lock.getCount()==0){
			callListeners();
		}
		return this;
	}
	
	/**
	 * Listener set is synchonized
	 */
	@Override
	public PillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener){
		return setViewListeners( listener, errorListener, false);
	}
	
	@Override
	public PillowResult<T> setViewListeners(Listener<T> listener, ErrorListener errorListener){
		return setViewListeners(listener, errorListener, true);
	}
	
	public synchronized PillowResult<T> setViewListeners(Listener<T> listener, ErrorListener errorListener, boolean viewListeners){
		if(this.listener!=null || this.errorListener!=null)
			throw new ToImplementException("should be a list of exceptions");
		this.viewListeners = viewListeners;
		this.listener = listener;
		this.errorListener = errorListener;
		if(lock.getCount()==0){
			//Result already finished so we call the listeners
			callListeners();
		}
		return this;
	}
	
	
	
	/**
	 * Result must be present when called
	 */
	private void callListeners(){
		if(error!=null){
			if(errorListener!=null){
				if(!viewListeners){
					errorListener.onErrorResponse(error);
					errorListener=null;
				}else{
					Handler mainHandler = new Handler(context.getMainLooper());
					mainHandler.post(new ErrorListenerRunnable());
				}
			}
		} else {
			if(listener!=null){
				if(!viewListeners){
					listener.onResponse(result);
					listener=null;
				}else{
					Handler mainHandler = new Handler(context.getMainLooper());
					mainHandler.post(new ListenerRunnable());
				}
			}
			for(Iterator<Listener<T>> it=systemListeners.iterator(); it.hasNext();){
				Listener<T> systemListener = it.next(); 
				systemListener.onResponse(result);
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
		@Override
		public void run() {
			if(listener!=null){
				listener.onResponse(result);
				listener = null;
			}
		}
	}
	private class ErrorListenerRunnable implements Runnable{
		@Override
		public void run() {
			if(errorListener!=null){
				errorListener.onErrorResponse(error);
				errorListener = null;
			}
		}	
	}
}
