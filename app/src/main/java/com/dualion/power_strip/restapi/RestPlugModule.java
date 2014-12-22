package com.dualion.power_strip.restapi;

import android.app.Application;

import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.view.DatesActivity;
import com.dualion.power_strip.view.LoginActivity;
import com.dualion.power_strip.view.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects =
        {
                LoginActivity.class,
                MainActivity.class,
                DatesActivity.class
        },
        library = true,
        complete = false)
public class RestPlugModule {

    String url;
    String username;
    String password;

    public RestPlugModule(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Provides
    @Singleton
    public RestPlug provideRestPlug(){
        return new RestPlug(url, username, password);
    }

}
