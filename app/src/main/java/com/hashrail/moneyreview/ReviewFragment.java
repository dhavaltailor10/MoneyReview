package com.hashrail.moneyreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {

    PieChart chart;
    private int maxValue = 0;
    private int minValue = 0;
    private String name = null, dateMax = null;
    RelativeLayout mainRevieLayoutToHide, MainHideNoDataReview;
    boolean flag = false;
    private String nameMin;

    public ReviewFragment() {
        // Required empty public constructor
    }

    DBExpendableList db;
    SQLiteDatabase sd;
    TextView textViewMax, textviewlostMin, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        AdView adView = (AdView) view.findViewById(R.id.adViewReview); //add the cast
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

        chart = (PieChart) view.findViewById(R.id.chart);
        db = new DBExpendableList(getActivity());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();
        textViewMax = (TextView) view.findViewById(R.id.textViewMax);
        textviewlostMin = (TextView) view.findViewById(R.id.textviewlostMin);
        mainRevieLayoutToHide = (RelativeLayout) view.findViewById(R.id.mainRevieLayoutToHide);
        date = (TextView) view.findViewById(R.id.date);

        selectMaxValue();
        selectMinValue();
        callMainMethod();


        return view;
    }

    private void callMainMethod() {


        int mainTot = maxValue + minValue;
        Double Max = ((double) maxValue / mainTot) * 100;
        Double Min = ((double) minValue / mainTot) * 100;

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(Max.floatValue(), 0));
        entries.add(new Entry(Min.floatValue(), 1));

        PieDataSet dataset = new PieDataSet(entries, "");
        //dataset.setColors(ColorTemplate.PASTEL_COLORS);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setValueTextColor(Color.WHITE);
        dataset.setValueTextSize(16);
        dataset.getSliceSpace();
        if (maxValue == 0) {
            name = "Category name";
        }
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(name);
        labels.add(nameMin);

        try {

            PieData data = new PieData(labels, dataset);
            chart.setData(data);
            chart.setDescription("");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectMaxValue() {

        String sry = "SELECT MAX(rs),date,name FROM " + DBExpendableList.CATEGORYLIST + " WHERE expenses LIKE '1'";
        Cursor cr = sd.rawQuery(sry, null);

        while (cr.moveToNext()) {

            maxValue = cr.getInt(0);
            dateMax = cr.getString(1);
            name = cr.getString(2);
        }
        textviewlostMin.setText(String.valueOf(maxValue));

        if (maxValue == 0) {
            date.setText(String.valueOf("Date"));
        } else {
            date.setText(String.valueOf(dateMax));
        }

    }

    private void selectMinValue() {
        String sry1 = "SELECT MAX(rs),date,name FROM " + DBExpendableList.CATEGORYLIST + " WHERE income LIKE '1'";
        Cursor cr1 = sd.rawQuery(sry1, null);

        while (cr1.moveToNext()) {
            minValue = cr1.getInt(0);
            nameMin = cr1.getString(2);


        }

        textViewMax.setText(String.valueOf(minValue));
    }


}
