package com.mateuyabar.android.pillow.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.adapters.PillowListAdapter;
import com.mateuyabar.android.pillow.view.presenters.PillowListPresenter;
import com.mateuyabar.android.pillow.views.R;

import java.util.ArrayList;
import java.util.Collection;


public class PillowListFragment<T extends IdentificableModel> extends PillowBaseFragment implements AdapterView.OnItemClickListener, PillowListPresenter.ViewRenderer<T>{
    PillowListPresenter<T> presenter;
    T filter;
    boolean hideButtons;
    Class<T> modelClass;
    PillowListAdapter<T> listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadDataFromBundle();
        presenter = new PillowListPresenter(getContext(), modelClass);
        presenter.initialize(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.listview);
        ImageButton createButton = (ImageButton)rootView.findViewById(R.id.create_model_button);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        if(filter!=null)
            getPresenter().setFilter(filter);

        listAdapter = createAdapter();
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(this);

        if(isHideButtons()){
            createButton.setVisibility(View.GONE);
        } else {
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToCreateModel();
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().synchronizeModels();
            }
        });


        return rootView;
    }

    protected void loadDataFromBundle(){
        filter = BundleUtils.getModel(getArguments());
        hideButtons = BundleUtils.getHideButtons(getArguments());
        modelClass = BundleUtils.getModelClass(getArguments());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        T model = (T) listAdapter.getItem(position);
        displayModelDetails(model);
    }

    protected void displayModelDetails(T model) {
        getNavigation().displayShowModel(model);
    }


    protected void navigateToCreateModel(){
        getNavigation().displayCreateModel(modelClass);
    }

    public boolean isHideButtons() {
        return hideButtons;
    }

    public PillowListAdapter<T> createAdapter(){
        return new PillowListAdapter<>(getContext(), modelClass);
    }

    public T getFilter() {
        return filter;
    }
    @Override
    public PillowListPresenter getPresenter() {
        return presenter;
    }

    public PillowListAdapter<T> getListAdapter() {
        return listAdapter;
    }

    @Override
    public void render(Collection<T> models) {
        getListAdapter().setModels(new ArrayList<T>(models));
    }

    @Override
    public void synchronizationFinished() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
