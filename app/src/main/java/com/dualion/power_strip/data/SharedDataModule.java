package com.dualion.power_strip.data;

import android.app.Application;

import com.dualion.power_strip.view.DatesActivity;
import com.dualion.power_strip.view.LoginActivity;
import com.dualion.power_strip.view.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (   injects = {LoginActivity.class, MainActivity.class, DatesActivity.class},
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
