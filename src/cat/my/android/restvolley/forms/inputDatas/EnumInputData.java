package cat.my.android.restvolley.forms.inputDatas;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import cat.my.android.restvolley.forms.InputData;

public class EnumInputData extends AbstractSpinnerInputData<Enum<?>> {
	Class<?> valueClass;

	public EnumInputData(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public ArrayAdapter<Enum<?>> createAdapter(Context context) {
		return new EnumSpinnerAdapter(context);
	}

	private class EnumSpinnerAdapter extends ArrayAdapter<Enum<?>> {
		public EnumSpinnerAdapter(Context context) {
			super(context, android.R.layout.simple_spinner_item);
			for (Object constant : valueClass.getEnumConstants()) {
				add((Enum<?>) constant);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
		}

		private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
			View view;
			TextView text;

			if (convertView == null) {
				view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
			} else {
				view = convertView;
			}

			try {
				text = (TextView) view;
			} catch (ClassCastException e) {
				Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
				throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
			}

			Enum<?> item = getItem(position);
			
			String stringId = valueClass.getSimpleName()+"_"+item.name();
			int id = getContext().getResources().getIdentifier(stringId, "string", getContext().getPackageName());
			String label; 
			if(id!=0)
				label = getContext().getResources().getString(id);
			else{
				label = item.name();
				Log.e("RestVolley", "The following String needs to be added to string.xml: "+stringId);
			}
			text.setText(label);

			return view;
		}

	}

	
}
