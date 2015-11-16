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

import com.android.volley.VolleyError;

public class Listeners{
	public interface Listener<T> extends com.android.volley.Response.Listener<T>{}
	public interface ViewListener<T> extends Listener<T>{}
	public interface ViewErrorListener extends ErrorListener{}
	public interface ErrorListener {
		public void onErrorResponse(PillowError error);
	};
	
	public static class VolleyErrorListener implements com.android.volley.Response.ErrorListener{
		ErrorListener listener;
		public VolleyErrorListener(ErrorListener listener) {
			this.listener = listener;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			listener.onErrorResponse(new PillowError(error));
		}
	}
}
