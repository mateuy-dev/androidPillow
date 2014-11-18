package cat.my.android.restvolley.forms.views;

import javax.sql.DataSource;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.forms.TFormView;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;


public class FromDialog<T> {
	Context context;
	String[] attributes = null;
	T model;
	OnClickListener okClickListener, cancelClickListener;
	TFormView<T> form ;
	
	public FromDialog(Context context, T model){
		this.context = context;
		this.model = model;
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
		
		form = new TFormView<T>(context);
		form.setModel(model, attributes);
		
		// Set up the input
		builder.setView(form);

		// Set up the buttons
		builder.setPositiveButton("Ok", okClickListener);
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
