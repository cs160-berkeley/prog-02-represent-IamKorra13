package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestConnection extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        Log.d("T", "Starting Test Connection class");
//        mFeedBtn = (Button) findViewById(R.id.button);
////        mFeedBtn.setText("Hi there");
//
//        mFeedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("T", "At on click in Test Connection Class");
//                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
//            }
//        });
    }

    public void selfDestruct(View view) {
        Log.d("T", "Start Watch toPhone Service from on Destruct Button method");
        Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        sendIntent.putExtra("SELECTION", "I am from TestConnection");
        startService(sendIntent);
    }

}
