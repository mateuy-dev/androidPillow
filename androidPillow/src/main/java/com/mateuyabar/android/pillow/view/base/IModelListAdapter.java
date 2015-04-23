package com.mateuyabar.android.pillow.view.base;

import android.widget.Filterable;
import android.widget.ListAdapter;
import com.mateuyabar.android.pillow.IdentificableModel;

public interface IModelListAdapter<T extends IdentificableModel> extends ListAdapter, Filterable{
	public T getItem(int position);
	public void refreshList();
	public void setFilter(T filter);
}
