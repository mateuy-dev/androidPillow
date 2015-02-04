package cat.my.android.pillow.view.forms.views.rv;
//package cat.my.android.pillow.view.forms.views.rv;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import cat.my.android.pillow.IdentificableModel;
//import cat.my.android.pillow.Pillow;
//import cat.my.android.pillow.view.forms.views.FromDialog;
//import cat.my.android.pillow.data.sync.CommonListeners;
//import cat.my.android.pillow.data.sync.ISynchDataSource;
//
//public class RvFormDialog<T extends IdentificableModel> extends FromDialog<T>{
//	
//	public RvFormDialog(Context context, T model) {
//		super(context, model);
//	}
//	
//	protected void initListeners() {
//		OnClickListener okClickListener = new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				ISynchDataSource<T> dataSource = (ISynchDataSource<T>) Pillow.getInstance(getContext()).getDataSource(getModel().getClass());
//				if(getModel().getId()==null){
//					dataSource.create(getForm().getModel(), CommonListeners.dummyListener, CommonListeners.dummyErrorListener);
//				} else {
//					dataSource.update(getForm().getModel(), CommonListeners.dummyListener, CommonListeners.dummyErrorListener);
//				}
//			}
//		};
//		setOkClickListener(okClickListener);
//	}
//
//}
