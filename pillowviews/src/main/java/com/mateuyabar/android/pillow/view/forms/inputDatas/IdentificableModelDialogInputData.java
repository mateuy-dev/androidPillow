//package com.mateuyabar.android.pillow.view.forms.inputDatas;
//
//import java.util.Collection;
//import java.util.List;
//
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.view.ActionMode;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//import com.mateuyabar.android.pillow.data.models.IdentificableModel;
//import com.mateuyabar.android.pillow.Listeners;
//import com.mateuyabar.android.pillow.Listeners.ErrorListener;
//import com.mateuyabar.android.pillow.Listeners.Listener;
//import com.mateuyabar.android.pillow.Pillow;
//import com.mateuyabar.android.pillow.view.forms.BelongsToInputData;
//import com.mateuyabar.android.pillow.view.forms.InputData;
//import com.mateuyabar.android.pillow.view.forms.inputs.ModelDialogInputView;
//import com.mateuyabar.android.pillow.data.sync.CommonListeners;
//import com.mateuyabar.android.pillow.data.sync.ISynchDataSource;
//import com.mateuyabar.android.pillow.data.sync.CommonListeners.MultipleTasksListener;
//
//public class IdentificableModelDialogInputData<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T>{
//	Class<T> selectedClass;
//	
//	public IdentificableModelDialogInputData(Class<T> selectedClass) {
//		super();
//		this.selectedClass = selectedClass;
//	}
//
//	@Override
//	public View createView(Context context) {
//		return new ModelDialogInputView<T>(context, selectedClass);
//	}
//	
//	
//	public class SelectReferenceDialog extends DialogFragment {
//		List<T> models;
//		T selected;
//		
//		public SelectReferenceDialog(List<T> models){
//			this.models = models;
//		}
//	    @Override
//	    public Dialog onCreateDialog(Bundle savedInstanceState) {
//	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	        builder.setTitle("SELECT")
//	        		.setItems(getNames(), new OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							selected = models.get(which);
//						}
//					});
//	        return builder.create();
//	    }
//	    
//	    private String[] getNames() {
//			String[] result = new String[models.size()];
//			int i=0;
//			for(T herd: models){
//				result[i] = herd.toString();
//				++i;
//			}
//			return result;
//		}
//	}
//	
//	private void setValue(View view, T value){
//		selected = value;
//		IdentificableModelDialogInputData.super.setValue(view, value.toString());
//	}
//	
//	@Override
//	public void setValue(final View view, Object value) {
//		String id = (String) value;
//		T model;
//		try {
//			model = parentClass.newInstance();
//			model.setId(id);
//			dataSource.show(model, new Listener<T>() {
//				@Override
//				public void onResponse(T response) {
//					setValue(view, response);
//				}
//			}, CommonListeners.silentErrorListener);
//		} catch (Exception e) {
//			//TODO
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	public String getValue(View view) {
//		return selected.getId();
//	}
//}
