package com.mateuyabar.android.pillow.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.conf.ModelViewConfiguration;
import com.mateuyabar.android.pillow.util.BundleUtils;

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
	
	public void displayShowModel(IdentificableModel model){
		Fragment newFragment = getShowModelFragment(model);
		changeFragment(newFragment);
	}
	
	public Fragment getShowModelFragment(IdentificableModel model){
		Fragment newFragment = getModelViewCongifuration(model).getShowFragment();
		Bundle bunlde = BundleUtils.createIdBundle(model);
		newFragment.setArguments(bunlde);
		return newFragment;
	}
	
	public void displayEditModel(IdentificableModel model){
		Context context = getContext();
		Intent intent = new Intent(context, getModelViewCongifuration(model).getFormClass());
		Bundle bundle = BundleUtils.createIdBundle(model);
//		BundleUtils.setShownAtts(bundle, atts);
		intent.putExtras(bundle);
		context.startActivity(intent);
//		FormFragment<T> fragment = new FormFragment<T>();
//		editModel(model, fragment);
	}
	
	public  void displayListModel(Class<? extends IdentificableModel> modelClass){
		Fragment newFragment = getListFragment(modelClass);
		changeFragment(newFragment);
	}
	
	public <T extends IdentificableModel> void displayListModel(Class<T> modelClass, T filter){
		Fragment newFragment = getListFragment(modelClass);
		//Arguments allready set, but we change it....
		Bundle bundle = BundleUtils.createModelBundle(filter);
		newFragment.setArguments(bundle);
		//its a filtered view, we don't want buttons
		BundleUtils.setHideButtons(bundle);
		changeFragment(newFragment);
	}
	
	public Fragment getListFragment(Class<? extends IdentificableModel> modelClass){
		Fragment newFragment = getModelViewCongifuration(modelClass).getListFragment();
		Bundle bundle = BundleUtils.createIdBundle(modelClass);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	
	private  void editModel(IdentificableModel model, Fragment newFragment){
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

