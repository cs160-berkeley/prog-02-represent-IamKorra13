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

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private TextView mTextView;
    private int randCount = 0;
    private int count = 0;

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

        /* Shake. */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                count++;
                if (count >2) {
                    Log.d("SHAKE", "I am shaking :O");
                    Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                    String[] locations = {"Berkeley", "Los Angeles", "San Fransisco", "Baltimore"};
                    Intent intentChangeLocation = new Intent(getBaseContext(), WatchToPhoneService.class);
                    int index = ((randCount++) % 4);
                    intentChangeLocation.putExtra("LOCATION", locations[index]);
                    startService(intentChangeLocation);
                }
            }
        });
        count++;
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
