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

/**
 * Represents a result of a computation that may be asynchronous.
 *
 * We can obtain the value of it synchronously {@link IPillowResult#get()} (will wait until result is present or asynchronously {@link IPillowResult#addListeners(Listener, ErrorListener)}
 *
 * @param <T> Type of the computed result
 */
public interface IPillowResult<T>{
	/*Listener<T> listener;
	ErrorListener errorListener;*/

    /**
     * Waits untill the result is obtained
     * @throws PillowError
     */
	public void await() throws PillowError;

    /**
     * Waits untill the result is obtained and returns it.
     * @return result
     * @throws PillowError
     */
	public T get() throws PillowError;

    /**
     * Adds to listeners called when result is computed.
     * @param listener called when result computed
     * @param errorListener called when there is an error obtaining the result
     * @return this
     */
	public IPillowResult<T> addListeners(Listener<T> listener, ErrorListener errorListener);

    /**
     * Adds to listeners called when result is computed.
     * @param listener called when result computed
     * @return this
     */
	public IPillowResult<T> addListener(Listener<T> listener);

    /**
     * Adds to listeners called when result is computed.
     * @param errorListener called when there is an error obtaining the result
     * @return this
     */
	public IPillowResult<T> addErrorListener(ErrorListener errorListener);

    /**
     * Waits until result is computed.
     * @return Returns an error found if any.
     */
	public PillowError getError();
	
	

}
