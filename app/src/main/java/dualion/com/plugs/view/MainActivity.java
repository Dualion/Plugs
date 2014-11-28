package dualion.com.plugs.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import dualion.com.plugs.R;
import dualion.com.plugs.adapter.CustomGrid;
import dualion.com.plugs.model.Plug;
import dualion.com.plugs.model.PlugsList;
import dualion.com.plugs.restapi.PlugService;
import dualion.com.plugs.restapi.RestPlug;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {

    PlugService plugService;
    CustomGrid adapter;
    GridView grid;

    String url;
    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPref();

        RestPlug restProduct = new RestPlug(url);
        plugService = restProduct.getService();
        plugService.getAllPlugs(new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {
                adapter = new CustomGrid(MainActivity.this, (ArrayList<Plug>) plugsList.getPlugs());
                grid=(GridView)findViewById(R.id.grid);
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        plugService.setPlug(position+1, new Callback<PlugsList>() {
                            @Override
                            public void success(PlugsList plugsList, Response response) {
                                Toast.makeText(MainActivity.this,"Successful: "+ response.getUrl(), Toast.LENGTH_SHORT).show();
                                //adapter = new CustomGrid(MainActivity.this, (ArrayList<Plug>) plugsList.getPlugs());
                                //grid=(GridView)findViewById(R.id.grid);
                                //grid.setAdapter(adapter);
                                if (plugsList.getPlugs().size() > 0) {
                                    Plug plug = plugsList.getPlugs().get(0);
                                    int position = Integer.valueOf(plug.getId())-1;
                                    adapter.setItem(position, plug);
                                    grid.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Toast.makeText(MainActivity.this, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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
        switch (id){
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

    private void loadPref(){
        SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefPass", "");

    }

}
