package com.dualion.power_strip;


import android.app.Application;

import com.dualion.power_strip.data.SharedDataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
		includes = {
                SharedDataModule.class
        },
        injects = {
                PowerStripApp.class
        }
)
public class PowerStripAppModule {

    private final PowerStripApp powerStripApp;

    public PowerStripAppModule(PowerStripApp powerStripApp) {
        this.powerStripApp = powerStripApp;
    }

    @Provides
	@Singleton
    public Application provideApplication() {
        return powerStripApp;
    }
}
