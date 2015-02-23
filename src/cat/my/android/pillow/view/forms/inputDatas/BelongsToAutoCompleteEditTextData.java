package cat.my.android.pillow.view.forms.inputDatas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.view.base.PillowBaseListAdapter;
import cat.my.android.pillow.view.forms.BelongsToInputData;

public class BelongsToAutoCompleteEditTextData<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T> {
	public static final int EMS = 10;
	T selected = null;
	Class<T> parentClass;
    MyAdapter adapter;
	public BelongsToAutoCompleteEditTextData() {
		super();
	}
	
	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}
	
	@Override
	public String getValue() {
		if(selected==null) return null;
        else return selected.getId();
	}

	@Override
	public void setValue(Object value) {
		getView().setText((String) value);
	}

	@Override
	public View createView(Context context) {
		AutoCompleteTextView autoComplete = new AutoCompleteTextView(context);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = (T) adapter.getItem(position);
            }
        });
        adapter = new MyAdapter<T>(context, parentClass);
		autoComplete.setAdapter(adapter);
		autoComplete.setEms(EMS);
		autoComplete.setThreshold(1);

		return autoComplete;
		
	}
	
	private class MyAdapter<T extends IdentificableModel> extends PillowBaseListAdapter<T> implements Filterable{
		List<T> originalModels;
		
		public MyAdapter(Context context, Class<T> clazz) {
			super(context, clazz);
			refreshList();
		}
		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					if (originalModels == null && !models.isEmpty()) {
						originalModels = new ArrayList<T>(models);
					}
					
					FilterResults results = new FilterResults();
					if (originalModels == null || constraint == null || constraint.length() == 0) {
						results.values = new ArrayList();
						results.count = 0;
					} else {
						

						List<T> startWith = new ArrayList<T>();
						List<T> contains = new ArrayList<T>();
						for (T model : originalModels) {
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
					if (originalModels == null && !models.isEmpty()) {
						originalModels = new ArrayList<T>(models);
					}
					models.clear();
					models.addAll((Collection<? extends T>) results.values);
					notifyDataSetChanged();
				}
			};
		}

       public Collection<T> getModels(){
           return models;
       }

        @Override
		public View getView(T model, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = new TextView(getContext());
			}
			TextView textView = (TextView) convertView;
			textView.setText(model.toString());
			return textView;
		}
	}
	 
	
	
	@Override
	public EditText getView() {
		return (EditText) super.getView();
	}
	
}