//package com.mateuyabar.android.pillow.data.core;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
//import android.content.Context;
//import com.mateuyabar.android.pillow.Listeners.ErrorListener;
//import com.mateuyabar.android.pillow.Listeners.Listener;
//import com.mateuyabar.android.pillow.PillowError;
//import com.mateuyabar.util.exceptions.ToImplementException;
//
//public class CopyOfProxyPillowResult<T> implements IPillowResult<T>{
//	CountDownLatch lock;
//	IPillowResult<T> mainPillowResult;
//	Listener<T> listener;
//	ErrorListener errorListener;
//	Context context;
//	boolean viewListener;
//	List<Listener<T>> systemListeners = new ArrayList<Listener<T>>();
//	
//	public CopyOfProxyPillowResult(){
//		lock = new CountDownLatch(1);
//	}
//	
//	public synchronized void setMainPillowResult(IPillowResult<T> mainPillowResult) {
//		this.mainPillowResult = mainPillowResult;
//		if(listener!=null){
//			if(viewListener)
//				mainPillowResult.setViewListeners(listener, errorListener);
//			else
//				mainPillowResult.setListeners(listener, errorListener);
//		}
//		for(Listener<T> listener: systemListeners){
//			mainPillowResult.addSystemListener(listener);
//		}
//		lock.countDown();
//	}
//	
//	public synchronized CopyOfProxyPillowResult<T> addSystemListener(Listener<T> listener){
//		systemListeners.add(listener);
//		return this;
//	}
//	
//	@Override
//	public void await() throws PillowError {
//		try {
//			lock.await();
//		} catch (InterruptedException e) {
//			throw new PillowError(e);
//		}
//	}
//
//	@Override
//	public T getResult() throws PillowError {
//		await();
//		return mainPillowResult.getResult();
//	}
//	
//	@Override
//	public PillowError getError() {
//		try {
//			lock.await();
//		} catch (InterruptedException e) {
//			return  new PillowError(e);
//		}
//		return mainPillowResult.getError();
//	}
//	
//	
//	@Override
//	public IPillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener) {
//		return setListeners(listener, errorListener, false);
//	}
//
//	@Override
//	public synchronized  IPillowResult<T> setViewListeners(Listener<T> listener, ErrorListener errorListener) {
//		return setListeners(listener, errorListener, true);
//	}
//	
//	
//	public IPillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener, boolean viewListener) {
//		if(this.listener!=null || this.errorListener!=null)
//			throw new ToImplementException("should be a list of exceptions");
//		this.viewListener = viewListener;
//		this.listener = listener;
//		this.errorListener = errorListener;
//		if(mainPillowResult!=null){
//			mainPillowResult.setViewListeners(listener, errorListener);
//		}
//		return this;
//	}
//
//	
//}
