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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.view.base.PillowBaseListAdapter;





public class PillowListAdapter<T extends IdentificableModel> extends PillowBaseListAdapter<T> {
	int rowViewId = R.layout.list_row_layout;

	public PillowListAdapter(Context context, Class<T> clazz) {
		super(context, clazz);
	}
	
	
	
	public void updateListView(T model, TextView titleView, TextView textView, ImageView imageView){
		textView.setText(model.getId());
		titleView.setText(model.toString());
	}
	
	@Override
	public View getView(T model, View convertView, ViewGroup parent) {
		TextView textView, titleView;
		ImageView imageView;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(rowViewId, parent, false);
			textView = (TextView) convertView.findViewById(R.id.row_sub_title);
			titleView = (TextView) convertView.findViewById(R.id.row_main_text);
			imageView = (ImageView) convertView.findViewById(R.id.row_icon);
			convertView.setTag(createViewHolder(textView, titleView, imageView));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			textView = viewHolder.textView;
			titleView = viewHolder.titleView;
			imageView = viewHolder.imageView;
		}
		updateListView(model, titleView, textView, imageView);

		return convertView;
	}
	
	public void setRowViewId(int rowViewId) {
		this.rowViewId = rowViewId;
	}

	
	protected Object createViewHolder(TextView textView, TextView titleView, ImageView imageView) {
		return new ViewHolder(textView, titleView, imageView);
	}

	/**
	 * ViewHolder
	 */
	protected class ViewHolder {
		public final TextView textView;
		public final TextView titleView;
		public final ImageView imageView;


		public ViewHolder(TextView textView, TextView titleView, ImageView imageView) {
			this.textView = textView;
			this.titleView = titleView;
			this.imageView = imageView;
		}
	}
	
	
	
}
