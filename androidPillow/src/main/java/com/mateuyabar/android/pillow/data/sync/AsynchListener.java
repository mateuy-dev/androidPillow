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

import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;

import java.util.concurrent.CountDownLatch;

public class AsynchListener<T> implements Listener<T>, ErrorListener{
	CountDownLatch lock;
	T result; 
	

	PillowError error;
	
	public AsynchListener() {
		lock = new CountDownLatch(1);
	}
	
	@Override
	public void onResponse(T response) {
		this.result = response;
		lock.countDown();
	}

	@Override
	public void onErrorResponse(PillowError error) {
		this.error = error;
		lock.countDown();
	}
	
	public void await(){
		try {
			lock.await();
		} catch (InterruptedException e) {
			if(error!=null)
				error = new PillowError(e);
		}
	}
	
	public T getResult() {
		await();
		return result;
	}

	public PillowError getError() {
		await();
		return error;
	}

}
