package dualion.com.plugs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import dualion.com.plugs.R;
import dualion.com.plugs.model.Plug;

public class CustomGrid extends BaseAdapter {

    private LayoutInflater inflater;
    private final ArrayList<Plug> plugs;
    private Context context;

    // View lookup cache
    /*private static class ViewHolder {
        TextView grid_text;
    }*/

    private static class ViewHolder {
        TextView pid;
        TextView name;
        TextView price;
        TextView description;
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
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(plugs.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Plug product = (Plug) this.getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_single, null);
            viewHolder = new ViewHolder();
            //viewHolder.grid_text = (TextView) convertView.findViewById(R.id.grid_text);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.pid = (TextView) convertView.findViewById(R.id.pid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String outPlug = plugs.get(position).getId() + ":" + plugs.get(position).getPinState();

        viewHolder.name.setText("Plug: " + plugs.get(position).getId());
        viewHolder.description.setText( "State: " + plugs.get(position).getPinState());
        //viewHolder.price.setText(product.getPrice());
        //viewHolder.description.setText(plugs.get(position).getPinState());

        if (plugs.get(position).getPinState().compareToIgnoreCase("false") == 0){
            viewHolder.img.setImageResource(R.drawable.plugoff);
        } else {
            viewHolder.img.setImageResource(R.drawable.plugon);
        }


        // Populate the data into the template view using the data object
//        viewHolder.grid_text.setText(outPlug);
//
//        if (plugs.get(position).getPinState().compareToIgnoreCase("false") == 0){
//            viewHolder.grid_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plugoff, 0, 0, 0);
//        } else {
//            viewHolder.grid_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plugon, 0, 0, 0);
//        }

        // Return the completed view to render on screen
        return convertView;


    }
}
