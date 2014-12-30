package com.dualion.power_strip.model.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dualion.power_strip.PowerStripApp;

public abstract class BaseActivity extends ActionBarActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PowerStripApp) getApplication()).getObjectGraph().inject(this);
    }

}