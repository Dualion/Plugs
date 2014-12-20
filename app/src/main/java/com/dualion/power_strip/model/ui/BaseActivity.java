package com.dualion.power_strip.model.ui;


import android.app.Activity;
import android.os.Bundle;

import com.dualion.power_strip.PowerStripApp;

public abstract class BaseActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PowerStripApp) getApplication()).getObjectGraph().inject(this);
    }
}