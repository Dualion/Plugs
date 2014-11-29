package com.dualion.power_strip.power_strip.view;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.dualion.power_strip.power_strip.R;

public class SettingsActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();

    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

        }
    }

}


