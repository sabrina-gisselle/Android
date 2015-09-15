package com.example.sabrina.x11colors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sabrina on 3/27/2015.
 */
public class XllColorAdapter extends ArrayAdapter<XllColor> {

    private final List<XllColor> colorsList;

    public XllColorAdapter(Context context, List<XllColor> objects) {
        super(context, 0, objects);
        colorsList = objects;
    }

    private static class ViewHolder {
        TextView preview;
        TextView name;
        TextView hexcode;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder; //cache Views to avoid future lookups

        // Check if an existing view is being recycled; if not
        // inflate a new one
        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView =
                LayoutInflater.from(getContext())
                    .inflate(R.layout.color, parent, false);
            viewHolder.preview = (TextView) convertView.findViewById(R.id.preview);
            viewHolder.name = (TextView) convertView.findViewById(R.id.color_name);
            viewHolder.hexcode = (TextView) convertView.findViewById(R.id.rgb_hex_code);

            // cache the Views
            convertView.setTag( viewHolder );
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Find the (sub)Views to populate with data

//        TextView preview = (TextView) convertView.findViewById(R.id.preview);
//        TextView name = (TextView) convertView.findViewById(R.id.color_name);
//        TextView hexcode = (TextView) convertView.findViewById(R.id.rgb_hex_code);

        // Populate the (sub)Views

        XllColor color = getItem( position );
        viewHolder.preview.setBackgroundColor(color.getColorAsInt());
        viewHolder.name.setText( color.getName());
        viewHolder.hexcode.setText( color.getHexcode());
        return convertView;
    }

    public void remove (int position){
        colorsList.remove(position);
        notifyDataSetChanged();
    }
}
