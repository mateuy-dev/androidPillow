package cat.my.android.pillow.data.core;

import android.content.Context;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.PillowError;

public interface IPillowResult<T>{
	/*Listener<T> listener;
	ErrorListener errorListener;*/
	
	public T getResult() throws PillowError;
	public IPillowResult<T> setListeners(Listener<T> listener, ErrorListener errorListener);
	public IPillowResult<T> setViewListeners(Listener<T> listener, ErrorListener errorListener);
	public PillowError getError();
	public IPillowResult<T> addSystemListener(Listener<T> listener);
	

}
