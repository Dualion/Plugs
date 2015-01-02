package com.dualion.power_strip.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.dualion.power_strip.R;
import com.dualion.power_strip.adapter.CustomGrid;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.plug.Plug;
import com.dualion.power_strip.model.plug.PlugsList;
import com.dualion.power_strip.model.ui.BaseListActivity;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.toggleView;

public class MainActivity extends BaseListActivity {

    private CustomGrid adapter;

    @Inject
    SharedData settings;

    private View progressView;
    private ListView mainView;
    private SwipeRefreshLayout swipeRefreshWidget;

    private PlugService plugService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = getListView();
        progressView = findViewById(R.id.main_progress);
        swipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
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
                adapter = new CustomGrid(MainActivity.this, (ArrayList<Plug>) plugsList.getPlugs(), plugService);
                setListAdapter(adapter);
                toggleView(mainView,
                        progressView,
                        getResources().getInteger(android.R.integer.config_shortAnimTime),
                        false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
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
                final EditText input = new EditText(MainActivity.this);
                input.setHint("Component");
                input.setText(adapter.getItem(position).getComponent());
                new AlertDialog.Builder(MainActivity.this)
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
			    // getting values from selected ListItem
			    String pid = String.valueOf(adapter.getItem(position).getId());

			    // Starting new intent
			    Intent in = new Intent(getApplicationContext(), DatesActivity.class);

			    // sending pid to next activity
			    in.putExtra("pid", pid);

			    // starting new activity and expecting some response back
			    startActivityForResult(in, 100);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_stop:
	            stopPlugs();
                return true;
            case R.id.action_logout:
                settings.setCurrentPass("");
                startActivityForResult(new Intent(this, LoginActivity.class), 0);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            case R.id.action_quit:
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void stopPlugs() {
		new AlertDialog.Builder(this)
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
								Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getBaseContext(), "Fail write component name", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
