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

import android.content.Context;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;

public class PillowResultListener<T> extends PillowResult<T> implements Listener<T>, ErrorListener{

	@Override
	public void onResponse(T response) {
		setResult(response);
	}

	@Override
	public void onErrorResponse(PillowError error) {
		setError(error);
	}

	public PillowResultListener(Context context, Exception exception) {
		super(context, exception);

	}

	public PillowResultListener(Context context, PillowError error) {
		super(context, error);

	}

	public PillowResultListener(Context context, T result) {
		super(context, result);

	}

	public PillowResultListener(Context context) {
		super(context);

	}

}
