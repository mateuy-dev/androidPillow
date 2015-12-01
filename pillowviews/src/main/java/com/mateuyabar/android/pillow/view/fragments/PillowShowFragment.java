package com.mateuyabar.android.pillow.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.NavigationUtil;
import com.mateuyabar.android.pillow.view.forms.TFormView;
import com.mateuyabar.android.pillow.view.presenters.PillowShowPresenter;
import com.mateuyabar.android.pillow.views.R;


public class PillowShowFragment<T extends IdentificableModel> extends PillowBaseFragment implements PillowShowPresenter.ViewRenderer<T> {
    String modelId;
    String[] atts;

    TFormView<T> formView;
    IDataSource<T> dataSource;
    Class<T> modelClass;
    PillowShowPresenter<T> presenter;

    @Override
    public PillowShowPresenter<T> getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadDataFromBundle();
        presenter = preparePresenter();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        new NavigationUtil(this).displayEditModel(getPresenter().getModel());
    }

    private void deleteModel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_warn_title).setMessage(R.string.delete_warn_text).setPositiveButton(R.string.delete_warn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().deleteModel();
            }
        }).setNegativeButton(R.string.delete_warn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public void onModelDeleted() {
        getFragmentManager().popBackStack();
    }
}
