package com.dualion.power_strip.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.DateTime;
import com.dualion.power_strip.model.DateTimePicker;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.SimpleDateTimePicker;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DatesActivity extends FragmentActivity /*implements DateTimePicker.OnDateTimeSetListener*/{

    private SimpleDateTimePicker initDateTimePicker;
    private SimpleDateTimePicker endDateTimePicker;



    PlugService plugService;

    private SharedPreferences mySettings;

    private String url;
    private String user;
    private String password;

    private TextView titleDate;
    private Button sendDates;
    private EditText initDate;
    private EditText endDate;

    private Long initDateMilli;
    private Long endDateMilli;

    private String pid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        loadPref();

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra("pid");

        initDate = (EditText) findViewById(R.id.initDate);
        endDate = (EditText) findViewById(R.id.endDate);
        titleDate = (TextView) findViewById(R.id.titleDate);
        sendDates = (Button) findViewById(R.id.sendDates);

        titleDate.setText(getString(R.string.plug) + ": " + pid);
        initDateMilli = 0L;
        endDateMilli = 0L;

        // Create a initDateTimePicker
        initDateTimePicker = SimpleDateTimePicker.make(
                getString(R.string.prompt_initDate),
                new Date(System.currentTimeMillis()+60*1000),
                new DateTimePicker.OnDateTimeSetListener() {
                    @Override
                    public void DateTimeSet(Date date) {
                        DateTime dateTime = new DateTime(date);
                        initDate.setText(dateTime.getDateString());
                        initDateMilli = dateTime.getCalendar().getTimeInMillis();
                        if( initDateMilli != 0L && endDateMilli != 0L && (initDateMilli < endDateMilli)) {
                            sendDates.setEnabled(true);
                        }else{
                            sendDates.setEnabled(false);
                        }
                    }
                },
                getSupportFragmentManager()
        );

        endDateTimePicker = SimpleDateTimePicker.make(
                getString(R.string.prompt_endDate),
                new Date(System.currentTimeMillis()+2*60*1000),
                new DateTimePicker.OnDateTimeSetListener() {
                    @Override
                    public void DateTimeSet(Date date) {
                        DateTime dateTime = new DateTime(date);
                        endDate.setText(dateTime.getDateString());
                        endDateMilli = dateTime.getCalendar().getTimeInMillis();
                        if( initDateMilli != 0L && endDateMilli != 0L && (initDateMilli < endDateMilli)) {
                            sendDates.setEnabled(true);
                        }else{
                            sendDates.setEnabled(false);
                        }
                    }
                },
                getSupportFragmentManager()
        );

        // init Restapi
        final RestPlug restProduct = new RestPlug(url, user, password);

        sendDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (initDateMilli >= endDateMilli) {
                    sendDates.setError(getString(R.string.WrongDate));
                    return;
                } else {
                    sendDates.setError(null);
                }

                plugService = restProduct.getService();
                plugService.SetSchedulerPlugFromId("[Hola:Tonto]", Integer.parseInt(pid), initDateMilli, endDateMilli, "undefined", new Callback<PlugsList>() {
                    @Override
                    public void success(PlugsList plugsList, Response response) {
                        Toast.makeText(DatesActivity.this, "OK", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(DatesActivity.this, "fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void selectInitDate(View view) {
        initDateTimePicker.show();
    }

    public void selectEndDate(View view) {
        endDateTimePicker.show();
    }

    private void loadPref() {
        mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "http://127.0.0.1");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefCurrentPass", "");
    }

}