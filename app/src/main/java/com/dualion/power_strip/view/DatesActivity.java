package com.dualion.power_strip.view;

import android.content.Intent;
import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.DateTime;
import com.dualion.power_strip.model.DateTimePicker;
import com.dualion.power_strip.model.SimpleDateTimePicker;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class DatesActivity extends FragmentActivity /*implements DateTimePicker.OnDateTimeSetListener*/{

    private SimpleDateTimePicker initDateTimePicker;
    private SimpleDateTimePicker endDateTimePicker;
    private EditText initDate;
    private EditText endDate;
    private TextView titleDate;

    private String pid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra("pid");

        initDate = (EditText) findViewById(R.id.initDate);
        endDate = (EditText) findViewById(R.id.endDate);
        titleDate = (TextView) findViewById(R.id.titleDate);

        titleDate.setText(getString(R.string.plug) + ": " + pid);

        // Create a initDateTimePicker
        initDateTimePicker = SimpleDateTimePicker.make(
                getString(R.string.prompt_initDate),
                new Date(),
                new DateTimePicker.OnDateTimeSetListener() {
                    @Override
                    public void DateTimeSet(Date date) {
                        DateTime dateTime = new DateTime(date);
                        initDate.setText(dateTime.getDateString());
                        initDate.requestFocus();
                    }
                },
                getSupportFragmentManager()
        );

        endDateTimePicker = SimpleDateTimePicker.make(
                getString(R.string.prompt_endDate),
                new Date(),
                new DateTimePicker.OnDateTimeSetListener() {
                    @Override
                    public void DateTimeSet(Date date) {
                        DateTime dateTime = new DateTime(date);
                        endDate.setText(dateTime.getDateString());
                        endDate.requestFocus();
                    }
                },
                getSupportFragmentManager()
        );

    }

    public void selectInitDate(View view) {
        initDateTimePicker.show();
    }

    public void selectEndDate(View view) {
        endDateTimePicker.show();
    }

}