package com.mateuyabar.android.pillow.view.forms.inputs.display;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.util.exceptions.BreakFastException;

/**
 * TextView that displays a model (a reference)
 */
public class BelongsToTextView<T extends IdentificableModel> extends TextView {

    public BelongsToTextView(Context context) {
        super(context);
        init();
    }

    public BelongsToTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BelongsToTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BelongsToTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){

    }


    /**
     * Model with the id, not filled.
     */
    public void setModel(T model){
        if(model==null || model.getId()==null){
            setText("");
        } else {
            Class<T> modelClass = (Class<T>) model.getClass();
            IDataSource<T> dataSource = Pillow.getInstance(getContext()).getDataSource(modelClass);
            dataSource.show(model).addListeners(new Listeners.ViewListener<T>() {
                    @Override
                    public void onResponse(T response) {
                       setText(response.toString());
                    }
            }, CommonListeners.defaultErrorListener);
        }
    }

    public void setModelId(Class<T> modelClass, String id){
        if(id==null){
            setModel(null);
        } else {

            try {
                T toSearch = modelClass.newInstance();
                toSearch.setId(id);
                setModel(toSearch);
            } catch (Exception e) {
                throw new BreakFastException(e);
            }
        }
    }
}
