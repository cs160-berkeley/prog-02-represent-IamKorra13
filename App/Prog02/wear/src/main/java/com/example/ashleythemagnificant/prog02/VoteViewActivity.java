package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class VoteViewActivity extends Activity {
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private TextView mTextView;
    private String location = "Alameda";
    private int randCount = 0;
    private int count = 0;

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

        /* Shake. */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                count++;
                if(count > 2) {
                    Log.d("SHAKE", "I am shaking :O");
                    Toast.makeText(VoteViewActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                    String[] locations = {"Berkeley", "Los Angeles", "San Fransisco", "Baltimore"};
                    Intent intentChangeLocation = new Intent(getBaseContext(), WatchToPhoneService.class);
//                    int index = ((randCount++) % 4);
                    Random random = new Random();
                    int value = random.nextInt(((3 - 0) + 1) + 0) % 4;
                    location = locations[value];
                    intentChangeLocation.putExtra("LOCATION", location);
                    startService(intentChangeLocation);

                    Intent newVoteView = new Intent(VoteViewActivity.this, VoteViewActivity.class);
                    newVoteView.putExtra("LOCATION", location);
                    startActivity(newVoteView);
                }
            }
        });
        count++;
    }

    public void loadData() {
        TextView resultView = (TextView) findViewById(R.id.vote_results);
        resultView.setText(" 30 %      70 %");
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
