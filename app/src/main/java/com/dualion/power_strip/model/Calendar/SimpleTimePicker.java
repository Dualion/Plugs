package com.dualion.power_strip.model.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class SimpleTimePicker {

    private CharSequence dialogTitle;
    private Time initTime;
    private TimePicker.OnTimeSetListener onTimeSetListener;
    private FragmentManager fragmentManager;

    /**
     * Private constructor that can only be access using the make method
     * @param dialogTitle Title of the Time Picker Dialog
     * @param initTime Time object to use to set the initial Time
     * @param onTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     */
    private SimpleTimePicker(CharSequence dialogTitle, Time initTime,
                             TimePicker.OnTimeSetListener onTimeSetListener,
                             FragmentManager fragmentManager) {

        // Find if there are any DialogFragments from the FragmentManager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment dateTimeDialogFrag = fragmentManager.findFragmentByTag(
                DateTimePicker.TAG_FRAG_TIME
        );

        // Remove it if found
        if(dateTimeDialogFrag != null) {
            fragmentTransaction.remove(dateTimeDialogFrag);
        }
        fragmentTransaction.addToBackStack(null);

        this.dialogTitle = dialogTitle;
        this.initTime = initTime;
        this.onTimeSetListener = onTimeSetListener;
        this.fragmentManager = fragmentManager;

    }

    /**
     * Creates a new instance of the SimpleTimePicker
     * @param dialogTitle Title of the Date Time Picker Dialog
     * @param initTime Date object to use to set the initial Date and Time
     * @param onTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     * @return Returns a SimpleTimePicker object
     */
    public static SimpleTimePicker make(CharSequence dialogTitle, Time initTime,
                                            TimePicker.OnTimeSetListener onTimeSetListener,
                                            FragmentManager fragmentManager) {

        return new SimpleTimePicker(dialogTitle, initTime,
                onTimeSetListener, fragmentManager);

    }

    /**
     * Shows the TimePicker Dialog
     */
    public void show() {

        // Create new TimePicker
        TimePicker TimePicker = com.dualion.power_strip.model.Calendar.TimePicker.newInstance(dialogTitle, initTime);
        TimePicker.setOnTimeSetListener(onTimeSetListener);
        TimePicker.show(fragmentManager, DateTimePicker.TAG_FRAG_TIME);

    }

}
