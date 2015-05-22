package com.mateuyabar.android.pillow.data.sync.singleinstance;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.singleinstance.ISingleInstanceDataSource;
import com.mateuyabar.android.pillow.data.sync.ISynchDataSource;

/**
 * Created by mateuyabar on 22/05/15.
 */
public interface ISynchSingleInstanceDataSource<T extends IdentificableModel> extends ISingleInstanceDataSource<T>, ISynchDataSource<T> {
}
