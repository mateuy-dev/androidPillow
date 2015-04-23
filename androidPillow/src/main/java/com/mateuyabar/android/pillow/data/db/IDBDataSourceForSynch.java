package com.mateuyabar.android.pillow.data.db;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.core.IPillowResult;

public interface IDBDataSourceForSynch<T extends IdentificableModel> extends IDBDataSource<T>{
	public IPillowResult<T> setAsNotDirty(T model);

}
