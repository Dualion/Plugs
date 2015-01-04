package com.dualion.power_strip.model.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dualion.power_strip.PowerStripApp;

public class BaseFragment extends Fragment {

	public final static String ARG_PID = "pid";
	public final static String ARG_INDEX = "index";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((PowerStripApp) getActivity().getApplication()).getObjectGraph().inject(this);
	}
}
