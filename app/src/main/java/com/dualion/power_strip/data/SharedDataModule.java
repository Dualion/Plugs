package com.dualion.power_strip.data;

import android.app.Application;

import com.dualion.power_strip.view.DatesActivity;
import com.dualion.power_strip.view.DetailPlugFragment;
import com.dualion.power_strip.view.LoginActivity;
import com.dualion.power_strip.view.MainActivity;
import com.dualion.power_strip.view.PlugsActivity;
import com.dualion.power_strip.view.PlugsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (   injects = {LoginActivity.class, MainActivity.class, DatesActivity.class, PlugsFragment.class, DetailPlugFragment.class, PlugsActivity.class},
            library = true,
            complete = false
        )

public class SharedDataModule {

	private final Application app;

	public SharedDataModule(Application app) {
		this.app = app;
	}

    @Provides
    @Singleton
    public SharedData provideSharedData(){
        return new SharedData(app);
    }

}
