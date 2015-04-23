package com.mateuyabar.android.pillow.view.forms.inputDatas;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.view.forms.BelongsToInputData;
import com.mateuyabar.android.pillow.view.forms.inputs.ModelDialogInputView;
import android.content.Context;
import android.view.View;

public class BelongsToDialogInputData<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T>{

	
	Class<T> parentClass;
	
	@Override
	public Object getValue() {
		return getView().getValue();
	}

	@Override
	public void setValue(Object value) {
		getView().setValue((String) value);
	}

	@Override
	protected View createView(Context context) {
		return new ModelDialogInputView<T>(context, parentClass);
	}
	
	@Override
	public ModelDialogInputView<T> getView() {
		return (ModelDialogInputView<T>) super.getView();
	} 

	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}

}
