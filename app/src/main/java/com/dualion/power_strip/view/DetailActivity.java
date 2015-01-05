package com.dualion.power_strip.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.dualion.power_strip.model.ui.BaseFragmentActivity;

public class DetailActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			TabsPlugsFragment details = new TabsPlugsFragment();
			details.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(
					android.R.id.content, details).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 // Respond to the action bar's Up/Home button
		 case android.R.id.home:
			 NavUtils.navigateUpFromSameTask(this);
			 return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
