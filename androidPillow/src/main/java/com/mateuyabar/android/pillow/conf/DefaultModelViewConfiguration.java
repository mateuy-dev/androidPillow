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


package com.mateuyabar.android.pillow.conf;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.base.IModelListAdapter;
import com.mateuyabar.android.pillow.view.forms.views.FormActivity;
import com.mateuyabar.android.pillow.view.forms.views.PillowShowFragment;
import com.mateuyabar.android.pillow.view.list.PillowListAdapter;
import com.mateuyabar.android.pillow.view.list.PillowListFragment;

public class DefaultModelViewConfiguration<T extends IdentificableModel> implements ModelViewConfiguration<T>{
	Class<T> modelClass;
	
	public DefaultModelViewConfiguration(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	public Class<?> getFormClass() {
		return FormActivity.class;
	}

	@Override
	public IModelListAdapter<T> getListAdapter(Context context) {
		return new PillowListAdapter<T>(context, modelClass);
	}

	@Override
	public Fragment getListFragment() {
		return new PillowListFragment<IdentificableModel>();
	}

	@Override
	public Fragment getShowFragment() {
		return new PillowShowFragment<IdentificableModel>();
	}

	
}
