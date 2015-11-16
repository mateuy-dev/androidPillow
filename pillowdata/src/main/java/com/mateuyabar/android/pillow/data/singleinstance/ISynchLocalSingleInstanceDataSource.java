package com.mateuyabar.android.pillow.data.singleinstance;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.ISynchLocalDataSource;


public interface ISynchLocalSingleInstanceDataSource <T extends IdentificableModel> extends ISingleInstanceDataSource<T>, ISynchLocalDataSource<T>{
}
