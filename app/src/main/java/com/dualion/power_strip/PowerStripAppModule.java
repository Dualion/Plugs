package com.dualion.power_strip;


import android.app.Application;

import com.dualion.power_strip.Settings.PreferenceModule;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                PowerStripApp.class
        },
        includes = {
                PreferenceModule.class
        }
)
public class PowerStripAppModule {

    private final PowerStripApp powerStripApp;

    public PowerStripAppModule(PowerStripApp powerStripApp) {
        this.powerStripApp = powerStripApp;
    }

    @Provides
    public Application provideApplication() {
        return powerStripApp;
    }
}
