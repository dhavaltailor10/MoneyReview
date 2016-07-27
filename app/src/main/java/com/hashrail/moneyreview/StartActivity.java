package com.hashrail.moneyreview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    SessionManager session;
    private Button btnstart;
    private MyApplication application;
    HashMap<String, String> currencyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        session = new SessionManager(getApplicationContext());
        currencyDetails = new HashMap<>();
        currencyDetails = session.getUserDetails();
        btnstart = (Button) findViewById(R.id.btnstart);
        btnstart.getDrawingCacheBackgroundColor();
        btnstart.setTextSize(23);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

    }
}
