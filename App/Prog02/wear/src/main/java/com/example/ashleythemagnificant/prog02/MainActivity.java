package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

            }
        });
        Intent intent = getIntent();
        while (intent == null) {
            intent = getIntent();
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("T", "in Watch Main Activity");
            String selection = extras.getString("SELECTION");
            mTextView.setText(selection);
        }

    }
}