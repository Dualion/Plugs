package com.dualion.power_strip.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.view.LayoutInflater;
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
import com.dualion.power_strip.model.ui.BaseFragment;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.toggleView;

public class PlugsFragment extends BaseFragment {

	@Inject
	SharedData settings;

	private CustomGrid adapter;
	private View progressView;
	private ListView mainView;
	private SwipeRefreshLayout swipeRefreshWidget;
	private PlugService plugService;
	OnHeadlineSelectedListener mCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_main, container, false);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mainView = (ListView) getActivity().findViewById(android.R.id.list);
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
				mainView.setAdapter(adapter);
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
			}
		});

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

				String pid = String.valueOf(adapter.getItem(position).getId());

				// Notify the parent activity of selected item
				mCallback.onArticleSelected(pid);

				// Set the item as checked to be highlighted when in two-pane layout
				mainView.setItemChecked(position, true);

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
				if(mainView != null && mainView.getChildCount() > 0){
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
				}, 1500);
			}
		});

	}

	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(String position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected list item
		// (We do this during onStart because at the point the listview is available.)
		if (getFragmentManager().findFragmentById(R.id.plug_details) != null) {
			mainView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	private void stopPlugs() {
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


	private void refresh() {
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

	private void PutComponent(final int id, final String componentName) {
		plugService.SetComponentPlugFromId(id+1, componentName, new Callback<PlugsList>() {
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
