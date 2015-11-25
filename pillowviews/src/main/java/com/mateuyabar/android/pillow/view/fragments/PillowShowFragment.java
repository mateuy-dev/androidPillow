package com.mateuyabar.android.pillow.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mateuyabar.android.cleanbase.Component;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.forms.TFormView;
import com.mateuyabar.android.pillow.view.presenters.PillowShowPresenter;


public class PillowShowFragment<T extends IdentificableModel> extends PillowBaseFragment implements PillowShowPresenter.ViewRenderer<T> {
    String modelId;
    String[] atts;

    TFormView<T> formView;
    IDataSource<T> dataSource;
    Class<T> modelClass;
    PillowShowPresenter<T> presenter;

    @Override
    public Component getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadDataFromBundle();
        presenter = preparePresenter();
        super.onCreate(savedInstanceState);
    }

    protected void loadDataFromBundle() {
        Bundle bundle = getArguments();
        atts = BundleUtils.getShownAtts(bundle);
        modelId = BundleUtils.getId(bundle);
        modelClass = BundleUtils.getModelClass(bundle);
    }

    protected PillowShowPresenter<T> preparePresenter() {
        PillowShowPresenter<T> presenter =  new PillowShowPresenter(getContext(), modelClass);
        presenter.setModelId(modelId);
        presenter.initialize(this);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        formView = new TFormView<T>(getActivity(), false);
        dataSource = Pillow.getInstance(getActivity()).getDataSource(modelClass);
        return formView;
    }

    @Override
    public void render(T model) {
        formView.setModel(model, atts);
    }

    public String getModelId() {
        return modelId;
    }
}
