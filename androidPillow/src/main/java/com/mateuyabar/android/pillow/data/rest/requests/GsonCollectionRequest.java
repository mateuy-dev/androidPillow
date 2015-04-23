/**
 * Copyright Mateu Yábar (http://mateuyabar.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mateuyabar.android.pillow.data.rest.requests;

 


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.data.rest.Route;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
 

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