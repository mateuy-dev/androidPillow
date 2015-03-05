//package cat.my.android.pillow.view.forms.inputs;
//
//import android.content.Context;
//import android.widget.EditText;
//import cat.my.android.pillow.IDataSource;
//import cat.my.android.pillow.IdentificableModel;
//import cat.my.android.pillow.Listeners.Listener;
//import cat.my.android.pillow.Listeners.ViewListener;
//import cat.my.android.pillow.Pillow;
//import cat.my.android.pillow.data.sync.CommonListeners;
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
//			dataSource.show(toSearch).setListeners(new ViewListener<T>() {
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
