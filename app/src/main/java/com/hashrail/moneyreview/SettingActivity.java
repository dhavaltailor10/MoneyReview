package com.hashrail.moneyreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    Button btnrupee, btndollar;
    SessionManager session;
    HashMap<String, String> currencyDetails;
    MyApplication application;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsetting);
        setSupportActionBar(toolbar);
        setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView adView = (AdView) findViewById(R.id.adViewSetting); //add the cast
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        spinner = (Spinner) findViewById(R.id.spinnercurrency);

        List<String> currency = new ArrayList<String>();
        currency.add("Set Currency");
        currency.add("Ruppes");
        currency.add("Doller");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, currency);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        session = new SessionManager(getApplicationContext());
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();
        application = new MyApplication();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(), "Item " + item, Toast.LENGTH_LONG).show();
                if (i == 0) {

                } else if (i == 1) {
                    session.setCurrency("Rs.");
                    Intent i2 = new Intent(SettingActivity.this, Main2Activity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                } else if (i == 2) {
                    session.setCurrency("$");
                    Intent i3 = new Intent(SettingActivity.this, Main2Activity.class);
                    i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i3);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
