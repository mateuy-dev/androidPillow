package com.mateuyabar.android.pillow.data.core;

import java.util.ArrayList;
import java.util.List;

import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;
import android.content.Context;

public class MultiTaskVoidResult extends PillowResult<Void> implements Listener, ErrorListener{
	List<IPillowResult<?>> subOperations = new ArrayList<IPillowResult<?>>();
	boolean lastOperationAdded;
	Integer operationsFinished = 0;
	PillowError error;

	public MultiTaskVoidResult(Context context) {
		super(context);
	}
	
	public synchronized void addOperation(IPillowResult<?> operation){
		subOperations.add(operation);
		operation.setListeners(this, this);
	}
	
	public synchronized void setLastOperationAdded() {
		this.lastOperationAdded = true;
		checkLastOperationFinished();
	}
	
	private synchronized  void checkLastOperationFinished() {
		if(lastOperationAdded && operationsFinished == subOperations.size()){
			if(error!=null)
				setResult(null);
			else
				setError(error);
		}
	}

	@Override
	public synchronized void onResponse(Object response) {
		operationsFinished++;
		checkLastOperationFinished();
	}

	

	@Override
	public synchronized void onErrorResponse(PillowError error) {
		operationsFinished++;
		
		if(this.error==null){
			this.error = error;
		}
		checkLastOperationFinished();
	}

}
