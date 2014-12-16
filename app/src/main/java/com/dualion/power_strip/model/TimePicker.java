package com.dualion.power_strip.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.dualion.power_strip.R;

import java.util.Date;

public class TimePicker extends DialogFragment {

    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_TIME = "initTime";

    private Context context;
    private ButtonClickListener buttonClickListener;
    private OnDateTimeSetListener onDateTimeSetListener;
    private Bundle argument;
    private DatePicker datePicker;
    private android.widget.TimePicker timePicker;

    // DialogFragment constructor must be empty
    public TimePicker() {
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
     * @param initDate Initial Time set to the Time Picker
     * @return Instance of the TimePicker DialogFragment
     */
    public static TimePicker newInstance(CharSequence dialogTitle, Time initDate) {

        // Create a new instance of DateTimePicker
        TimePicker timePicker = new TimePicker();

        // Setup the constructor parameters as arguments
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_DIALOG_TITLE, dialogTitle);
        bundle.putSerializable(KEY_INIT_TIME, initDate);
        timePicker.setArguments(bundle);

        // Return instance with arguments
        return timePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve Argument passed to the constructor
        argument = getArguments();

        // Use an AlertDialog Builder to initially create the Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Setup the Dialog
        alertDialogBuilder.setTitle(argument.getCharSequence(KEY_DIALOG_TITLE));
        alertDialogBuilder.setNegativeButton(android.R.string.no, buttonClickListener);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, buttonClickListener);

        // Create the Alert Dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Set the View to the Dialog
        alertDialog.setView(
            createTimeView(alertDialog.getLayoutInflater())
        );

        // Return the Dialog created
        return alertDialog;
    }

    /**
     * Inflates the XML Layout and setups the tabs
     * @param layoutInflater Layout inflater from the Dialog
     * @return Returns a view that will be set to the Dialog
     */
    private View createTimeView(LayoutInflater layoutInflater) {

        // Inflate the XML Layout using the inflater from the created Dialog
        View view = layoutInflater.inflate(R.layout.time_picker,null);

        // Retrieve Date from Arguments sent to the Dialog
        //DateTime dateTime = new DateTime( DATE_FORMAT, (Date) argument.getSerializable(KEY_INIT_TIME));

        // Initialize Date and Time Pickers
        datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
        //datePicker.init(dateTime.getYear(), dateTime.getMonthOfYear(),
        //        dateTime.getDayOfMonth(), null);
        //timePicker.setCurrentHour(dateTime.getHourOfDay());
        //timePicker.setCurrentMinute(dateTime.getMinuteOfHour());
        //timePicker.setIs24HourView(true);

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

