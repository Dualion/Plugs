package com.dualion.power_strip.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.Calendar.DateTime;
import com.dualion.power_strip.model.Calendar.DateTimePicker;
import com.dualion.power_strip.model.Calendar.SimpleTimePicker;
import com.dualion.power_strip.model.Calendar.Time;
import com.dualion.power_strip.model.Calendar.TimePicker;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.Calendar.SimpleDateTimePicker;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DatesActivity extends FragmentActivity /*implements DateTimePicker.OnDateTimeSetListener*/{

    private SimpleDateTimePicker initDateTimePicker;
    private SimpleDateTimePicker endDateTimePicker;
    private SimpleTimePicker initTimePicker;
    private SimpleTimePicker endTimePicker;
    PlugService plugService;

    private SharedPreferences mySettings;

    private String url;
    private String user;
    private String password;

    private TextView titleDate;
    private Button sendDates;
    private EditText initDate;
    private EditText endDate;

    private Long initMillis;
    private Long endMillis;

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

        initMillis = 0L;
        endMillis = 0L;

        //init DateTimePicker
        initDateTimePicker();

        //init TimePicker
        initTimePicker();

        // init Restapi
        final RestPlug restProduct = new RestPlug(url, user, password);

        sendDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (initMillis >= endMillis) {
                    sendDates.setError(getString(R.string.WrongDate));
                    return;
                } else {
                    sendDates.setError(null);
                }

                plugService = restProduct.getService();
                plugService.SetSchedulerPlugFromId("[Hola:Tonto]", Integer.parseInt(pid), initMillis, endMillis, "undefined", new Callback<PlugsList>() {
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

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        initTimePicker = SimpleTimePicker.make(
                getString(R.string.prompt_initTime),
                new Time(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)+1),
                new TimePicker.OnTimeSetListener() {
                    @Override
                    public void TimeSet(Time timeSet) {
                        Time time = new Time(timeSet);
                        initDate.setText(time.getTimeString());
                        initMillis = time.getTimeMillis();
                        if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
                            sendDates.setEnabled(true);
                        }else{
                            sendDates.setEnabled(false);
                        }
                    }
                },
                getSupportFragmentManager()
        );

        endTimePicker = SimpleTimePicker.make(
                getString(R.string.prompt_endTime),
                new Time(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)+2),
                new TimePicker.OnTimeSetListener() {
                    @Override
                    public void TimeSet(Time timeSet) {
                        Time time = new Time(timeSet);
                        endDate.setText(time.getTimeString());
                        endMillis = time.getTimeMillis();
                        if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
                            sendDates.setEnabled(true);
                        }else{
                            sendDates.setEnabled(false);
                        }
                    }
                },
                getSupportFragmentManager()
        );


    }

    private void initDateTimePicker() {

        // Create a initDateTimePicker
        initDateTimePicker = SimpleDateTimePicker.make(
                getString(R.string.prompt_initDate),
                new Date(System.currentTimeMillis()+60*1000),
                new DateTimePicker.OnDateTimeSetListener() {
                    @Override
                    public void DateTimeSet(Date date) {
                        DateTime dateTime = new DateTime(date);
                        initDate.setText(dateTime.getDateString());
                        initMillis = dateTime.getCalendar().getTimeInMillis();
                        if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
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
                        endMillis = dateTime.getCalendar().getTimeInMillis();
                        if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
                            sendDates.setEnabled(true);
                        }else{
                            sendDates.setEnabled(false);
                        }
                    }
                },
                getSupportFragmentManager()
        );

    }

    public void selectInitDate(View view) {
        //initDateTimePicker.show();
        initTimePicker.show();
    }

    public void selectEndDate(View view) {
        //endDateTimePicker.show();
        endTimePicker.show();
    }

    private void loadPref() {
        mySettings = PreferenceManager.getDefaultSharedPreferences(this);

        url = mySettings.getString("prefUrlApi", "http://127.0.0.1");
        user = mySettings.getString("prefUser", "");
        password = mySettings.getString("prefCurrentPass", "");
    }

}