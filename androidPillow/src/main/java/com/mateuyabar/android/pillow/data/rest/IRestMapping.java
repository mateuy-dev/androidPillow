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

package com.mateuyabar.android.pillow.data.rest;



import com.google.gson.Gson;

import java.lang.reflect.Type;

public interface IRestMapping<T> {

	public Route getCollectionRoute(int method, String operation);
	
	public Route getMemberRoute(T model, int method, String operation);
	
	public Route getIndexPath();

	public Route getCreatePath(T model);

	public Route getShowPath(T model);

	public Route getUpdatePath(T model);

	public Route getDestroyPath(T model);
	
	public Gson getSerializer();

	public String getModelName();
	
	public Class<T> getModelClass();
	
	public Type getCollectionType();

}