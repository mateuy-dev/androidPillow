package cat.my.android.pillow.view.forms.inputDatas.display;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.Listeners.ViewListener;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.view.forms.BelongsToInputData;
import cat.my.android.pillow.view.forms.inputDatas.AbstractInputData;
import cat.my.util.exceptions.BreakFastException;

public class BelongsToTextDisplay<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T>{
	Class<T> parentClass;
	Object value;
	IDataSource<T> dataSource;
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}
	
	@Override
	public void setValue(Object value){
		String modelId = (String) value;
		if(modelId==null){
			getView().setText("");
		} else {
			try {
				T toSearch = parentClass.newInstance();
				toSearch.setId(modelId);
				
				dataSource.show(toSearch).setListeners(new ViewListener<T>() {
					@Override
					public void onResponse(T response) {
						getView().setText(response.toString());
					}
				}, CommonListeners.defaultErrorListener);
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
	}

	protected String valueToString() {
		return value.toString();
	}

	@Override
	protected View createView(Context context) {
		TextView result =  new TextView(context);
		dataSource = Pillow.getInstance(context).getDataSource(parentClass);
		return result;
	}
	
	@Override
	protected TextView getView() {
		return (TextView) super.getView();
	}

}
