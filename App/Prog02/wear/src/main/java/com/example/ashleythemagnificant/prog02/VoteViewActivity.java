package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class VoteViewActivity extends Activity {

    private TextView mTextView;
    private String location = "Alameda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_view2);
        Intent mainIntent = getIntent();
        while (mainIntent == null) {
            mainIntent = getIntent();
        }
        Bundle extras = mainIntent.getExtras();
        if (extras != null) {
            Log.d("T", "in Watch Main Activity");

            location = mainIntent.getStringExtra("LOCATION");
        }
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText("2012 US Presidential Vote \n " + location +"\n Mitt Romney Barack Obama" +"\n 30 %      70 %");
//                loadData();
            }
        });
    }

    public void loadData() {
        TextView resultView = (TextView) findViewById(R.id.vote_results);
        resultView.setText(" 30 %      70 %");
    }
}