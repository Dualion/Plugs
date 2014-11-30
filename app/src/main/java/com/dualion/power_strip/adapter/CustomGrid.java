package com.dualion.power_strip.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.dualion.power_strip.R;
import com.dualion.power_strip.model.Plug;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.restapi.PlugService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomGrid extends BaseAdapter {

    private LayoutInflater inflater;
    private final ArrayList<Plug> plugs;
    private Context context;
    private PlugService plugService;

    // View lookup cache
    private static class ViewHolder {
        TextView pid;
        TextView name;
        TextView component;
        TextView pin;
        ImageView img;
    }

    public CustomGrid(Context c, ArrayList<Plug> plugs, PlugService plugService) {
        context = c;
        this.plugs = plugs;
        this.plugService = plugService;
    }

    @Override
    public int getCount() {
        return plugs.size();
    }

    @Override
    public Plug getItem(int position) {
        return plugs.get(position);
    }

    public void setItem(int position, Plug plug) {
        plugs.set(position, plug);
        notifyDataSetChanged();
    }

    public void setComponent(int position, String component) {
        plugs.get(position).setComponent(component);
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
            convertView = inflater.inflate(R.layout.grid_single, null);
            viewHolder = new ViewHolder();
            viewHolder.component = (TextView) convertView.findViewById(R.id.component);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.pin = (TextView) convertView.findViewById(R.id.pin);
            viewHolder.pid = (TextView) convertView.findViewById(R.id.pid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.pid.setText(plug.getPinId());
        viewHolder.name.setText("Plug: " + plug.getId());
        viewHolder.pin.setText("Pin: " + plug.getPinId());

        SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(context);
        plug.setComponent((mySettings.getString(plug.getPinId(), "-")));
        viewHolder.component.setText(plug.getComponent());

        if (plugs.get(position).getPinState().compareToIgnoreCase("false") == 0) {
            viewHolder.img.setImageResource(R.drawable.plugoff);
        } else {
            viewHolder.img.setImageResource(R.drawable.plugon);
        }

        viewHolder.img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        plugService.setPlug(position + 1, new Callback<PlugsList>() {
                            @Override
                            public void success(PlugsList plugsList, Response response) {
                                Toast.makeText(context, "Successful: " + response.getUrl(), Toast.LENGTH_SHORT).show();

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


        // Return the completed view to render on screen
        return convertView;
    }
}
