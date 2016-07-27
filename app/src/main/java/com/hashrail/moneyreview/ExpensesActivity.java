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

public class ExpensesActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> dlist = new ArrayList<>();
    DBExpendableList db;
    SQLiteDatabase sd;
    ListView listviewExp;
    PieChart chart;
    private int maxValue = 0;
    private int minValue = 0;
    private int TotalMainExp = 0;
    private int rsexp = 0;
    ArrayList<Entry> entries;
    ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        chart = (PieChart) findViewById(R.id.chartExpenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExpenseOnly);
        setSupportActionBar(toolbar);
        setTitle("Expenses Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView adView = (AdView)findViewById(R.id.adViewExpenses); //add the cast
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        db = new DBExpendableList(getApplicationContext());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();
        listviewExp = (ListView) findViewById(R.id.listviewExp);
        entries = new ArrayList<>();
        labels = new ArrayList<String>();
        callMainMethod();

    }

    private void getTotalExpenses() {

        try {
            String TotalqryExp = "SELECT SUM(rs) FROM " + DBExpendableList.CATEGORYLIST + " WHERE expenses LIKE '1'";
            Cursor crExpTotal = sd.rawQuery(TotalqryExp, null);

            while (crExpTotal.moveToNext()) {

                TotalMainExp = crExpTotal.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FillTotalExpensesByCategoryGroup() {

        try {
            String qryExp = "SELECT name,SUM(rs) FROM " + DBExpendableList.CATEGORYLIST + " WHERE expenses LIKE '1' GROUP BY name";
            Cursor crExp = sd.rawQuery(qryExp, null);

            while (crExp.moveToNext()) {
                rsexp = crExp.getInt(1);
                String cnameexp = crExp.getString(0);

                HashMap<String, String> mapexp = new HashMap<>();
                mapexp.put("NAME", cnameexp);
                mapexp.put("RS", String.valueOf(rsexp));
                dlist.add(mapexp);


                Double Max = ((double) rsexp / TotalMainExp) * 100;
                entries.add(new Entry(Max.floatValue(), 0));
                labels.add(cnameexp);


            }
            SimpleAdapter adapter = new SimpleAdapter(ExpensesActivity.this, dlist, R.layout.list_exp_summary, new String[]{"NAME", "RS"}, new int[]{R.id.lblSummaryCatExp, R.id.lblSummaryRsExp});
            listviewExp.setAdapter(adapter);
            PieDataSet dataset = new PieDataSet(entries, "");
            //dataset.setColors(ColorTemplate.PASTEL_COLORS);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            dataset.setValueTextColor(Color.WHITE);
            dataset.setValueTextSize(12);
            dataset.getSliceSpace();
            PieData data = new PieData(labels, dataset);
            chart.setData(data);
            chart.setDescription("");

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void callMainMethod() {
        getTotalExpenses();

        FillTotalExpensesByCategoryGroup();

    }


}

