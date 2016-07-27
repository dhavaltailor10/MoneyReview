package com.hashrail.moneyreview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by new-3 on 6/13/2016.
 */
public class ListViewAdapter extends ArrayAdapter<String> {

    private Context activity;
    private List<String> categorylist;

    public ListViewAdapter(Context context, int resource) {
        super(context, resource);
        this.activity = context;

    }

    public ListViewAdapter(ExpensesCategoryActivity categoryActivity, int item_listview, ArrayList<String> list, String[] from, int[] to) {
        super(categoryActivity, item_listview);
        this.activity = categoryActivity;
        this.categorylist = list;
    }

    @Override
    public int getCount() {
        return categorylist.size();
    }

    @Override
    public String getItem(int position) {
        return categorylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_listview_for_exp_category, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryname.setText(getItem(position));

        //get first letter of each String item
        String firstLetter = String.valueOf(getItem(position).charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        holder.imageView.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView categoryname;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            categoryname = (TextView) v.findViewById(R.id.textlist);
        }
    }
}