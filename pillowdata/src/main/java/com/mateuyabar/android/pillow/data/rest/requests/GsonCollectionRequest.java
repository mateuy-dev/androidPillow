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
package com.mateuyabar.android.pillow.data.rest.requests;

 


import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.data.rest.Route;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
 

public class GsonCollectionRequest<T> extends AbstractGsonRequest<Collection<T>> {
	StackTraceElement[] originalStackTraces;
	
	Type collectionType;
	
	public GsonCollectionRequest(Gson gson, Route route, Type collectionType, Map<String, Object> params, Listener<Collection<T>> listener, ErrorListener errorListener, int initialTimeOutMs) {
		super(gson, route, params, listener, errorListener, initialTimeOutMs);
		this.collectionType = collectionType;
		this.originalStackTraces = Thread.currentThread().getStackTrace();
	}
    
    @Override
    protected Response<Collection<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            Collection<T> data = getGson().fromJson(json, collectionType);
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}