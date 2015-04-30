/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.mateuyabar.android.pillow.data.rest;

import android.content.Context;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursorRestDataSource<T extends IdentificableModel> extends RestDataSource<T> {
    public CursorRestDataSource(IRestMapping<T> restMapping, Context context) {
        super(restMapping, context);
    }

    public CursorRestDataSource(IRestMapping<T> restMapping, Context context, IAuthenticationController authenticationController) {
        super(restMapping, context, authenticationController);
    }

    public IPillowResult<Collection<T>> index(int size, int offset){
        Route route = getRestMapping().getIndexPath();
        Map<String, Object> params = new HashMap<>();
        params.put("page", (offset/size) +1);
        return executeListOperation(route, params);
    }

    public IPillowResult<List<IPillowResult<T>>> cursorIndex() {
        return new PillowResult(new DataSourceCursoredList());
    }

    private class DataSourceCursoredList extends AbstractDataSourceCursoredList<T>{
        private DataSourceCursoredList() {
            super(getContext());
        }

        @Override
        public IPillowResult<Collection<T>> getData(int size, int offset) {
            return index(size, offset);
        }
    }


}
