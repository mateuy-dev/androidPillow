package com.mateuyabar.android.pillow.data.sync.db;

import com.mateuyabar.android.pillow.data.db.IDBDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.ISynchDataSource;

/**
 * Created by mateuyabar on 22/05/15.
 */
public interface ISynchDbDataSource<T extends IdentificableModel> extends IDBDataSource<T>, ISynchDataSource<T> {
}
