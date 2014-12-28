package com.dualion.power_strip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scheduler {

    @SerializedName("start_at")
    @Expose
    private Long start;

    @SerializedName("stop_at")
    @Expose
    private Long stop;

    @Expose
    private String repeat = "";

    public Scheduler() {
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getStop() {
        return stop;
    }

    public void setStop(Long stop) {
        this.stop = stop;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
