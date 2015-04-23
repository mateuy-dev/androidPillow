package com.mateuyabar.android.pillow.view.forms;

import com.mateuyabar.android.pillow.IdentificableModel;

public interface BelongsToInputData<T extends IdentificableModel> extends InputData{
	public void setParentClass(Class<T> parentClass);

}
