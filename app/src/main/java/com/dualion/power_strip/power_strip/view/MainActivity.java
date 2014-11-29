package com.dualion.power_strip.power_strip.view;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.dualion.power_strip.power_strip.R;
import com.dualion.power_strip.power_strip.adapter.CustomGrid;
import com.dualion.power_strip.power_strip.model.Plug;
import com.dualion.power_strip.power_strip.model.PlugsList;
import com.dualion.power_strip.power_strip.restapi.PlugService;
import com.dualion.power_strip.power_strip.restapi.RestPlug;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ListActivity {

    PlugService plugService;
    CustomGrid adapter;

    String url;
    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPref();

        RestPlug restProduct = new RestPlug(url, user, password);
        plugService = restProduct.getService();
        plugService.getAllPlugs(new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {

            adapter = new CustomGrid(MainActivity.this, (ArrayList<Plug>) plugsList.getPlugs());
            setListAdapter(adapter);
        }
            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, final View view, final int position, long arg3) {

                // Set an EditText view to get user input
                final EditText input = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Status")
                        .setMessage("Component")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Editable value = input.getText();
                                Plug plug = adapter.getItem(position);
                                Toast.makeText(MainActivity.this, plug.getPinId() + ": " + value, Toast.LENGTH_SHORT).show();
                                SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                mySettings.edit().putString(plug.getPinId(), value.toString()).apply();
                                adapter.setComponent(position, value.toString());
                                //adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                        Toast.makeText(MainActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
                    }
                }).show();

                return true;
            }

        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        plugService.setPlug(position + 1, new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {
                Toast.makeText(MainActivity.this, "Successful: " + response.getUrl(), Toast.LENGTH_SHORT).show();

                if (plugsList.getPlugs().size() > 0) {
                    Plug plug = plugsList.getPlugs().get(0);
                    int position = Integer.valueOf(plug.getId()) - 1;
                    adapter.setItem(position, plug);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPref() {
        SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "http://127.0.0.1");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefPass", "");
    }

}
