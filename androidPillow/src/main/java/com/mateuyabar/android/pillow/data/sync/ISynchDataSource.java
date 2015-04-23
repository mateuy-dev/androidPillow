package com.mateuyabar.android.pillow.data.sync;

import java.util.Collection;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.db.IDBDataSource;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.rest.RestDataSource;


public interface ISynchDataSource<T extends IdentificableModel> extends IDBDataSource<T> {
	public IPillowResult<Void> sendDirty();
	public IPillowResult<Collection<T>> download();
	public Class<T> getModelClass();
	public RestDataSource<T> getRestDataSource();
	public IDbMapping<T> getDbFuncs();
}
