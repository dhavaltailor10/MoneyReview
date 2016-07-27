package com.hashrail.moneyreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ReportFragment extends Fragment {


    CardView cardViewExp, cardViewInc;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_report, container, false);
        AdView adView = (AdView) view.findViewById(R.id.adViewReport); //add the cast
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

        cardViewExp = (CardView) view.findViewById(R.id.cardviewExpenses);
        cardViewInc = (CardView) view.findViewById(R.id.cardviewIncome);

        cardViewExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpensesActivity.class);
                startActivity(intent);

            }
        });

        cardViewInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), IncomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
