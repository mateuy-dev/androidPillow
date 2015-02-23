package cat.my.android.pillow.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.R;
import cat.my.android.pillow.view.base.PillowBaseListAdapter;





public class PillowListAdapter<T extends IdentificableModel> extends PillowBaseListAdapter<T> {
	int rowViewId = R.layout.list_row_layout;

	public PillowListAdapter(Context context, Class<T> clazz) {
		super(context, clazz);
	}
	
	
	
	public void updateListView(TextView titleView, TextView textView, T model){
		textView.setText(model.getId());
		titleView.setText(model.toString());
	}
	
	@Override
	public View getView(T model, View convertView, ViewGroup parent) {
		TextView textView, titleView;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(rowViewId, parent, false);
			textView = (TextView) convertView.findViewById(R.id.row_sub_title);
			titleView = (TextView) convertView.findViewById(R.id.row_main_text);
			convertView.setTag(createViewHolder(textView, titleView));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			textView = viewHolder.textView;
			titleView = viewHolder.titleView;
		}
		updateListView(titleView, textView, model);

		return convertView;
	}

	
	protected Object createViewHolder(TextView textView, TextView titleView) {
		return new ViewHolder(textView, titleView);
	}

	/**
	 * ViewHolder
	 */
	protected class ViewHolder {
		public final TextView textView;
		public final TextView titleView;


		public ViewHolder(TextView textView, TextView titleView) {
			this.textView = textView;
			this.titleView = titleView;
		}
	}
	
	
	public void setRowViewId(int rowViewId) {
		this.rowViewId = rowViewId;
	}
}
