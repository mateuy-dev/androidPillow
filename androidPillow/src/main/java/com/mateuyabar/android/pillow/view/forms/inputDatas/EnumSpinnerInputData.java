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

package com.mateuyabar.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;

public class EnumSpinnerInputData extends AbstractSpinnerInputData<Enum<?>> {
	Class<?> valueClass;

	public EnumSpinnerInputData(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public ArrayAdapter<Enum<?>> createAdapter(Context context, Spinner spinner) {
		return new EnumSpinnerAdapter(context);
	}

	private class EnumSpinnerAdapter extends ArrayAdapter<Enum<?>> {
		public EnumSpinnerAdapter(Context context) {
			super(context, android.R.layout.simple_spinner_item);
			for (Object constant : valueClass.getEnumConstants()) {
				add((Enum<?>) constant);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
		}

		private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
			View view;
			TextView text;

			if (convertView == null) {
				view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
			} else {
				view = convertView;
			}

			try {
				text = (TextView) view;
			} catch (ClassCastException e) {
				Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
				throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
			}

			Enum<?> item = getItem(position);
			
			String label = StringResourceUtils.getLabel(getContext(), item);
			text.setText(label);

			return view;
		}

	}

	
}
