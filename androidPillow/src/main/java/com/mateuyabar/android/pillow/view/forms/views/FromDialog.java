package com.mateuyabar.android.pillow.view.forms.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.mateuyabar.android.pillow.view.forms.TFormView;


public class FromDialog<T> {
	Context context;
	String[] attributes = null;
	T model;
	OnClickListener okClickListener, cancelClickListener;
	TFormView<T> form ;
	boolean editable;
	
	public FromDialog(Context context, T model, boolean editable){
		this.context = context;
		this.model = model;
		this.editable = editable;
		initListeners();
	}
	
	

	protected void initListeners() {
		cancelClickListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
	}



	public void show(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Change herd location");
		
		form = new TFormView<T>(context, editable);
		form.setModel(model, attributes);
		
		// Set up the input
		builder.setView(form);

		// Set up the buttons
		builder.setPositiveButton("Ok", okClickListener);
		if(editable)
			builder.setNegativeButton("Cancel", cancelClickListener);

		builder.show();
	}

	/**
	 * Indicate which attributes must be shown and in which order. If not defined all are shown
	 * @param attributes
	 */
	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}

	/**
	 * Set an specific on Ok Click Listener
	 * @param okClickListener
	 */
	public void setOkClickListener(DialogInterface.OnClickListener okClickListener) {
		this.okClickListener = okClickListener;
	}

	/**
	 * Set an specific on Cancel Click Listener
	 * @param cancelClickListener
	 */
	public void setCancelClickListener(DialogInterface.OnClickListener cancelClickListener) {
		this.cancelClickListener = cancelClickListener;
	}



	public Context getContext() {
		return context;
	}



	public T getModel() {
		return model;
	}



	public TFormView<T> getForm() {
		return form;
	}
	
	

}
