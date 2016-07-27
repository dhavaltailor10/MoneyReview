package com.hashrail.moneyreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;


public class SumFragment extends Fragment {

    RelativeLayout MainHideNoDataSummary;
    ListView listviewSummary;
    DBExpendableList db;
    LazyAdapterSummary mAdapter;
    SQLiteDatabase sd;
    ArrayList<HashMap<String, String>> addlist = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> currencyDetails;

    boolean flag = false;

    public SumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sum, container, false);
        AdView adView = (AdView) view.findViewById(R.id.adViewSummary); //add the cast
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewOperation.class);
                startActivity(i);
            }
        });

        listviewSummary = (ListView) view.findViewById(R.id.listsummary);
        MainHideNoDataSummary = (RelativeLayout) view.findViewById(R.id.MainHideNoDataSummary);
        db = new DBExpendableList(getActivity());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();
        session = new SessionManager(getContext());
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();


        FillDataFromSummary();

        return view;
    }

    private void FillDataFromSummary() {


        try {
            String q = "SELECT SUM(rs),name,expenses,income FROM " + DBExpendableList.CATEGORYLIST + " GROUP BY name";

            Cursor cr = sd.rawQuery(q, null);
            if (cr.getCount() == 0) {
                listviewSummary.setVisibility(View.GONE);
                MainHideNoDataSummary.setVisibility(View.VISIBLE);

            } else {
                while (cr.moveToNext()) {

                    String cat = cr.getString(1);
                    String rs = String.valueOf(cr.getInt(0));
                    String exp = String.valueOf(cr.getInt(2));
                    String icm = String.valueOf(cr.getInt(3));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("CATE", cat);
                    map.put("RS", rs);
                    if (exp.matches("1")) {
                        map.put("RS", "- " + currencyDetails.get(SessionManager.KEY_CURRENCY) + rs);

                    } else if (icm.matches("1")) {
                        map.put("RS", "+ " + currencyDetails.get(SessionManager.KEY_CURRENCY) + rs);
                    } else {
                        map.put("RS", rs);

                    }
                    addlist.add(map);

                }
                mAdapter = new LazyAdapterSummary(getActivity(), addlist);
                listviewSummary.setAdapter(mAdapter);
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        }


    }


}
