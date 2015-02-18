package cat.my.android.pillow.view.forms.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.util.BundleUtils;
import cat.my.android.pillow.view.forms.TFormView;
import cat.my.util.exceptions.BreakFastException;

public class FormFragment<T extends IdentificableModel>  extends Fragment{
	public static final String EDIT_MODE_PARAM = "editMode";

	String modelId;
	TFormView<T> formView;
	IDataSource<T> dataSource;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		
		
		Bundle bundle = getArguments();
		final String[] atts = BundleUtils.getShownAtts(bundle);
		modelId = BundleUtils.getId(bundle);
		Class<T> clazz = BundleUtils.getModelClass(bundle);
		boolean editMode = bundle.getBoolean(EDIT_MODE_PARAM, true);
		
		formView = new TFormView<T>(getActivity(), editMode);
		
		
		dataSource = Pillow.getInstance(getActivity()).getDataSource(clazz);
		
		try {
			T idModel = null;
			idModel = clazz.newInstance();
			if (modelId != null) {
				//update, we should look for current values
				idModel.setId(modelId);
				dataSource.show(idModel, new Listener<T>() {
					@Override
					public void onResponse(T model) {
						formView.setModel(model, atts);
					}
				}, CommonListeners.defaultErrorListener);
			} else {
				//we check if there is a model in the bundle, other wise we use the empty model
				T bundleModel = BundleUtils.getModel(bundle);
				if(bundleModel!=null){
					idModel = bundleModel;
				}
				formView.setModel(idModel, atts);
			}
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		
		return formView;
	}
	
	public T getModel(){
		return formView.getModel();
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public IDataSource<T> getDataSource() {
		return dataSource;
	}

}
