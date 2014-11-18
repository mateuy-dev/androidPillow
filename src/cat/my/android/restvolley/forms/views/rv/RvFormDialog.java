package cat.my.android.restvolley.forms.views.rv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.forms.views.FromDialog;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;

public class RvFormDialog<T extends IdentificableModel> extends FromDialog<T>{
	
	public RvFormDialog(Context context, T model) {
		super(context, model);
	}
	
	protected void initListeners() {
		OnClickListener okClickListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ISynchDataSource<T> dataSource = (ISynchDataSource<T>) RestVolley.getInstance(getContext()).getDataSource(getModel().getClass());
				if(getModel().getId()==null){
					dataSource.create(getForm().getModel(), CommonListeners.dummyListener, CommonListeners.dummyErrorListener);
				} else {
					dataSource.update(getForm().getModel(), CommonListeners.dummyListener, CommonListeners.dummyErrorListener);
				}
			}
		};
		setOkClickListener(okClickListener);
	}

}
