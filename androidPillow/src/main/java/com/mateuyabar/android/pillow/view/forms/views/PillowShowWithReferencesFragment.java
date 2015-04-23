package com.mateuyabar.android.pillow.view.forms.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.util.exceptions.BreakFastException;

public class PillowShowWithReferencesFragment<T extends IdentificableModel> extends PillowShowFragment<T>{
	public static final String REFERENCED_CLASS_ATTRIBUTE="referencedClass"; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Class<? extends IdentificableModel> referencedClass = (Class<? extends IdentificableModel>) getArguments().getSerializable(REFERENCED_CLASS_ATTRIBUTE);
		
		LinearLayout linearlayout = new LinearLayout(getActivity());
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		View view = super.onCreateView(inflater, container, savedInstanceState);
		linearlayout.addView(view);
		
		//Create a fake space to put the fragment
		FrameLayout frame = new FrameLayout(getActivity());
		int id = com.mateuyabar.android.pillow.R.id.reserver_id_for_list_fragment;
		frame.setId(id);
		linearlayout.addView(frame);
		
		Fragment listFragment = Pillow.getInstance(getActivity()).getViewConfiguration(referencedClass).getListFragment();
		
		IdentificableModel filter;
		try {
			filter = referencedClass.newInstance();
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		ReflectionUtil.setReferenceId(filter, getModelClass(), getModelId());
		
		
		Bundle bundle = BundleUtils.createModelBundle(filter);
		BundleUtils.setHideButtons(bundle);
		listFragment.setArguments(bundle);
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frame.getId(), listFragment).commit();
		return linearlayout;
	}

}
