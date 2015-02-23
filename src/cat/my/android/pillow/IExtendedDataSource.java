package cat.my.android.pillow;

import java.util.Collection;

import cat.my.android.pillow.data.core.IPillowResult;

public interface IExtendedDataSource<T extends IdentificableModel> extends IDataSource<T>{
	public IPillowResult<Collection<T>> index(T filter);

}
