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

package com.mateuyabar.android.pillow.view.forms.inputs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class ColorPicker extends Button implements OnClickListener{
	private static final int COLOR_TAG = 1;
	AlertDialog dialog;
	int color;
	String title = "";
	
	public void setTitle(String title) {
		this.title = title;
	}

	public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorPicker(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		super.setOnClickListener(this);
	}
	
	public int getColor() {
		return color;
	}

	public ColorPicker setColor(int color) {
		this.color = color;
		setBackgroundColor(color);
		return this;
	}
	
	@Override
	public void onClick(View v) {
		showDialog();
	}

	OnClickListener dialogColorListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int color = (Integer) v.getTag();
			setColor(color);
			dialog.dismiss();
		}
	};
	
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(title);

		int colors[] = {Color.parseColor("#33b5e5"), Color.parseColor("#aa66cc"), Color.parseColor("#99cc00"),
				Color.parseColor("#ffbb33"), Color.parseColor("#ff4444"), Color.parseColor("#0099cc") };

		LinearLayout rootView = new LinearLayout(getContext());
		for(int color : colors){
			Button button = new Button(getContext());
			button.setBackgroundColor(color);
			button.setTag(color);
			button.setOnClickListener(dialogColorListener);
			rootView.addView(button);
		}
		builder.setView(rootView);
		
		dialog = builder.create();
		dialog.show();
	}

	

}
