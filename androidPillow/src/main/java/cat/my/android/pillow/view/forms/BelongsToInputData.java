package cat.my.android.pillow.view.forms;

import cat.my.android.pillow.IdentificableModel;

public interface BelongsToInputData<T extends IdentificableModel> extends InputData{
	public void setParentClass(Class<T> parentClass);

}
