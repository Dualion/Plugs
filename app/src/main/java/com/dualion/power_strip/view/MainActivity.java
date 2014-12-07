package com.dualion.power_strip.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.dualion.power_strip.R;
import com.dualion.power_strip.adapter.CustomGrid;
import com.dualion.power_strip.model.Plug;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ListActivity {

    private CustomGrid adapter;
    private SharedPreferences mySettings;

    private String url;
    private String user;
    private String password;
    private View progressView;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPref();


        mainView = getListView();
        progressView = findViewById(R.id.main_progress);

        showProgress(true);

        RestPlug restProduct = new RestPlug(url, user, password);
        final PlugService plugService = restProduct.getService();
        plugService.getAllPlugs(new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {
                adapter = new CustomGrid(MainActivity.this, (ArrayList<Plug>) plugsList.getPlugs(), plugService);
                setListAdapter(adapter);
                showProgress(false);
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

    }

    @Override
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
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            case R.id.action_refresh:
                startActivity(getIntent());
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.action_logout:
                mySettings.edit().putString("prefCurrentPass", "").apply();
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

    private void loadPref() {
        mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "http://127.0.0.1");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefCurrentPass", "");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
