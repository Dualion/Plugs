package com.dualion.power_strip.model.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Date;

public class SimpleDateTimePicker {

	private final CharSequence dialogTitle;
	private final Date initDate;
	private final DateTimePicker.OnDateTimeSetListener onDateTimeSetListener;
	private final FragmentManager fragmentManager;

	/**
	 * Private constructor that can only be access using the make method
	 *
	 * @param dialogTitle           Title of the Date Time Picker Dialog
	 * @param initDate              Date object to use to set the initial Date and Time
	 * @param onDateTimeSetListener OnDateTimeSetListener interface
	 * @param fragmentManager       Fragment Manager from the activity
	 */
	private SimpleDateTimePicker(CharSequence dialogTitle, Date initDate,
	                             DateTimePicker.OnDateTimeSetListener onDateTimeSetListener,
	                             FragmentManager fragmentManager) {

		// Find if there are any DialogFragments from the FragmentManager
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment dateTimeDialogFrag = fragmentManager.findFragmentByTag(
				DateTimePicker.TAG_FRAG_TIME
		);

		// Remove it if found
		if (dateTimeDialogFrag != null) {
			fragmentTransaction.remove(dateTimeDialogFrag);
		}
		fragmentTransaction.addToBackStack(null);

		this.dialogTitle = dialogTitle;
		this.initDate = initDate;
		this.onDateTimeSetListener = onDateTimeSetListener;
		this.fragmentManager = fragmentManager;

	}

	/**
	 * Creates a new instance of the SimpleDateTimePicker
	 *
	 * @param dialogTitle           Title of the Date Time Picker Dialog
	 * @param initDate              Date object to use to set the initial Date and Time
	 * @param onDateTimeSetListener OnDateTimeSetListener interface
	 * @param fragmentManager       Fragment Manager from the activity
	 * @return Returns a SimpleDateTimePicker object
	 */
	public static SimpleDateTimePicker make(CharSequence dialogTitle, Date initDate,
	                                        DateTimePicker.OnDateTimeSetListener onDateTimeSetListener,
	                                        FragmentManager fragmentManager) {

		return new SimpleDateTimePicker(dialogTitle, initDate,
				onDateTimeSetListener, fragmentManager);

	}

	/**
	 * Shows the DateTimePicker Dialog
	 */
	public void show() {

		// Create new DateTimePicker
		DateTimePicker dateTimePicker = DateTimePicker.newInstance(dialogTitle, initDate);
		dateTimePicker.setOnDateTimeSetListener(onDateTimeSetListener);
		dateTimePicker.show(fragmentManager, DateTimePicker.TAG_FRAG_TIME);

	}

}
