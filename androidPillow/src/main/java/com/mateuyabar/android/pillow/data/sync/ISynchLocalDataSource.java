package com.mateuyabar.android.pillow.data.sync;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;

import java.util.List;

/**
 * Created by mateuyabar on 14/05/15.
 */
public interface ISynchLocalDataSource<T> extends IDataSource<T>{
    public static final int DIRTY_STATUS_CLEAN = 0;
    public static final int DIRTY_STATUS_UPDATED = 1;
    public static final int DIRTY_STATUS_CREATED = 2;

    public IPillowResult<T> setAsNotDirty(T model);

    /**
     * DBModelController.DIRTY_STATUS_CREATED or DBModelController.DIRTY_STATUS_UPDATED or DIRTY_STATUS_CLEAN
     * @param dirtyType
     * @return
     */
    public List<T> getDirty(int dirtyType);
    public void cacheAll(List<T> models);
    public List<T> getDeletedModelsIds();
    public void setAsDeleted(String id);
}
