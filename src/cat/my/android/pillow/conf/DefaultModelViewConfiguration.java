package cat.my.android.pillow.conf;

import android.content.Context;
import android.support.v4.app.Fragment;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.view.base.IModelListAdapter;
import cat.my.android.pillow.view.forms.views.FormActivity;
import cat.my.android.pillow.view.forms.views.PillowShowFragment;
import cat.my.android.pillow.view.list.PillowListAdapter;
import cat.my.android.pillow.view.list.PillowListFragment;

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
