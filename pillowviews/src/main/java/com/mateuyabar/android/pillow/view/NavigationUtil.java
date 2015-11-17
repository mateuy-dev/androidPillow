/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.conf.ModelViewConfiguration;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.forms.views.FormActivity;
import com.mateuyabar.util.exceptions.BreakFastException;

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

	public <T extends IdentificableModel> void displayCreateModel(Class<T> clazz){
		try {
			Intent intent = new Intent(getActivity(), FormActivity.class);
			T model = clazz.newInstance();
			Bundle bundle = BundleUtils.createIdBundle(model);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
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

		changeFragment(getListFragment(modelClass, filter));
	}

	public <T extends IdentificableModel> Fragment getListFragment(Class<T> modelClass, T filter) {
		Fragment newFragment = getListFragment(modelClass);
		//Arguments allready set, but we change it....
		Bundle bundle = BundleUtils.createModelBundle(filter);
		newFragment.setArguments(bundle);
		//its a filtered view, we don't want buttons
		BundleUtils.setHideButtons(bundle);
		return newFragment;
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
	
	protected Context getContext() {
		return getActivity();
	}

	private <T extends IdentificableModel> ModelViewConfiguration<T> getModelViewCongifuration(T model) {
		return (ModelViewConfiguration<T>) getModelViewCongifuration(model.getClass());
	}
	
	private <T extends IdentificableModel> ModelViewConfiguration<T> getModelViewCongifuration(Class<T> modelclazz) {
		return (ModelViewConfiguration<T>) PillowView.getInstance(getContext()).getViewConfiguration(modelclazz);
	}
}

