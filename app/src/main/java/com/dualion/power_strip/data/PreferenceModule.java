package com.dualion.power_strip.data;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(

        complete = false,
        library = true
)
public class PreferenceModule {

    @Provides
    @Singleton
    public Preference providePreference(Application app){
        return new Preference(app);
    }

}
