package cat.my.android.pillow.data.db;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.core.IPillowResult;

public interface IDBDataSourceForSynch<T extends IdentificableModel> extends IDBDataSource<T>{
	public IPillowResult<T> setAsNotDirty(T model);

}
