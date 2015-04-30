//package com.mateuyabar.android.pillow.view.forms.inputs;
//
//import android.content.Context;
//import android.widget.EditText;
//import com.mateuyabar.android.pillow.IDataSource;
//import com.mateuyabar.android.pillow.IdentificableModel;
//import com.mateuyabar.android.pillow.Listeners.Listener;
//import com.mateuyabar.android.pillow.Listeners.ViewListener;
//import com.mateuyabar.android.pillow.Pillow;
//import com.mateuyabar.android.pillow.data.sync.CommonListeners;
//
//public class TestModelDialogInputView<T extends IdentificableModel> extends EditText{
//	IDataSource<T> dataSource;
//	Class<T> selectedClass;
//	T selected;
//
//	public TestModelDialogInputView(Context context, Class<T> selectedClass) {
//		super(context);
//		this.selectedClass = selectedClass;
//		dataSource = Pillow.getInstance(context).getDataSource(selectedClass);
//	}
//
//	public void setValue(String modelId){
//		T toSearch;
//		try {
//			toSearch = selectedClass.newInstance();
//			toSearch.setId(modelId);
//			dataSource.show(toSearch).addListeners(new ViewListener<T>() {
//				@Override
//				public void onResponse(T response) {
//					selected = response;
//					setText(response.toString());
//				}
//			}, CommonListeners.defaultErrorListener);
//			
//		} catch (Exception e) {
//			//TODO check exception
//			e.printStackTrace();
//		}
//	}
//	
//	public T getValue(){
//		return selected;
//	}
//}
