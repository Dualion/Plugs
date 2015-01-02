package com.dualion.power_strip.view;

import android.os.Bundle;

import com.dualion.power_strip.model.ui.BaseFragmentActivity;

public class DetailPlugActivity extends BaseFragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailPlugFragment details = new DetailPlugFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();
        }

	}

}
