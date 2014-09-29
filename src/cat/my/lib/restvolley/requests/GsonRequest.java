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
package cat.my.lib.restvolley.requests;

 
import android.util.Log;
import cat.my.lib.restvolley.pathbuilders.Route;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

 
import java.io.UnsupportedEncodingException;
import java.util.Map;
 

public class GsonRequest<T> extends JsonRequest<T> {
	private Gson gson;
	Class<T> clazz;
	
	public GsonRequest(Gson gson, Route route, Class<T> clazz, String requestBody, Listener<T> listener, ErrorListener errorListener) {
		super(route.getMethod(), route.getUrl(), requestBody, listener, errorListener);
		this.gson = gson;
		this.clazz = clazz;
		setRetryPolicy(new DefaultRetryPolicy(
                90000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}
    
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
        	e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
    
    
}