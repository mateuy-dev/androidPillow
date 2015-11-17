package com.mateuyabar.android.pillow.data;

import com.mateuyabar.android.pillow.data.core.IPillowResult;

import java.util.Map;

public interface IRestDataSource<T> extends IDataSource<T> {
/*    IPillowResult<Collection<T>> executeCollectionListOperation(int method, String operation, Map<String, Object> params);

    IPillowResult<T> executeMemberOperation(T model, int method, String operation, Map<String, Object> params);*/

    IPillowResult<T> executeCollectionOperation(T model, int method, String operation, Map<String, Object> params);
}
