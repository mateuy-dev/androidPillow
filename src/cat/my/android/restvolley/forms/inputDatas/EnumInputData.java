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

public class EnumInputData implements InputData {
	Class<?> valueClass;

	public EnumInputData(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public Object getValue(View view) {
		return ((Spinner) view).getSelectedItem();
	}

	@Override
	public void setValue(View view, Object value) {
		((Spinner) view).setSelection(((Enum<?>) value).ordinal());
	}

	@Override
	public View createView(Context context) {
		Spinner spinner = new Spinner(context);
		spinner.setAdapter(new EnumSpinnerAdapter(context));
		return spinner;
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
			
			int id = getContext().getResources().getIdentifier(valueClass.getSimpleName()+"_"+item.name(), "string", getContext().getPackageName());

			text.setText(getContext().getResources().getString(id));

			return view;
		}

	}
}
