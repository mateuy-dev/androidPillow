package com.mateuyabar.android.pillow.data.core;


import android.content.Context;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;

public class PillowResultProxyType<T, K>  extends PillowResult<T> implements  Listener<K>, ErrorListener{
	IPillowResult<K> subOperation;
	T result;
	public PillowResultProxyType(Context context, T result, IPillowResult<K> subOperation){
		super(context);
		this.subOperation=subOperation;
		this.result = result;
		
		subOperation.setListeners(this, this);
	}

	@Override
	public void onResponse(K response) {
		setResult(result);
	}

	@Override
	public void onErrorResponse(PillowError error) {
		setError(error);
	}
	
}