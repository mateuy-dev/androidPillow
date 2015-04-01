package cat.my.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.util.Log;
import android.view.View;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.view.forms.BelongsToInputData;
import cat.my.android.pillow.view.forms.inputs.HasManyInputView;
import cat.my.util.exceptions.UnimplementedException;

public class HasManyInput<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T>{
	Class<T> parentClass;
	
	@Override
	public Object getValue() {
		return getView().getValue();
	}
	
	@Override
	public void setValue(Object value) {
		Log.w(Pillow.LOG_ID, "HasManyInput setValue not implemented");
	}
	
	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}
	
	@Override
	protected HasManyInputView<T> getView() {
		return (HasManyInputView<T>) super.getView();
	}
	
	@Override
	protected View createView(Context context) {
		return new HasManyInputView<T>(context, parentClass);
	}
}
