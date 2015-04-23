package com.mateuyabar.android.pillow.conf;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.mateuyabar.android.pillow.IdentificableModel;
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
