package com.mateuyabar.android.pillow.data.sync;

import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;

/**
 * Created by mateuyabar on 14/05/15.
 */
public interface ILocalSynchDataSource<T> extends IDataSource<T>{
    public IPillowResult<T> setAsNotDirty(T model);
}
