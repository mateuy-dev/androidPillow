package com.mateuyabar.android.pillow.conf;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.view.base.IModelListAdapter;

public interface ModelViewConfiguration<T extends IdentificableModel> {
	public Class<?> getFormClass();
	public IModelListAdapter<T> getListAdapter(Context context);
	public Fragment getListFragment();
	public Fragment getShowFragment();
}
