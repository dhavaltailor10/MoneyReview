package com.hashrail.moneyreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {
    Calendar myCalendardd = Calendar.getInstance();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    List<String> listDataHeaderRupees = new ArrayList<>();
    HashMap<String, ArrayList<String>> listDataChildID;
    HashMap<String, ArrayList<String>> listDataChild;
    HashMap<String, ArrayList<String>> listDataChildDate;
    HashMap<String, ArrayList<String>> listDataChildTitle;
    HashMap<String, ArrayList<String>> listDataChildRS;

    ArrayList<String> mchildID = new ArrayList<String>();
    ArrayList<String> mchild = new ArrayList<String>();
    ArrayList<String> mchildDate = new ArrayList<String>();
    ArrayList<String> mchildTitle = new ArrayList<String>();
    ArrayList<String> mchildRS = new ArrayList<String>();
    RelativeLayout MainHideNoData;

    ArrayList<String> mchildID1 = new ArrayList<String>();
    ArrayList<String> mchild1 = new ArrayList<String>();
    ArrayList<String> mchildDate1 = new ArrayList<String>();
    ArrayList<String> mchildTitle1 = new ArrayList<String>();
    ArrayList<String> mchildRS1 = new ArrayList<String>();

    ImageView imageViewNoData;
    TextView textViewNoData, textviewClickPlus;
    Header header = new Header();
    NewOperation no = new NewOperation();
    String[] from = {DBExpendableList.CATEGORY_DATE};
    int[] to = {R.id.datelist};
    EditText editText, editTexttitle, edittextcategory;
    NewOperation o = new NewOperation();
    SessionManager session;
    HashMap<String, String> currencyDetails;
    LayoutInflater inflater;
    private int totIcm, totExp;
    private String currentCurrency;
    private String ok;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AdView adView = (AdView) view.findViewById(R.id.adViewHome); //add the cast
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        inflater = getActivity().getLayoutInflater();
        View c = inflater.inflate(R.layout.content_new_operation, null);
        editText = (EditText) c.findViewById(R.id.editText2);

        MainHideNoData = (RelativeLayout) view.findViewById(R.id.MainHideNoData);

        session = new SessionManager(getActivity());
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();
        editTexttitle = (EditText) c.findViewById(R.id.editTexttitle);
        edittextcategory = (EditText) c.findViewById(R.id.editextcategory);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewOperation.class);
                startActivity(i);
            }
        });
        ok = currencyDetails.get(SessionManager.KEY_CURRENCY);
        exportDB();

        expListView = (ExpandableListView) view.findViewById(R.id.expandlist);
        try {
            expListView.expandGroup(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                Intent i = new Intent(getActivity(), NewOperation.class);
                i.putExtra("id", listDataChildID.get(listDataHeader.get(groupPosition)).get(childPosition));
                i.putExtra("title", listDataChildTitle.get(listDataHeader.get(groupPosition)).get(childPosition));
                i.putExtra("cate", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                i.putExtra("date", listDataChildDate.get(listDataHeader.get(groupPosition)).get(childPosition));
                i.putExtra("rs", listDataChildRS.get(listDataHeader.get(groupPosition)).get(childPosition));
                i.putExtra("activity", "HomeFragment");
                startActivity(i);

                return false;
            }
        });


        prepareListData();


        return view;
    }

    private void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + getActivity().getPackageName()
                    + "/databases/" + DBExpendableList.DATABASE_NAME + "";
            String backupDBPath = DBExpendableList.DATABASE_NAME;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }

    public static boolean isValidDate(String pDateString) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(pDateString);
        return new Date().before(date);
    }

    private void prepareListData() {

        boolean flag = false;
        listDataHeader = new ArrayList<String>();
        listDataHeaderRupees = new ArrayList<>();
        listDataChildID = new HashMap<>();
        listDataChild = new HashMap<>();
        listDataChildTitle = new HashMap<>();
        listDataChildDate = new HashMap<>();
        listDataChildRS = new HashMap<>();

        DBExpendableList dbExpendableList = new DBExpendableList(getActivity());
        SQLiteDatabase db = dbExpendableList.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " + DBExpendableList.CATEGORY_DATE + " FROM " + DBExpendableList.CATEGORYLIST + " group by date order by date desc", null);
        Log.e("q1", "" + c);
        getActivity().startManagingCursor(c);
        listDataHeader.clear();
        while (c.moveToNext()) {
            flag = true;
            mchildID.clear();
            mchild.clear();
            mchildTitle.clear();
            mchildDate.clear();
            mchildRS.clear();
            try {
                String dd = c.getString(0);

                try {
                    //*********************
                    if (isValidDate(dd)) {
                        System.out.println("d1 is after d2");
                        // Toast.makeText(getActivity(), "The Item is Added into the Future Task", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("d1 is equal to d2");
                        String qry = "select * from categorylist where date LIKE '" + dd + "' ORDER BY _id DESC";
                        Cursor c1 = db.rawQuery(qry, null);
                        getActivity().startManagingCursor(c1);

                        listDataHeader.add(dd);

                        while (c1.moveToNext()) {

                            mchildID.add(c1.getString(0));
                            mchild.add(c1.getString(1));
                            mchildTitle.add(c1.getString(2));
                            mchildDate.add(c1.getString(3));

                            String exp = c1.getString(5);
                            String icm = c1.getString(6);
                            if (exp.matches("1")) {
                                mchildRS.add("-" + c1.getString(4));
                                String q = "SELECT SUM(rs) FROM categorylist WHERE expenses LIKE '1' AND date LIKE '" + dd + "'";
                                Cursor cr = db.rawQuery(q, null);
                                while (cr.moveToNext()) {
                                    totExp = cr.getInt(0);
                                }
                            } else if (icm.matches("1")) {
                                mchildRS.add("+" + c1.getString(4));

                                String qq = "SELECT SUM(rs) FROM categorylist WHERE income LIKE '1' AND date LIKE '" + dd + "'";
                                Cursor crr = db.rawQuery(qq, null);
                                while (crr.moveToNext()) {
                                    totIcm = crr.getInt(0);

                                }
                            } else {
                                mchildRS.add(c1.getString(4));
                            }


                            mchildID1 = (ArrayList<String>) mchildID.clone();
                            mchild1 = (ArrayList<String>) mchild.clone();
                            mchildTitle1 = (ArrayList<String>) mchildTitle.clone();
                            mchildDate1 = (ArrayList<String>) mchildDate.clone();
                            mchildRS1 = (ArrayList<String>) mchildRS.clone();
                        }
                        int TotalValue = totIcm - totExp;
                        if (String.valueOf(TotalValue).contains("-")) {
                            listDataHeaderRupees.add(currencyDetails.get(SessionManager.KEY_CURRENCY) + String.valueOf(TotalValue));
                        } else {
                            listDataHeaderRupees.add(currencyDetails.get(SessionManager.KEY_CURRENCY) + "+" +
                                    "" + String.valueOf(TotalValue));
                        }

                        listDataChildID.put(c.getString(0), mchildID1);
                        listDataChild.put(c.getString(0), mchild1);
                        listDataChildTitle.put(c.getString(0), mchildTitle1);
                        listDataChildDate.put(c.getString(0), mchildDate1);
                        listDataChildRS.put(c.getString(0), mchildRS1);
                    }

                    //*********************

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        if (flag == true) {
            Log.d("Lists", listDataChildID.toString() + " " + listDataChild.toString() + " " + listDataChildTitle.toString() + " " + listDataChildDate.toString() + " " + listDataChildRS.toString());
            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataHeaderRupees, listDataChildID, listDataChild, listDataChildTitle, listDataChildRS, expListView);
            expListView.setAdapter(listAdapter);
            expListView.expandGroup(0);

        } else {
            expListView.setVisibility(View.GONE);
            MainHideNoData.setVisibility(View.VISIBLE);
        }

    }
}
