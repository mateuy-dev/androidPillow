package com.mateuyabar.android.pillow.view.components;

import android.content.Context;

import com.mateuyabar.android.cleanbase.BaseComponent;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.sync.SynchOnConnectionChangeReceiver;

/**
 * Created by mateuyabar on 24/11/15.
 */
public class SynchOnConnectionChangedComponent extends BaseComponent {
    SynchOnConnectionChangeReceiver reciever;

    public SynchOnConnectionChangedComponent(Context context) {
        super(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        reciever = new SynchOnConnectionChangeReceiver();
        reciever.register(getContext(), Pillow.getInstance(getContext()).getSynchManager());
    }



    @Override
    public void onPause() {
        getContext().unregisterReceiver(reciever);
        super.onPause();
    }

}
