package com.dualion.power_strip.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.ui.BaseFragmentActivity;

public class PlugsActivity extends BaseFragmentActivity implements PlugsFragment.OnHeadlineSelectedListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.plug_list);

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. If so, we must add the first fragment
		if (findViewById(R.id.plugs) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			PlugsFragment firstFragment = new PlugsFragment();

			// In case this activity was started with special instructions from an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'plugs' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.plugs, firstFragment).commit();
		}
	}

	public void onArticleSelected(String position) {
		// The user selected the headline of an article from the HeadlinesFragment

		// Capture the article fragment from the activity layout
		DetailPlugFragment articleFrag = (DetailPlugFragment)
				getSupportFragmentManager().findFragmentById(R.id.plug_details);

		if (articleFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			articleFrag.updateArticleView(position);

		} else {
			// If the frag is not available, we're in the one-pane layout and must swap frags...

			// Create fragment and give it an argument for the selected article
			DetailPlugFragment newFragment = new DetailPlugFragment();
			Bundle args = new Bundle();
			args.putString(DetailPlugFragment.ARG_PID, position);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.plugs, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}
}
