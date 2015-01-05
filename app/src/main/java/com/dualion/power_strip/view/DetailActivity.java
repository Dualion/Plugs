package com.dualion.power_strip.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dualion.power_strip.R;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.plug.Plug;
import com.dualion.power_strip.model.plug.PlugsList;
import com.dualion.power_strip.model.ui.BaseFragmentActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends BaseFragmentActivity {

	@Inject
	SharedData settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String nPlug = getIntent().getExtras().getString(ARG_PID);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.plug) + ": " + nPlug);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			TabsPlugsFragment details = new TabsPlugsFragment();
			details.setArguments(getIntent().getExtras());
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.fadein, R.anim.fadeout)
					.add(android.R.id.content, details)
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 // Respond to the action bar's Up/Home button
		 case android.R.id.home:
			 Intent upIntent = NavUtils.getParentActivityIntent(this);
			 if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				 // This activity is NOT part of this app's task, so create a new task
				 // when navigating up, with a synthesized back stack.
				 TaskStackBuilder.create(this)
						 // Add all of this activity's parents to the back stack
						 .addNextIntentWithParentStack(upIntent)
								 // Navigate up to the closest parent
						 .startActivities();
			 } else {
				 // This activity is part of this app's task, so simply
				 // navigate up to the logical parent activity.
				 NavUtils.navigateUpTo(this, upIntent);
			 }
			 overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			 return true;
			case R.id.action_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				return true;
			case R.id.action_logout:
				logout();
				return true;
			case R.id.action_quit:
				finish();
				System.exit(0);
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void logout() {
		settings.setCurrentPass("");
		startActivityForResult(new Intent(this, LoginActivity.class), 0);
		finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}
}
