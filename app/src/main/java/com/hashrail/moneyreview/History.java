package com.hashrail.moneyreview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class History extends AppCompatActivity {

    ListView listviewUnCat;
    DBExpendableList db;
    LazyAdapterHistory mAdapter;
    SQLiteDatabase sd;
    ArrayList<HashMap<String, String>> addlist = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> currencyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarhistory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("History");
        AdView adView = (AdView) findViewById(R.id.adViewHistory); //add the cast
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        listviewUnCat = (ListView) findViewById(R.id.listviewHistory);

        db = new DBExpendableList(getApplicationContext());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();
        session = new SessionManager(getApplicationContext());
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();

        FillDataFromHistory();
    }

    private void FillDataFromHistory() {

        String q = "SELECT * FROM " + DBExpendableList.CATEGORYLIST + " ORDER BY date DESC";

        Cursor cr = sd.rawQuery(q, null);
        while (cr.moveToNext()) {
            String cat = cr.getString(1);
            String title = cr.getString(2);
            String date = cr.getString(3);
            String rs = String.valueOf(cr.getInt(4));
            String exp = String.valueOf(cr.getInt(5));
            String icm = String.valueOf(cr.getInt(6));

            HashMap<String, String> map = new HashMap<>();
            map.put("TITLE", cat + " ," + title);
            map.put("DATE", date);


            if (exp.matches("1")) {
                map.put("RS", "- " + currencyDetails.get(SessionManager.KEY_CURRENCY) + rs);

            } else if (icm.matches("1")) {
                map.put("RS", "+ " + currencyDetails.get(SessionManager.KEY_CURRENCY) + rs);
            } else {
                map.put("RS", rs);

            }
            addlist.add(map);

        }

        /*SimpleAdapter adapter = new SimpleAdapter(History.this, addlist, R.layout.list_item_history, new String[]{"TITLE", "DATE", "RS"}, new int[]{R.id.lblCatAndTitleHistory, R.id.lblDateHistory, R.id.lblrupessHistory});
          listviewUnCat.setAdapter(adapter);*/

        mAdapter = new LazyAdapterHistory(History.this, addlist);
        listviewUnCat.setAdapter(mAdapter);
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
