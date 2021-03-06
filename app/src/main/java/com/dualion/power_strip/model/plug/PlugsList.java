package com.dualion.power_strip.model.plug;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PlugsList {

	@Expose
	private List<Plug> plugs = new ArrayList<>();

	public List<Plug> getPlugs() {
		return plugs;
	}

	public void setPlugs(List<Plug> plugs) {
		this.plugs = plugs;
	}


}
