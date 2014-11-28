package dualion.com.plugs.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import dualion.com.plugs.R;

public class UserSettingActivity extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }

}
