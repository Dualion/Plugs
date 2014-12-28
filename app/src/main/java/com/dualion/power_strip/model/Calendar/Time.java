package com.dualion.power_strip.model.calendar;

import java.io.Serializable;
import java.util.Calendar;

public class Time implements Serializable {

    private final int hour;
    private final int minute;

    /**
     * Constructor with no parameters which will create DateTime
     * Object with the current time
     */
    public Time() {
        this(Calendar.getInstance());
    }


    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(int hour) {
        this(hour, 0);
    }

    private Time(Calendar calendar) {
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    public Time(Time time) {
        hour = time.getHour();
        minute = time.getMinute();
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTimeString() {
        return toString();
    }

    public Long getTimeMillis(){
        return ((hour * 60L * 60L) + (minute * 60L)) * 1000L;
    }

    @Override
    public String toString() {
		return String.format("%02d", hour) +
				":" +
				String.format("%02d", minute);
    }
}
