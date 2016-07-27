package com.hashrail.moneyreview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LazyAdapterHistory extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private ImageView thumb_image;
    private ArrayList<Map<String, ?>> mUnfilteredData;
    private int[] mTo;
    private String[] mFrom;

    public LazyAdapterHistory(Activity a, ArrayList<HashMap<String, String>> sdata) {
        activity = a;
        //data = d;
        this.data = sdata;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return data.indexOf(getItem(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_history, null);

        TextView id = (TextView) vi.findViewById(R.id.lblCatAndTitleHistory); // artist
        TextView name = (TextView) vi.findViewById(R.id.lblDateHistory); // title
        TextView url = (TextView) vi.findViewById(R.id.lblrupessHistory); // artist

        thumb_image = (ImageView) vi.findViewById(R.id.imgDrawAplhaHistory); // thumb

//        song = data.get(position);
        // Setting all values in listview
        if(data.get(position).get("RS").contains("-"))
        {
            name.setText(data.get(position).get("RS"));
            name.setTextColor(Color.RED);
        }
        else if(data.get(position).get("RS").contains("+"))
        {
            name.setText(data.get(position).get("RS"));
            name.setTextColor(Color.rgb(0, 150, 136));
        }
        else
        {
            name.setText(data.get(position).get("RS"));
        }


        id.setText(data.get(position).get("TITLE"));
        url.setText(data.get(position).get("DATE"));

        String firstLetter =String.valueOf( data.get(position).get("TITLE").charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color = generator.getColor(data.get(position).get("TITLE"));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        thumb_image.setImageDrawable(drawable);
        return vi;
    }

}