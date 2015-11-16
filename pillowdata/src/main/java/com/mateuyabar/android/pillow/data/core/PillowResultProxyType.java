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

public class PillowResultProxyType<T, K>  extends PillowResult<T> implements  Listener<K>, ErrorListener{
	IPillowResult<K> subOperation;
	T result;
	public PillowResultProxyType(T result, IPillowResult<K> subOperation){
		super();
		this.subOperation=subOperation;
		this.result = result;
		
		subOperation.addListeners(this, this);
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