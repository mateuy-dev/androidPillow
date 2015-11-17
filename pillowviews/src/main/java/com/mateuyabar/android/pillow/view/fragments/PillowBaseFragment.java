package com.mateuyabar.android.pillow.view.fragments;

import com.mateuyabar.android.cleanbase.PresenterFragment;
import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.view.NavigationUtil;


public abstract class PillowBaseFragment extends PresenterFragment {
    public PillowView getPillowView(){
        return PillowView.getInstance(getContext());
    }

    public NavigationUtil getNavigation(){
        return getPillowView().getNavigation(this);
    }
}
