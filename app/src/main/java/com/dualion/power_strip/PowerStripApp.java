package com.dualion.power_strip;

import android.app.Application;

import com.dualion.power_strip.Settings.Preference;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class PowerStripApp extends Application {

    private ObjectGraph objectGraph;

    @Inject com.dualion.power_strip.Settings.Preference settings;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);

        //settings.registerAppEnter();

    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new PowerStripAppModule(this));
    }

}
