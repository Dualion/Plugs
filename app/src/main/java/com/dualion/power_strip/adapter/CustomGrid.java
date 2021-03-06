package com.dualion.power_strip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.plug.Plug;
import com.dualion.power_strip.model.plug.PlugsList;
import com.dualion.power_strip.restapi.PlugService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomGrid extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Plug> plugs;
	private final Context context;
	private final PlugService plugService;
	//private final int lastPosition;

	// View lookup cache
	private static class ViewHolder {
		TextView pid;
		TextView name;
		TextView component;
		TextView state;
		TextView frequency;
		ImageView img;
	}

	public CustomGrid(Context c, ArrayList<Plug> plugs, PlugService plugService) {
		this.context = c;
		this.plugs = plugs;
		this.plugService = plugService;
		//this.lastPosition = -1;
	}

	@Override
	public int getCount() {
		return plugs.size();
	}

	@Override
	public Plug getItem(int position) {
		return plugs.get(position);
	}

	void setItem(int position, Plug plug) {
		plugs.set(position, plug);
		notifyDataSetChanged();
	}

	public void setComponent(int position, String component) {
		plugs.get(position).setComponent(component);
		notifyDataSetChanged();
	}

	public void setPlugs(ArrayList<Plug> plugs) {
		this.plugs = plugs;
		notifyDataSetChanged();
	}


	@Override
	public long getItemId(int position) {
		return Long.parseLong(plugs.get(position).getId());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Plug plug = this.getItem(position);

		ViewHolder viewHolder; // view lookup cache stored in tag
		if (inflater == null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.plug, null);
			viewHolder = new ViewHolder();
			viewHolder.pid = (TextView) convertView.findViewById(R.id.pid);
			viewHolder.component = (TextView) convertView.findViewById(R.id.component);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.thumbnail);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.frequency = (TextView) convertView.findViewById(R.id.frequency);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.pid.setText(plug.getId());
		viewHolder.name.setText("Plug " + plug.getId() + ": ");
		viewHolder.component.setText(plug.getComponent());

		//Estado de la programación
		switch (plug.getSchedulerState()) {
			case ACTIVO:
				viewHolder.state.setText("Scheduler: " + context.getResources().getString(R.string.activo));

				//tipo de programación
				switch (plug.getRepeatFrecuency()) {
					case DIARIO:
						viewHolder.frequency.setText(context.getResources().getString(R.string.frequency) + ": " + context.getResources().getString(R.string.diaria));
						break;
					case SEMANAL:
						viewHolder.frequency.setText(repeticionSemanal(plug));
						break;
					case NONE:
						viewHolder.frequency.setText(context.getResources().getString(R.string.frequency) + ": " + context.getResources().getString(R.string.unica));
						break;
				}

				break;
			case PARADO:
				viewHolder.state.setText("Scheduler:" + context.getResources().getString(R.string.inactivo));
				viewHolder.frequency.setText("");
				break;
		}

		//Imagen Stado plug
		if (plugs.get(position).getPinState().compareToIgnoreCase("false") == 0) {
			viewHolder.img.setBackgroundResource(R.drawable.plugoffbuttons);
		} else {
			viewHolder.img.setBackgroundResource(R.drawable.plugonbuttons);
		}

		viewHolder.img.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						plugService.toggleStatePlugFromId(position + 1, new Callback<PlugsList>() {
							@Override
							public void success(PlugsList plugsList, Response response) {
								if (plugsList.getPlugs().size() > 0) {
									Plug plug = plugsList.getPlugs().get(0);
									int position = Integer.valueOf(plug.getId()) - 1;
									setItem(position, plug);
								}
							}

							@Override
							public void failure(RetrofitError retrofitError) {
								Toast.makeText(context, "Fail: " + retrofitError.getUrl(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				});

		// This tells the view where to start based on the direction of the scroll.
		// If the last position to be loaded is <= the current position, we want
		// the views to start below their ending point (500f further down).
		// Otherwise, we start above the ending point.
		/*float initialTranslation = (lastPosition <= position ? 500f : -500f);

        convertView.setTranslationY(initialTranslation);
        convertView.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        lastPosition = position;*/

		// Return the completed view to render on screen
		return convertView;
	}

	private String repeticionSemanal(Plug plug) {

		String text;

		text = context.getResources().getString(R.string.freq) + ": " + context.getResources().getString(R.string.semanal) + "   ";

		text += context.getResources().getString(R.string.dia) + ": ";
		if (plug.getRepeatOnDays().getMonday().equals("True"))
			text += " " + context.getResources().getString(R.string.Lun);
		if (plug.getRepeatOnDays().getTuesday().equals("True"))
			text += " " + context.getResources().getString(R.string.Mar);
		if (plug.getRepeatOnDays().getWednesday().equals("True"))
			text += " " + context.getResources().getString(R.string.Mir);
		if (plug.getRepeatOnDays().getThursday().equals("True"))
			text += " " + context.getResources().getString(R.string.Jue);
		if (plug.getRepeatOnDays().getFriday().equals("True"))
			text += " " + context.getResources().getString(R.string.Vie);
		if (plug.getRepeatOnDays().getSaturday().equals("True"))
			text += " " + context.getResources().getString(R.string.Sab);
		if (plug.getRepeatOnDays().getSunday().equals("True"))
			text += " " + context.getResources().getString(R.string.Dom);

		return text;
	}
}
