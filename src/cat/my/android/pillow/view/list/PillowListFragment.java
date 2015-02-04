package cat.my.android.pillow.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.R;
import cat.my.android.pillow.view.forms.views.FormActivity;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.data.sync.ISynchDataSource;
import cat.my.android.pillow.util.BundleUtils;
import cat.my.util.exceptions.BreakFastException;


public class PillowListFragment<T extends IdentificableModel> extends Fragment {
	Class<T> clazz;
	PillowListAdapter<T> listAdapter;
	IDataSource<T> ops;
	
	Listener<T> refreshListListener = new Listener<T>(){
		@Override
		public void onResponse(T post) {
			listAdapter.refreshList();
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_fragment, container, false);
		
		clazz = BundleUtils.getModelClass(getArguments());
		
		ops = Pillow.getInstance(getActivity()).getDataSource(clazz);
		

		ListView listview = (ListView) rootView.findViewById(R.id.listview);
		listAdapter = new PillowListAdapter<T>(getActivity(),clazz);
		listview.setAdapter(listAdapter);
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		if(item.getItemId() == R.id.menu_action_new){
			createPost();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		listAdapter.refreshList();
	}
	
	private void sendData() {
		if(ops instanceof ISynchDataSource<?>)
			((ISynchDataSource<T>)ops).sendDirty(CommonListeners.dummyListener, CommonListeners.dummyErrorListener);
	}
	
	private void createPost(){
		try {
			Intent intent = new Intent(getActivity(), FormActivity.class);
			T model = clazz.newInstance();
			Bundle bundle = BundleUtils.createIdBundle(model);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			new BreakFastException(e);
		}
		
	}
	
	

}
