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


package com.mateuyabar.android.pillow.data.core;

import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;

import java.util.ArrayList;
import java.util.List;

public class MultiTaskVoidResult extends PillowResult<Void> implements Listener, ErrorListener{
	List<IPillowResult<?>> subOperations = new ArrayList<IPillowResult<?>>();
	boolean lastOperationAdded;
	Integer operationsFinished = 0;
	PillowError error;

	public MultiTaskVoidResult() {
		super();
	}
	
	public synchronized void addOperation(IPillowResult<?> operation){
		subOperations.add(operation);
		operation.addListeners(this, this);
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
