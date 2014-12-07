package com.dualion.power_strip.view;

import android.app.Activity;
import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.DateTime;
import com.dualion.power_strip.model.DateTimePicker;
import com.dualion.power_strip.model.SimpleDateTimePicker;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class DatesActivity extends FragmentActivity implements DateTimePicker.OnDateTimeSetListener{

    private SimpleDateTimePicker simpleDateTimePicker;
    private EditText initDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);

        initDate = (EditText) findViewById(R.id.initDate);

        // Create a SimpleDateTimePicker and Show it
        simpleDateTimePicker = SimpleDateTimePicker.make(
                "Set Date & Time Title",
                new Date(),
                this,
                getSupportFragmentManager()
        );

    }

    public void selectDate(View view) {
        simpleDateTimePicker.show();
    }

    @Override
    public void DateTimeSet(Date date) {

        // This is the DateTime class we created earlier to handle the conversion
        // of Date to String Format of Date String Format to Date object
        DateTime dateTime = new DateTime(date);
        initDate.setText(dateTime.getDateString());

    }

}