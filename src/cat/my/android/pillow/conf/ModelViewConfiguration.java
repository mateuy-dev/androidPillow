package cat.my.android.pillow.conf;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ListAdapter;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.view.base.IModelListAdapter;

public interface ModelViewConfiguration<T extends IdentificableModel> {
	public Class<?> getFormClass();
	public IModelListAdapter<T> getListAdapter(Context context);
	public Fragment getListFragment();
	public Fragment getShowFragment();
}
