package com.dualion.power_strip.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dualion.power_strip.R;
import com.dualion.power_strip.adapter.CustomGrid;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.plug.Plug;
import com.dualion.power_strip.model.plug.PlugsList;
import com.dualion.power_strip.model.ui.BaseListFragment;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.toggleView;

public class PlugsFragment extends BaseListFragment {

	boolean dualPane;
	int position = 0;

	@Inject
	SharedData settings;

	private CustomGrid adapter;
	private View progressView;
	private ListView mainView;
	private SwipeRefreshLayout swipeRefreshWidget;
	private PlugService plugService;

	public final static String ARG_POS = "pos";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_main, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POS, position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			position = savedInstanceState.getInt(ARG_POS, 0);
		}

		mainView = getListView();
		mainView.setSelector(R.drawable.list_item_selector);
		progressView = getActivity().findViewById(R.id.main_progress);
		swipeRefreshWidget = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_widget);
		swipeRefreshWidget.setColorSchemeColors(R.color.dualion);

		toggleView(mainView,
				progressView,
				getResources().getInteger(android.R.integer.config_shortAnimTime),
				true);

		RestPlug restProduct = new RestPlug(settings.getURI(), settings.getUser(), settings.getCurrentPass());
		plugService = restProduct.getService();

		plugService.getAllPlugs(new Callback<PlugsList>() {
			@Override
			public void success(PlugsList plugsList, Response response) {
				adapter = new CustomGrid(getActivity(), (ArrayList<Plug>) plugsList.getPlugs(), plugService);
				setListAdapter(adapter);

				initList();

				toggleView(mainView,
						progressView,
						getResources().getInteger(android.R.integer.config_shortAnimTime),
						false);
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Toast.makeText(getActivity(), "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
				toggleView(mainView,
						progressView,
						getResources().getInteger(android.R.integer.config_shortAnimTime),
						false);
				logout();
			}
		});

	}

	private void initList() {

		// Check to see if we have a frame in which to embed the details
		// fragment directly in the containing UI.
		View detailsFrame = getActivity().findViewById(R.id.plug_details);
		dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

		if (dualPane) {
			// In dual-pane mode, the list view highlights the s"position"elected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// Make sure our UI is in the correct state.
			showDetails(position);
		}

		mainView.setLongClickable(true);
		mainView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// Set an EditText view to get user input
				final EditText input = new EditText(getActivity());
				input.setHint("Component");
				input.setText(adapter.getItem(position).getComponent());
				new AlertDialog.Builder(getActivity())
						.setTitle("Info Component")
						.setView(input)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Editable value = input.getText();
								PutComponent(position, value.toString());
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Do nothing.
					}
				}).show();

				return true;
			}
		});

		mainView.setClickable(true);
		mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showDetails(position);
			}
		});

		mainView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
			                     int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (mainView != null && mainView.getChildCount() > 0) {
					// check if the first item of the list is visible
					boolean firstItemVisible = mainView.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = mainView.getChildAt(0).getTop() == 0;
					// enabling or disabling the refresh layout
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeRefreshWidget.setEnabled(enable);
			}
		});

		swipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				}, 1000);
			}
		});

	}

	/**
	 * Helper function to show the details of a selected item, either by
	 * displaying a fragment in-place in the current UI, or starting a
	 * whole new activity in which it is displayed.
	 */
	void showDetails(int index) {
		position = index;
		String pid = String.valueOf(adapter.getItem(index).getId());
		String component = adapter.getItem(index).getComponent();

		if (dualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index, true);

			//TabsPlugsFragment details = (TabsPlugsFragment)
			//		getFragmentManager().findFragmentById(R.id.plug_details);
			//if (details == null || details.getShownIndex() != index) {
				// Make new fragment to show this selection.
				TabsPlugsFragment details = TabsPlugsFragment.newInstance(index, pid, component);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				getFragmentManager().beginTransaction()
						.replace(R.id.plug_details, details)
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			//}

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailActivity.class);
			intent.putExtra(ARG_INDEX, index);
			intent.putExtra(ARG_PID, pid);
			intent.putExtra(ARG_COMP,component);
			startActivity(intent);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.plugs_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		//noinspection SimplifiableIfStatement
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent i = new Intent(getActivity(), SettingsActivity.class);
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				return true;
			case R.id.action_refresh:
				refresh();
				return true;
			case R.id.action_stop:
				stopPlugs();
				return true;
			case R.id.action_logout:
				logout();
				return true;
			case R.id.action_quit:
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void logout() {
		settings.setCurrentPass("");
		startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
		getActivity().finish();
		getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}

	public void stopPlugs() {
		new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.action_stop)
				.setMessage(R.string.are_sure)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						swipeRefreshWidget.setRefreshing(true);
						plugService.SetOffStatePlugs(new Callback<PlugsList>() {
							@Override
							public void success(PlugsList plugsList, Response response) {
								adapter.setPlugs((ArrayList<Plug>) plugsList.getPlugs());
							}

							@Override
							public void failure(RetrofitError retrofitError) {
								Toast.makeText(getActivity(), "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
							}
						});
						swipeRefreshWidget.setRefreshing(false);
					}
				})
				.setNegativeButton(R.string.no, null)
				.show();
	}


	public void refresh() {
		swipeRefreshWidget.setRefreshing(true);
		plugService.getAllPlugs(new Callback<PlugsList>() {
			@Override
			public void success(PlugsList plugsList, Response response) {
				adapter.setPlugs((ArrayList<Plug>) plugsList.getPlugs());
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Toast.makeText(getActivity(), "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
			}
		});
		swipeRefreshWidget.setRefreshing(false);
	}

	public void PutComponent(final int id, final String componentName) {
		plugService.SetComponentPlugFromId(id + 1, componentName, new Callback<PlugsList>() {
			@Override
			public void success(PlugsList plugsList, Response response) {
				adapter.setComponent(id, componentName);
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Toast.makeText(getActivity().getBaseContext(), "Fail write component name", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
