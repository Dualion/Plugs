package com.dualion.power_strip.view;

import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.ui.BaseFragmentActivity;

public class PlugsActivity extends BaseFragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.plug_list);
	}

}
