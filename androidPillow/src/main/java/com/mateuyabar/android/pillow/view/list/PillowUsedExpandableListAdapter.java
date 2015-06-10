package com.mateuyabar.android.pillow.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowListResult;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PillowUsedExpandableListAdapter<T extends IdentificableModel> extends BaseExpandableListAdapter implements Filterable {
	public static final int RECENT_MODELS_GROUP = 0;
	public static final int ALL_MODELS_GROUP = 1;

	Context context;
	Class<T> modelClass;
	RecentlyUsedModelsController recentlyUsedModelsController;

	int rowViewId = R.layout.list_row_layout;


	List<T> allModels = new ArrayList<>();
	List<T> recentModels = new ArrayList<>();

	List<T> originalAllModels;
	List<T> originalRecentModels;

	protected LayoutInflater inflater;
	IDataSource<T> dataSource;

	public PillowUsedExpandableListAdapter(Context context, Class<T> modelClass) {
		this.context = context;
		this.modelClass = modelClass;
		this.dataSource = (IDataSource<T>) Pillow.getInstance(context).getDataSource(modelClass);
		this.inflater = LayoutInflater.from(context);
		this.recentlyUsedModelsController = new RecentlyUsedModelsController(context, modelClass);
		loadItems();
	}

	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getList(groupPosition).size();
	}
	
	private List<T> getList(int groupPosition) {
		switch (groupPosition) {
		case RECENT_MODELS_GROUP:
			return recentModels;
		case ALL_MODELS_GROUP:
			return allModels;
		default:
			throw new UnimplementedException();
		}
	}

	@Override
	public String getGroup(int groupPosition) {
		switch (groupPosition) {
		case RECENT_MODELS_GROUP:
			return context.getString(R.string.recently_used_items);
		case ALL_MODELS_GROUP:
			return context.getString(R.string.all_items);
		default:
			throw new UnimplementedException();
		}
	}

	@Override
	public T getChild(int groupPosition, int childPosition) {
		return getList(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_list_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.title);
        lblListHeader.setText(headerTitle);
 
        return convertView;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		T model = getChild(groupPosition, childPosition);

		TextView textView, titleView;
		ImageView imageView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(rowViewId, parent, false);
			textView = (TextView) convertView.findViewById(R.id.row_sub_title);
			titleView = (TextView) convertView.findViewById(R.id.row_main_text);
			imageView = (ImageView) convertView.findViewById(R.id.row_icon);
			convertView.setTag(PillowListAdapter.createViewHolder(textView, titleView, imageView));
		} else {
			PillowListAdapter.ViewHolder viewHolder = (PillowListAdapter.ViewHolder) convertView.getTag();
			textView = viewHolder.textView;
			titleView = viewHolder.titleView;
			imageView = viewHolder.imageView;
		}
		updateListView(model, titleView, textView, imageView);

		return convertView;
	}

	public void updateListView(T model, TextView titleView, TextView textView, ImageView imageView) {
		titleView.setText(model.toString());
	}



	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void loadItems(){
		ViewListener<Collection<T>> listener = new ViewListener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> models) {
				allModels = new ArrayList<T>(models);
				notifyDataSetChanged();
			}
		};
		dataSource.index().addListeners(listener, CommonListeners.defaultErrorListener);


		List<String> usedIds = recentlyUsedModelsController.getRecentlyUsedIds();
		List<IPillowResult<T>> pillowResults = new ArrayList<>();
		for(String usedId: usedIds){
			IPillowResult<T> result = dataSource.show(ReflectionUtil.createIdModel(modelClass, usedId));
			pillowResults.add(result);
		}
		IPillowResult<List<T>> recentModelsResult = new PillowListResult<>(pillowResults);
		recentModelsResult.addListeners(new ViewListener<List<T>>() {
			@Override
			public void onResponse(List<T> response) {
				recentModels = response;
				notifyDataSetChanged();
			}
		}, CommonListeners.defaultErrorListener);
	}

	Filter filter = new BasicFilter();
	@Override
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Filters the results using the tostring method.
	 *
	 * In case that called before recentModels loaded, it may behave not correclty. TODO check this.
	 */
	private class BasicFilter extends Filter{
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (originalAllModels == null && !allModels.isEmpty()) {
				originalAllModels = new ArrayList<>(allModels);
				originalRecentModels = new ArrayList<>(recentModels);
			}

			FilterResults results = new FilterResults();
			if (originalAllModels == null) {
				results.values = new ArrayList();
				results.count = 0;
			} else if  (constraint == null || constraint.length() == 0) {
				results.values = originalAllModels;
				results.count = originalAllModels.size();
			} else {
				List<T> startWith = new ArrayList<T>();
				List<T> contains = new ArrayList<T>();
				for (T model : originalAllModels) {
					String modelString = model.toString().toLowerCase();
					String constrainString = constraint.toString().toLowerCase();
					if (modelString.startsWith(constrainString)) {
						startWith.add(model);
					} else  if(modelString.contains(constrainString)){
						contains.add(model);
					}
				}
				List<T> result = new ArrayList<T>(startWith);
				result.addAll(contains);
				results.values = result;
				results.count = result.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (originalAllModels == null && !allModels.isEmpty()) {
				originalAllModels = new ArrayList<>(allModels);
				originalRecentModels = new ArrayList<>(recentModels);
			}
			allModels.clear();
			allModels.addAll((Collection<? extends T>) results.values);
			recentModels.clear();
			for(T model : originalRecentModels){
				if(((Collection<? extends T>) results.values).contains(model)){
					recentModels.add(model);
				}
			}
			notifyDataSetChanged();
		}
	}
}
