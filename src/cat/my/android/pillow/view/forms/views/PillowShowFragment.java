package cat.my.android.pillow.view.forms.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.R;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.util.BundleUtils;
import cat.my.android.pillow.view.NavigationUtil;
import cat.my.android.pillow.view.forms.TFormView;
import cat.my.util.exceptions.BreakFastException;

public class PillowShowFragment<T extends IdentificableModel> extends Fragment{
	String modelId;
	TFormView<T> formView;
	IDataSource<T> dataSource;
	Class<T> modelClass;
	String[] atts;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		atts = BundleUtils.getShownAtts(bundle);
		modelId = BundleUtils.getId(bundle);
		modelClass = BundleUtils.getModelClass(bundle);
		
		
		formView = new TFormView<T>(getActivity(), false);
		dataSource = Pillow.getInstance(getActivity()).getDataSource(modelClass);
		
		return formView;
	}
	
	public void loadModel(){
		T idModel;
		try {
			idModel = modelClass.newInstance();
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		idModel.setId(modelId);
		dataSource.show(idModel).setViewListeners(new Listener<T>() {
			@Override
			public void onResponse(T model) {
				updateView(model);
			}
		}, CommonListeners.defaultErrorListener);
	}
	
	protected void updateView(T model) {
		formView.setModel(model, atts);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadModel();
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
	
	public Class<T> getModelClass() {
		return modelClass;
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	};
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.show_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		if(item.getItemId() == R.id.menu_action_edit){
			editModel();
			return true;
		}
		if(item.getItemId() == R.id.menu_action_delete){
			deleteModel();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void editModel() {
		new NavigationUtil(this).displayEditModel(getModel());
	}
	
	private void deleteModel() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Delete").setMessage("are you sure...?").setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDataSource().destroy(getModel()).setViewListeners(new Listener<Void>() {
					@Override
					public void onResponse(Void response) {
						new NavigationUtil(PillowShowFragment.this).displayListModel(getModel().getClass());
					}
				}, CommonListeners.defaultErrorListener);
			}
		}).setNegativeButton("cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
}
