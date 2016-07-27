package com.hashrail.moneyreview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by new-3 on 6/20/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataHeadeRupees; // header rupees
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChildID;
    private HashMap<String, ArrayList<String>> _listDataChild;
    private HashMap<String, ArrayList<String>> _listDataChildTitle;
    private HashMap<String, ArrayList<String>> _listDataChildRs;
    private String ID, hh, hM, rs;
    private String HEADER, RUPEES;
    private String HEADERCount, RUPEESCount;
    SessionManager session;
    ExpandableListView expListViewGet;
    HashMap<String, String> currencyDetails;
    private int lastExpandedGroupPosition = 0;
   /* public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }*/

    public ExpandableListAdapter(FragmentActivity activity, List<String> listDataHeader, List<String> listDataHeadeRupees, HashMap<String, ArrayList<String>> listDataChildID, HashMap<String, ArrayList<String>> listDataChild, HashMap<String, ArrayList<String>> listDataChildTitle, HashMap<String, ArrayList<String>> listDataChildRs, ExpandableListView expListView) {

        this._context = activity;
        this._listDataChildID = listDataChildID;
        this._listDataHeader = listDataHeader;
        this._listDataHeadeRupees = listDataHeadeRupees;
        this._listDataChild = listDataChild;
        this._listDataChildTitle = listDataChildTitle;
        this._listDataChildRs = listDataChildRs;
        this.expListViewGet = expListView;
        session = new SessionManager(this._context);
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        ID = this._listDataChildID.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        hh = this._listDataChildTitle.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        hM = this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        rs = this._listDataChildRs.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        return hh + hM + rs;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childTextWhole = (String) getChild(groupPosition, childPosition);
        String childID = ID.toString();
        String childCategory = hM.toString();
        String childTitle = hh.toString();
        String childRS = rs.toString();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        ImageView imgDrawAplha = (ImageView) convertView.findViewById(R.id.imgDrawAplha);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

        TextView lbltitle = (TextView) convertView.findViewById(R.id.lbltitle);
        TextView lblrs = (TextView) convertView.findViewById(R.id.lblrupess);

        txtListChild.setText(childCategory);
        txtListChild.setTypeface(null, Typeface.BOLD);

        lbltitle.setText(childTitle);
        lbltitle.setTypeface(null, Typeface.BOLD);

        if (childRS.contains("+")) {
            lblrs.setText(currencyDetails.get(SessionManager.KEY_CURRENCY) + childRS);
            lblrs.setTextColor(Color.rgb(0, 150, 136));

        } else if (childRS.contains("-")) {
            lblrs.setText(currencyDetails.get(SessionManager.KEY_CURRENCY) + childRS);
            lblrs.setTextColor(Color.RED);
        } else {
            lblrs.setText(currencyDetails.get(SessionManager.KEY_CURRENCY) + childRS);

        }


        String firstLetter = String.valueOf(childCategory.charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color = generator.getColor(childCategory);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        imgDrawAplha.setImageDrawable(drawable);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        HEADER = _listDataHeader.get(groupPosition);
        RUPEES = _listDataHeadeRupees.get(groupPosition);
        return HEADER + RUPEES;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        headerTitle = headerTitle.substring(0, HEADER.length());
        String rs = RUPEES.toString();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        TextView lblListHeaderRS = (TextView) convertView.findViewById(R.id.lblListHeaderRS);

        lblListHeader.setTypeface(null, Typeface.BOLD);

        lblListHeader.setText(headerTitle);
        if (rs.contains("-")) {
            lblListHeaderRS.setText(rs);
            lblListHeaderRS.setTextColor(Color.RED);
        } else if (rs.contains("+")) {
            lblListHeaderRS.setText(rs);
            lblListHeaderRS.setTextColor(Color.rgb(0, 150, 136));
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
