//package cat.my.android.pillow.view.forms.inputDatas;
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
//import cat.my.android.pillow.IdentificableModel;
//import cat.my.android.pillow.Listeners;
//import cat.my.android.pillow.Listeners.ErrorListener;
//import cat.my.android.pillow.Listeners.Listener;
//import cat.my.android.pillow.Pillow;
//import cat.my.android.pillow.view.forms.BelongsToInputData;
//import cat.my.android.pillow.view.forms.InputData;
//import cat.my.android.pillow.view.forms.inputs.ModelDialogInputView;
//import cat.my.android.pillow.data.sync.CommonListeners;
//import cat.my.android.pillow.data.sync.ISynchDataSource;
//import cat.my.android.pillow.data.sync.CommonListeners.MultipleTasksListener;
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
