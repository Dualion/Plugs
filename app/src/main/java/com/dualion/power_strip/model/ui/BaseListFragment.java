package com.dualion.power_strip.model.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.dualion.power_strip.PowerStripApp;

public abstract class BaseListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PowerStripApp) getActivity().getApplication()).getObjectGraph().inject(this);
    }
}