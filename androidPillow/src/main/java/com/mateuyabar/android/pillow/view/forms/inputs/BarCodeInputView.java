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
