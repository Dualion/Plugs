package com.dualion.power_strip.view;

import android.os.Bundle;

import com.dualion.power_strip.model.ui.BaseFragmentActivity;

public class DatesActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			DatesFragment details = new DatesFragment();
			details.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(
					android.R.id.content, details).commit();
		}
	}

}