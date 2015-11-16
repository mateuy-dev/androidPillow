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

package com.mateuyabar.android.pillow.view.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.NavigationUtil;
import com.mateuyabar.android.pillow.view.base.IModelListAdapter;


public class PillowListFragment<T extends IdentificableModel> extends Fragment implements OnItemClickListener{
	T filter;
	boolean hideButtons;
	Class<T> clazz;
	IModelListAdapter<T> listAdapter;
	Pillow pillow;
	
	Listener<T> refreshListListener = new Listener<T>(){
		@Override
		public void onResponse(T post) {
			listAdapter.refreshList();
		}
	};
	
	public IModelListAdapter<T> getListAdapter() {
		return listAdapter;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		loadDataFromBundle();

		View rootView = inflater.inflate(R.layout.list_fragment, container, false);
		
		
		pillow = Pillow.getInstance(getActivity());
		

		ListView listview = (ListView) rootView.findViewById(R.id.listview);
		ImageButton createButton = (ImageButton)rootView.findViewById(R.id.create_model_button);
		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

		listAdapter = createAdapter();
		if(filter!=null)
			listAdapter.setFilter(filter);
		listview.setAdapter(listAdapter);
		
		listview.setOnItemClickListener(this);

		if(isHideButtons()){
			createButton.setVisibility(View.GONE);
		} else {
			createButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createModel();
				}
			});
		}

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Pillow.getInstance(getActivity()).getSynchManager().download(true).addListeners(new Listeners.ViewListener<Void>() {
					@Override
					public void onResponse(Void response) {
						listAdapter.refreshList();
						swipeRefreshLayout.setRefreshing(false);
					}
				}, new Listeners.ErrorListener() {
					@Override
					public void onErrorResponse(PillowError error) {
						swipeRefreshLayout.setRefreshing(false);
						new CommonListeners.ErrorListenerWithNoConnectionToast(getActivity()).onErrorResponse(error);
					}
				});
			}
		});
		
		
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		T model = listAdapter.getItem(position);
		displayModelDetails(model);
	}

	protected void displayModelDetails(T model) {
		new NavigationUtil(PillowListFragment.this).displayShowModel(model);
	}

	protected void loadDataFromBundle(){
		filter = BundleUtils.getModel(getArguments());
		hideButtons = BundleUtils.getHideButtons(getArguments());
		clazz = BundleUtils.getModelClass(getArguments());
	}



	public IModelListAdapter<T> createAdapter(){
		return pillow.getViewConfiguration(clazz).getListAdapter(getActivity());
	}
	
	public T getFilter() {
		return filter;
	}
	
//	////We are not using menu anymore but floating button
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
//	};
//	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		inflater.inflate(R.menu.list_menu, menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle presses on the action bar items
//		if(item.getItemId() == R.id.menu_action_new){
//			createPost();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onResume() {
		super.onResume();
		listAdapter.refreshList();
	}
	
	protected void createModel(){
		new NavigationUtil(this).displayCreateModel(clazz);
		
	}

	public boolean isHideButtons() {
		return hideButtons;
	}
}
