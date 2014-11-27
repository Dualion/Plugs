package dualion.com.plugs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plug {

    @Expose
    private String hour;

    @Expose
    private String id;

    @SerializedName("pin_id")
    @Expose
    private String pinId;

    @SerializedName("pin_state")
    @Expose
    private String pinState;

    public String getHour() {
        return hour;
    }

    Plug(){}

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getPinState() {
        return pinState;
    }

    public void setPinState(String pinState) {
        this.pinState = pinState;
    }

}
