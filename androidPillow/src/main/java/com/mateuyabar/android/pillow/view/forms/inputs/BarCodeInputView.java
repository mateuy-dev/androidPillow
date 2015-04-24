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


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.mateuyabar.android.pillow.R;

public class BarCodeInputView extends LinearLayout implements OnClickListener{
	int REQUEST_CODE = 50001;
	EditText editText;

	public BarCodeInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BarCodeInputView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		editText = new EditText(getContext());
		addView(editText);
		ImageButton scanButton = new ImageButton(getContext());
		scanButton.setImageResource(R.drawable.ic_action_accept);
		addView(scanButton);
		scanButton.setOnClickListener(this);
	}
	
	public static class AuxFragment extends Fragment{
		public EditText editText;
		@Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        	if (resultCode == Activity.RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				editText.setText(contents);
			}
            super.onActivityResult(requestCode, resultCode, data);
            getFragmentManager().beginTransaction().remove(this).commit();
        }
	}

	@Override
	public void onClick(View v) {
		final FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
		AuxFragment auxiliary = new AuxFragment();
	    auxiliary.editText = editText;
	    fm.beginTransaction().add(auxiliary, "BAR_CODE_AUX_FRAGMENT").commit();
	    fm.executePendingTransactions();

	    try{
	    	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    	auxiliary.startActivityForResult(intent, REQUEST_CODE);
	    } catch (Exception e) {
	    	Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
			auxiliary.startActivity(marketIntent);
		}
	    
	}

	public String getValue(){
		return editText.getText().toString();
	}
	
	public void setValue(String value){
		editText.setText(value);
	}
	
}
