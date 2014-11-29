package dualion.com.power_strip.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import dualion.com.power_strip.R;
import dualion.com.power_strip.model.Plug;

public class CustomGrid extends BaseAdapter {

    private LayoutInflater inflater;
    private final ArrayList<Plug> plugs;
    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView pid;
        TextView name;
        TextView component;
        TextView pin;
        ImageView img;
    }

    public CustomGrid(Context c, ArrayList<Plug> plugs) {
        context = c;
        this.plugs = plugs;
    }

    @Override
    public int getCount() {
        return plugs.size();
    }

    @Override
    public Plug getItem(int position) {
        return plugs.get(position);
    }

    public void setItem(int position, Plug plug){
        plugs.set(position, plug);
        notifyDataSetChanged();
    }

    public void setComponent(int position, String component){
        plugs.get(position).setComponent(component);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(plugs.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        if (plugs.get(position).getPinState().compareToIgnoreCase("false") == 0){
            viewHolder.img.setImageResource(R.drawable.plugoff);
        } else {
            viewHolder.img.setImageResource(R.drawable.plugon);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
