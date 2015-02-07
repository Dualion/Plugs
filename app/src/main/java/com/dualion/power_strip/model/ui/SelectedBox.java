package com.dualion.power_strip.model.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.dualion.power_strip.R;

public class SelectedBox extends CheckBox {

	public SelectedBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setChecked(boolean checked) {
		if (checked) {
			this.setBackgroundResource(R.drawable.checkbox_selected);
		} else {
			this.setBackgroundResource(R.drawable.checkbox_deselected);
		}
		super.setChecked(checked);
	}

}
