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

package com.mateuyabar.android.pillow.view.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.base.IModelAdapter;
import com.mateuyabar.android.pillow.view.base.PillowBaseListAdapter;
import com.mateuyabar.android.pillow.views.R;

@Deprecated
public class PillowListAdapter<T extends IdentificableModel> extends PillowBaseListAdapter<T> {
	int rowViewId = R.layout.list_row_layout;
	IModelAdapter<T> modelAdapter;

	public PillowListAdapter(Context context, Class<T> clazz) {
		super(context, clazz);
		modelAdapter = PillowView.getInstance(context).getViewConfiguration(clazz).getModelAdapter(context);
	}

	@Override
	public View getView(T model, View convertView, ViewGroup parent) {
		return modelAdapter.getView(model, convertView, parent);
	}


	public IModelAdapter<T> getModelAdapter() {
		return modelAdapter;
	}
}
