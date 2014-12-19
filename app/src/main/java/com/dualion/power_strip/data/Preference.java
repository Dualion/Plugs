package com.dualion.power_strip.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Preference {

    private static final String URL_API = "prefUrlApi";
    private static final String USER_API = "prefUser";
    private static final String PASS_API = "prefPass";
    private static final String CURRENT_PASS_API = "prefCurrentPass";

    private Application app;
    private SharedPreferences mySettings;

    public Preference(Application app) {
        this.app = app;
        mySettings = PreferenceManager.getDefaultSharedPreferences(this.app);
    }

    public void registerAppEnter() {
        Toast.makeText(app, "App enter", Toast.LENGTH_LONG).show();
    }

    public String getURI(){
        return mySettings.getString(URL_API, "");
    }

    public String getUser(){
        return mySettings.getString(USER_API, "");
    }

    public String getPass(){
        return mySettings.getString(PASS_API, "");
    }

    public String getCurrentPass(){
        return mySettings.getString(CURRENT_PASS_API, "");
    }

    public void setURI(String urlApi){
        mySettings.edit().putString(URL_API, urlApi).apply();
    }

    public void setUser(String user){
        mySettings.edit().putString(USER_API, user).apply();
    }

    public void setPass(String pass){
        mySettings.edit().putString(PASS_API, pass).apply();
    }

    public void setCurrentPass(String currentPass){
        mySettings.edit().putString(CURRENT_PASS_API, currentPass).apply();
    }

}
