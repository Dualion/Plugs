package com.dualion.power_strip;

import android.app.Application;

import com.dualion.power_strip.data.SharedDataModule;
import com.dualion.power_strip.restapi.RestPlugModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

public class PowerStripApp extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(getModules().toArray());

    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new SharedDataModule(this), new RestPlugModule(this));
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
