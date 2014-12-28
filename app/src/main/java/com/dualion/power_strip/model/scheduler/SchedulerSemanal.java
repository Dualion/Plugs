package com.dualion.power_strip.model.scheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchedulerSemanal {

    @SerializedName("start_at")
    @Expose
    private String start;

    @SerializedName("stop_at")
    @Expose
    private String stop;

    @Expose
    private String repeat = "Semanal";

    @Expose
    private DaysOfWeek repeatOnDays;

    public SchedulerSemanal() {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public DaysOfWeek getRepeatOnDays() {
        return repeatOnDays;
    }

    public void setRepeatOnDays(DaysOfWeek repeatOnDays) {
        this.repeatOnDays = repeatOnDays;
    }

    public class DaysOfWeek {

        @Expose
        private String monday;

        @Expose
        private String tuesday;

        @Expose
        private String wednesday;

        @Expose
        private String thursday;

        @Expose
        private String friday;

        @Expose
        private String saturday;

        @Expose
        private String sunday;

        public DaysOfWeek() {
        }

        public String getMonday() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        public String getTuesday() {
            return tuesday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday = tuesday;
        }

        public String getWednesday() {
            return wednesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday = wednesday;
        }

        public String getThursday() {
            return thursday;
        }

        public void setThursday(String thursday) {
            this.thursday = thursday;
        }

        public String getFriday() {
            return friday;
        }

        public void setFriday(String friday) {
            this.friday = friday;
        }

        public String getSaturday() {
            return saturday;
        }

        public void setSaturday(String saturday) {
            this.saturday = saturday;
        }

        public String getSunday() {
            return sunday;
        }

        public void setSunday(String sunday) {
            this.sunday = sunday;
        }
    }

}
