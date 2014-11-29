package dualion.com.power_strip.model;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class PlugsList {

    @Expose
    private List<Plug> plugs = new ArrayList<Plug>();

    public List<Plug> getPlugs() {
        return plugs;
    }

    public void setPlugs(List<Plug> plugs) {
        this.plugs = plugs;
    }


}
