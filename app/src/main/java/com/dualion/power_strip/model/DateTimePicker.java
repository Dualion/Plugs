package com.dualion.power_strip.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import com.dualion.power_strip.R;

import java.util.Date;

public class DateTimePicker extends DialogFragment {

    public static final String TAG_FRAG_DATE_TIME = "fragDateTime";
    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_DATE = "initDate";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    private Context context;
    private ButtonClickListener buttonClickListener;
    private OnDateTimeSetListener onDateTimeSetListener;
    private Bundle argument;
    private DatePicker datePicker;
    private TimePicker timePicker;

    // DialogFragment constructor must be empty
    public DateTimePicker() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        buttonClickListener = new ButtonClickListener();
    }

    /**
     *
     * @param dialogTitle Title of the DateTimePicker DialogFragment
     * @param initDate Initial Date and Time set to the Date and Time Picker
     * @return Instance of the DateTimePicker DialogFragment
     */
    public static DateTimePicker newInstance(CharSequence dialogTitle, Date initDate) {

        // Create a new instance of DateTimePicker
        DateTimePicker dateTimePicker = new DateTimePicker();

        // Setup the constructor parameters as arguments
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_DIALOG_TITLE, dialogTitle);
        bundle.putSerializable(KEY_INIT_DATE, initDate);
        dateTimePicker.setArguments(bundle);

        // Return instance with arguments
        return dateTimePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve Argument passed to the constructor
        argument = getArguments();

        // Use an AlertDialog Builder to initially create the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Setup the Dialog
        builder.setTitle(argument.getCharSequence(KEY_DIALOG_TITLE));
        builder.setNegativeButton(android.R.string.no, buttonClickListener);
        builder.setPositiveButton(android.R.string.yes, buttonClickListener);

        // Create the Alert Dialog
        AlertDialog alertDialog = builder.create();

        // Set the View to the Dialog
        alertDialog.setView(
                createDateTimeView(alertDialog.getLayoutInflater())
        );

        // Return the Dialog created
        return alertDialog;
    }

    /**
     * Inflates the XML Layout and setups the tabs
     * @param layoutInflater Layout inflater from the Dialog
     * @return Returns a view that will be set to the Dialog
     */
    private View createDateTimeView(LayoutInflater layoutInflater) {

        // Inflate the XML Layout using the inflater from the created Dialog
        View view = layoutInflater.inflate(R.layout.date_time_picker,null);

        // Extract the TabHost
        TabHost tabHost = (TabHost) view.findViewById(R.id.tab_host);
        tabHost.setup();

        // Create Date Tab and add to TabHost
        TabHost.TabSpec dateTab = tabHost.newTabSpec(TAG_DATE);
        dateTab.setIndicator(getString(R.string.tab_date));
        dateTab.setContent(R.id.date_content);
        tabHost.addTab(dateTab);

        // Create Time Tab and add to TabHost
        TabHost.TabSpec timeTab = tabHost.newTabSpec(TAG_TIME);
        timeTab.setIndicator(getString(R.string.tab_time));
        timeTab.setContent(R.id.time_content);
        tabHost.addTab(timeTab);

        // Retrieve Date from Arguments sent to the Dialog
        DateTime dateTime = new DateTime((Date) argument.getSerializable(KEY_INIT_DATE));

        // Initialize Date and Time Pickers
        datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        datePicker.init(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), null);
        timePicker.setCurrentHour(dateTime.getHourOfDay());
        timePicker.setCurrentMinute(dateTime.getMinuteOfHour());
        timePicker.setIs24HourView(true);

        return view;
    }

    /**
     * Sets the OnDateTimeSetListener interface
     * @param onDateTimeSetListener Interface that is used to send the Date and Time
     *               to the calling object
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        this.onDateTimeSetListener = onDateTimeSetListener;
    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int result) {
            // Determine if the user selected Ok
            if(DialogInterface.BUTTON_POSITIVE == result) {
                DateTime dateTime = new DateTime(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute()
                );
                onDateTimeSetListener.DateTimeSet(dateTime.getDate());
            }
        }

    }

    /**
     * Interface for sending the Date and Time to the calling object
     */
    public interface OnDateTimeSetListener {
        public void DateTimeSet(Date date);
    }
}

