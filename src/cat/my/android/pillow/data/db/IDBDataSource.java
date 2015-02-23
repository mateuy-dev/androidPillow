package cat.my.android.pillow.data.db;

import java.util.Collection;

import cat.my.android.pillow.IExtendedDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.core.IPillowResult;

public interface IDBDataSource<T extends IdentificableModel> extends IExtendedDataSource<T>{

	public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order);
	public DBModelController<T> getDbModelController();
	public IPillowResult<Integer> count(String selection, String[] selectionArgs);
	

}
