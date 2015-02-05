package cat.my.android.pillow.view;


import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.conf.ModelViewConfiguration;
import cat.my.android.pillow.util.BundleUtils;
import cat.my.android.pillow.view.forms.views.FormActivity;
import cat.my.android.pillow.view.forms.views.FormFragment;
import cat.my.android.pillow.view.forms.views.PillowShowFragment;
import cat.my.android.pillow.view.list.PillowListFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class NavigationUtil {
	Fragment fragment;
	public NavigationUtil(Fragment fragment) {
		super();
		this.fragment = fragment;
	}
	
	FragmentActivity activity;
	Integer viewId;
	public NavigationUtil(FragmentActivity activity, int viewId) {
		super();
		this.activity = activity;
		this.viewId = viewId;
	}
	
	public <T extends IdentificableModel> void showModel(T model){
		Fragment newFragment = getModelViewCongifuration(model).getShowFragment();
		showModel(model, newFragment);
	}
	
	public <T extends IdentificableModel> void editModel(T model){
		Context context = getContext();
		Intent intent = new Intent(context, getModelViewCongifuration(model).getFormClass());
		Bundle bundle = BundleUtils.createIdBundle(model);
//		BundleUtils.setShownAtts(bundle, atts);
		intent.putExtras(bundle);
		context.startActivity(intent);
//		
//		FormFragment<T> fragment = new FormFragment<T>();
//		editModel(model, fragment);
	}
	


	public <T extends IdentificableModel> void list(Class<T> modelClass){
		Fragment newFragment = getModelViewCongifuration(modelClass).getListFragment();
		Bundle bundle = BundleUtils.createIdBundle(modelClass);
		newFragment.setArguments(bundle);
		changeFragment(newFragment);
	}
	
	private <T extends IdentificableModel> void showModel(T model, Fragment newFragment){
		Bundle bunlde = BundleUtils.createIdBundle(model);
		newFragment.setArguments(bunlde);
		changeFragment(newFragment);
	}
	
	private <T extends IdentificableModel> void editModel(T model, Fragment newFragment){
		Bundle bunlde = BundleUtils.createIdBundle(model);
		newFragment.setArguments(bunlde);
		changeFragment(newFragment);
	}
	
	public void changeFragment(Fragment newFragment){
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(getFragmentId(), newFragment).addToBackStack(null).commit();
	}

	private FragmentActivity getActivity() {
		if(activity!=null)
			return activity;
		return fragment.getActivity();
	}

	private int getFragmentId() {
		if(viewId !=null)
			return viewId;
		return fragment.getId();
	}
	
	private Context getContext() {
		return getActivity();
	}

	private <T extends IdentificableModel> ModelViewConfiguration<T> getModelViewCongifuration(T model) {
		return (ModelViewConfiguration<T>) getModelViewCongifuration(model.getClass());
	}
	
	private <T extends IdentificableModel> ModelViewConfiguration<T> getModelViewCongifuration(Class<T> modelclazz) {
		return (ModelViewConfiguration<T>)Pillow.getInstance(getContext()).getModelConfiguration(modelclazz).getViewConfiguration();
	}
}

