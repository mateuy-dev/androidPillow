package com.mateuyabar.android.pillow.data.core;

import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;

public interface IPillowResult<T>{
	/*Listener<T> listener;
	ErrorListener errorListener;*/
	
	public void await() throws PillowError;
	public T getResult() throws PillowError;
	public IPillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener);
	public IPillowResult<T> addListener(Listener<T> listener);
	public IPillowResult<T> addErrorListener(ErrorListener errorListener);
	public PillowError getError();
	
	

}
