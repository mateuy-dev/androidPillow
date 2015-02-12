package cat.my.android.pillow.view.base;

import cat.my.android.pillow.IdentificableModel;
import android.widget.ListAdapter;

public interface IModelListAdapter<T extends IdentificableModel> extends ListAdapter{
	public T getItem(int position);
	public void refreshList();
	public void setFilter(T filter);
}
