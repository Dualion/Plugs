package com.dualion.power_strip.data;

import android.app.Application;

import com.dualion.power_strip.view.DatesFragment;
import com.dualion.power_strip.view.DetailActivity;
import com.dualion.power_strip.view.LoginActivity;
import com.dualion.power_strip.view.PlugsActivity;
import com.dualion.power_strip.view.PlugsFragment;
import com.dualion.power_strip.view.TabsPlugsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {LoginActivity.class, PlugsFragment.class, DatesFragment.class, PlugsActivity.class, DetailActivity.class, TabsPlugsFragment.class},
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
	public SharedData provideSharedData() {
		return new SharedData(app);
	}

}
