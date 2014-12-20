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
import com.dualion.power_strip.model.Plug;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.ui.BaseListActivity;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.showProgress;

public class MainActivity extends BaseListActivity {

    private CustomGrid adapter;

    @Inject
    SharedData settings;

    private View progressView;
    private ListView mainView;
    private SwipeRefreshLayout swipeRefreshWidget;

    PlugService plugService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = getListView();
        progressView = findViewById(R.id.main_progress);
        swipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        swipeRefreshWidget.setColorSchemeColors(R.color.dualion);

        showProgress(mainView,
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
                showProgress(mainView,
                        progressView,
                        getResources().getInteger(android.R.integer.config_shortAnimTime),
                        false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
                showProgress(mainView,
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
                swipeRefreshWidget.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                        swipeRefreshWidget.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        // getting values from selected ListItem
        String pid = String.valueOf(adapter.getItem(position).getId());

        // Starting new intent
        Intent in = new Intent(getApplicationContext(), DatesActivity.class);

        // sending pid to next activity
        in.putExtra("pid", pid);

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);
    }

    /*@Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Set an EditText view to get user input
        final EditText input = new EditText(MainActivity.this);
        input.setHint("Component");
        input.setText(adapter.getItem(position).getComponent());
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Info Component")
                //.setMessage("Component")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        Plug plug = adapter.getItem(position);
                        Toast.makeText(MainActivity.this, plug.getPinId() + ": " + value, Toast.LENGTH_SHORT).show();
                        mySettings.edit().putString(plug.getPinId(), value.toString()).apply();
                        adapter.setComponent(position, value.toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
                Toast.makeText(MainActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
            }
        }).show();

    }*/

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            case R.id.action_refresh:
                swipeRefreshWidget.setRefreshing(true);
                refresh();
                break;
            case R.id.action_logout:
                //mySettings.edit().putString("prefCurrentPass", "").apply();
                settings.setCurrentPass("");
                startActivityForResult(new Intent(this, LoginActivity.class), 0);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.action_quit:
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private void refresh() {
        plugService.getAllPlugs(new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {
                adapter.setPlugs((ArrayList<Plug>) plugsList.getPlugs());
                swipeRefreshWidget.setRefreshing(false);
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
                swipeRefreshWidget.setRefreshing(false);
            }
        });
    }

    /*private void loadPref() {
        mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "http://127.0.0.1");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefCurrentPass", "");
    }*/

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
