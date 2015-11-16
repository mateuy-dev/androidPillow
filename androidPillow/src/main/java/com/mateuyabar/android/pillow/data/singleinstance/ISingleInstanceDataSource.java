package com.mateuyabar.android.pillow.data.singleinstance;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;

/**
 * DataSource that contains only one model. Ex: User
 */
public interface ISingleInstanceDataSource<T extends IdentificableModel> extends IDataSource<T> {
    /**
     * @return the instance
     */
    public IPillowResult<T> get();

    /**
     * Stores the given instance.
     * If no id is given it will replace the stored model (if any).
     * If an id is given, and an instance exists it must contain the same id, or the result will contain an error.
     * @return
     */
    public IPillowResult<T> set(T model);

    public IPillowResult<Void> remove();

    /**
     * @return true if an instance has been saved
     */
    public IPillowResult<Boolean> exists();

}
