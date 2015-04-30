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

package com.mateuyabar.android.pillow;

import java.util.Collection;

import com.mateuyabar.android.pillow.data.core.IPillowResult;



public interface IDataSource <T>{
	/**
	 * Returns all the instances
	 * @param listener to be executed when the instances have been retrieved
	 * @param errorListener to be executed in case of error
	 */
	public IPillowResult<Collection<T>> index();

	public IPillowResult<T> show(T model);
	
	public IPillowResult<T> create(T model);
	
	public IPillowResult<T> update(T model);
	
	public IPillowResult<Void> destroy(T model);
}
