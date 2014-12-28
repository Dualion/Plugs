package com.dualion.power_strip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchedulerDiario {

    @SerializedName("start_at")
    @Expose
    private String start;

    @SerializedName("stop_at")
    @Expose
    private String stop;

    @Expose
    private String repeat = "Diario";

    public SchedulerDiario() {
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
}
