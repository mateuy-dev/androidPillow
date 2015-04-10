package cat.my.android.pillow.data.sync;

import java.util.Collection;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.db.IDBDataSource;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.rest.RestDataSource;


public interface ISynchDataSource<T extends IdentificableModel> extends IDBDataSource<T> {
	public IPillowResult<Void> sendDirty();
	public IPillowResult<Collection<T>> download();
	public Class<T> getModelClass();
	public RestDataSource<T> getRestDataSource();
	public IDbMapping<T> getDbFuncs();
}
