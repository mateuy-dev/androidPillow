package cat.my.android.pillow.view.base;

import android.widget.Filterable;
import android.widget.ListAdapter;
import cat.my.android.pillow.IdentificableModel;

public interface IModelListAdapter<T extends IdentificableModel> extends ListAdapter, Filterable{
	public T getItem(int position);
	public void refreshList();
	public void setFilter(T filter);
}
