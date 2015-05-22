package com.mateuyabar.android.pillow.data.singleinstance;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;

/**
 * Created by mateuyabar on 22/05/15.
 */
public interface ISingleInstanceDataSource<T extends IdentificableModel> extends IDataSource<T> {
    public PillowResult<T> get();
    public PillowResult<Boolean> exists();

}
