package com.dualion.power_strip.model.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Date date;
	private final Calendar calendar;

	/**
	 * Constructor with no parameters which will create DateTime
	 * Object with the current date and time
	 */
	public DateTime() {
		this(new Date());
	}

	/**
	 * Constructor with Date parameter which will initialize the
	 * object to the date specified
	 *
	 * @param date init date
	 */
	public DateTime(Date date) {

		this.date = date;
		calendar = Calendar.getInstance();
		calendar.setTime(this.date);

	}

	/**
	 * Constructor with DateFormat and DateString which will be
	 * used to initialize the object to the date string specified
	 *
	 * @param dateFormat String DateFormat
	 * @param dateString Date in String
	 */
	private DateTime(String dateFormat, String dateString) {

		calendar = Calendar.getInstance();
		SimpleDateFormat mFormat = new SimpleDateFormat(dateFormat);

		try {
			date = mFormat.parse(dateString);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Constructor with DateString formatted as default DateFormat
	 * defined in DATE_FORMAT variable
	 *
	 * @param dateString init date in string format
	 */
	public DateTime(String dateString) {

		this(DATE_FORMAT, dateString);

	}

	/**
	 * Constructor with Year, Month, Day, Hour, Minute, and Second
	 * which will be use to set the date to
	 *
	 * @param year           Year
	 * @param monthOfYear    Month of Year
	 * @param dayOfMonth     Day of Month
	 * @param hourOfDay      Hour of Day
	 * @param minuteOfHour   Minute
	 * @param secondOfMinute Second
	 */
	private DateTime(int year, int monthOfYear, int dayOfMonth,
	                 int hourOfDay, int minuteOfHour, int secondOfMinute) {

		calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
		date = calendar.getTime();

	}

	/**
	 * Constructor with Year, Month Day, Hour, Minute which
	 * will be use to set the date to
	 *
	 * @param year         Year
	 * @param monthOfYear  Month of Year
	 * @param dayOfMonth   Day of Month
	 * @param hourOfDay    Hour of Day
	 * @param minuteOfHour Minute
	 */
	public DateTime(int year, int monthOfYear, int dayOfMonth,
	                int hourOfDay, int minuteOfHour) {

		this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0);

	}

	/**
	 * Constructor with Date only (no time)
	 *
	 * @param year        Year
	 * @param monthOfYear Month of Year
	 * @param dayOfMonth  Day of Month
	 */
	public DateTime(int year, int monthOfYear, int dayOfMonth) {

		this(year, monthOfYear, dayOfMonth, 0, 0, 0);

	}

	public Date getDate() {
		return date;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	String getDateString(String dateFormat) {

		SimpleDateFormat mFormat = new SimpleDateFormat(dateFormat);
		return mFormat.format(date);

	}

	public String getDateString() {

		return getDateString(DATE_FORMAT);

	}

	public int getYear() {

		return calendar.get(Calendar.YEAR);

	}

	public int getMonthOfYear() {

		return calendar.get(Calendar.MONTH);

	}

	public int getDayOfMonth() {

		return calendar.get(Calendar.DAY_OF_MONTH);

	}

	public int getHourOfDay() {

		return calendar.get(Calendar.HOUR_OF_DAY);

	}

	public int getMinuteOfHour() {

		return calendar.get(Calendar.MINUTE);

	}

	public int getSecondOfMinute() {

		return calendar.get(Calendar.SECOND);

	}

}