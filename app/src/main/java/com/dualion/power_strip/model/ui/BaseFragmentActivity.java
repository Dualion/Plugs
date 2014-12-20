package com.dualion.power_strip.model.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dualion.power_strip.PowerStripApp;

public abstract class BaseFragmentActivity extends FragmentActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PowerStripApp) getApplication()).getObjectGraph().inject(this);
    }
}