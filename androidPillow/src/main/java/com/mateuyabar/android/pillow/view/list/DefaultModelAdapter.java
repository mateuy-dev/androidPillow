package com.mateuyabar.android.pillow.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.view.base.IModelAdapter;

/**
 * Default implementation of IModelAdapter
 *
 * @param <T>
 */
public class DefaultModelAdapter<T> implements IModelAdapter<T>{
    Context context;
    int rowViewId = R.layout.list_row_layout;

    public DefaultModelAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(T model, View convertView, ViewGroup parent) {
        TextView textView, titleView;
        ImageView imageView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(rowViewId, parent, false);
            textView = (TextView) convertView.findViewById(R.id.row_sub_title);
            titleView = (TextView) convertView.findViewById(R.id.row_main_text);
            imageView = (ImageView) convertView.findViewById(R.id.row_icon);
            convertView.setTag(createViewHolder(textView, titleView, imageView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            textView = viewHolder.textView;
            titleView = viewHolder.titleView;
            imageView = viewHolder.imageView;
        }
        updateListView(model, titleView, textView, imageView);

        return convertView;
    }

    public void updateListView(T model, TextView titleView, TextView textView, ImageView imageView){
        titleView.setText(model.toString());
    }

    public static Object createViewHolder(TextView textView, TextView titleView, ImageView imageView) {
        return new ViewHolder(textView, titleView, imageView);
    }

    public Context getContext() {
        return context;
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder {
        public final TextView textView;
        public final TextView titleView;
        public final ImageView imageView;
        public ViewHolder(TextView textView, TextView titleView, ImageView imageView) {
            this.textView = textView;
            this.titleView = titleView;
            this.imageView = imageView;
        }
    }
}
