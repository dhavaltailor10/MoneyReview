package com.hashrail.moneyreview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class IncomeActivity extends AppCompatActivity {

    PieChart pieChart;
    ListView listViewIncome;

    ArrayList<HashMap<String, String>> dlist = new ArrayList<>();
    DBExpendableList db;
    SQLiteDatabase sd;
    private int maxValue = 0;
    private int minValue = 0;
    private int TotalMainExp = 0;
    private int rsexp = 0;
    ArrayList<Entry> entries;
    ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarIncomeeOnly);
        setSupportActionBar(toolbar);
        setTitle("Income Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView adView = (AdView) findViewById(R.id.adViewIncome); //add the cast
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        pieChart = (PieChart) findViewById(R.id.chartIncome);
        listViewIncome = (ListView) findViewById(R.id.listviewIncome);
        db = new DBExpendableList(getApplicationContext());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();


        entries = new ArrayList<>();
        labels = new ArrayList<String>();
        callMainMethod();
    }


    private void getTotalExpenses() {

        try {
            String TotalqryIcm = "SELECT SUM(rs) FROM " + DBExpendableList.CATEGORYLIST + " WHERE income LIKE '1'";
            Cursor crIcmTotal = sd.rawQuery(TotalqryIcm, null);

            while (crIcmTotal.moveToNext()) {

                TotalMainExp = crIcmTotal.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FillTotalExpensesByCategoryGroup() {

        try {
            String qryIcm = "SELECT name,SUM(rs) FROM " + DBExpendableList.CATEGORYLIST + " WHERE income LIKE '1' GROUP BY name";
            Cursor crIcm = sd.rawQuery(qryIcm, null);

            while (crIcm.moveToNext()) {
                rsexp = crIcm.getInt(1);
                String cnameIcm = crIcm.getString(0);

                HashMap<String, String> mapexp = new HashMap<>();
                mapexp.put("NAME", cnameIcm);
                mapexp.put("RS", String.valueOf(rsexp));
                dlist.add(mapexp);

                Double Max = ((double) rsexp / TotalMainExp) * 100;
                entries.add(new Entry(Max.floatValue(), 0));
                labels.add(cnameIcm);


            }

            SimpleAdapter adapter = new SimpleAdapter(IncomeActivity.this, dlist, R.layout.list_exp_summary, new String[]{"NAME", "RS"}, new int[]{R.id.lblSummaryCatExp, R.id.lblSummaryRsExp});
            listViewIncome.setAdapter(adapter);
            PieDataSet dataset = new PieDataSet(entries, "");
            dataset.setColors(ColorTemplate.JOYFUL_COLORS);
            dataset.setValueTextColor(Color.WHITE);
            dataset.setValueTextSize(12);
            dataset.getSliceSpace();
            PieData data = new PieData(labels, dataset);
            pieChart.setData(data);
            pieChart.setDescription("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callMainMethod() {
        getTotalExpenses();

        FillTotalExpensesByCategoryGroup();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
