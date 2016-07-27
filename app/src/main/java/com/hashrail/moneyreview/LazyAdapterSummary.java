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

public class LazyAdapterSummary extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private ImageView thumb_image;
    private ArrayList<Map<String, ?>> mUnfilteredData;

    public LazyAdapterSummary(Activity a, ArrayList<HashMap<String, String>> sdata) {
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
            vi = inflater.inflate(R.layout.list_item_summary, null);

        TextView cate = (TextView) vi.findViewById(R.id.lblCateSummary); // artist
        TextView rs = (TextView) vi.findViewById(R.id.lblRSSummary); // title


        thumb_image = (ImageView) vi.findViewById(R.id.imgDrawAplhaSummary); // thumb


        if(data.get(position).get("RS").contains("-"))
        {
            rs.setText(data.get(position).get("RS"));
            rs.setTextColor(Color.RED);
        }
        else if(data.get(position).get("RS").contains("+"))
        {
            rs.setText(data.get(position).get("RS"));
            rs.setTextColor(Color.rgb(0, 150, 136));
        }
        else
        {
            rs.setText(data.get(position).get("RS"));
        }


        cate.setText(data.get(position).get("CATE"));
        rs.setText(data.get(position).get("RS"));

        String firstLetter =String.valueOf( data.get(position).get("CATE").charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color = generator.getColor(data.get(position).get("CATE"));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        thumb_image.setImageDrawable(drawable);
        return vi;
    }

}