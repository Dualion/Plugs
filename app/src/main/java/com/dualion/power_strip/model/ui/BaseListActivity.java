package com.dualion.power_strip.model.ui;

import android.app.ListActivity;
import android.os.Bundle;

import com.dualion.power_strip.PowerStripApp;

public abstract class BaseListActivity extends ListActivity implements Base {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((PowerStripApp) getApplication()).getObjectGraph().inject(this);
	}
}