package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WearRepresentativeViewActivity extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private int randCount = 0;
    private String location = "Need to set";
    private String elements = "";
    private Intent intentMain;
    private String votes;
    private boolean onStart = true;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_representative_view);
        WearableListView listView =
                (WearableListView) findViewById(R.id.wearable_list);

        intentMain = getIntent();
        while (intentMain == null) {
            intentMain = getIntent();
        }
        if (intentMain.hasExtra("LOCATION")) {
            location = intentMain.getStringExtra("LOCATION");
        } else {
//            // type + cond   ; selection
            elements = intentMain.getStringExtra("SELECTION");//.split("^^^");

        }
//        String elements = location + "@Barbara Lee \n Democrat-" + "Loni Hancock \n Democrat-" +
//                "Mark Leno \n Democrat ";
//        String[] elements = { location, "Barbara Lee \n Democrat", "Loni Hancock \n Democrat",
//                "Mark Leno \n Democrat "}; //TODO change the data struct and set

        Log.d("WATCH ", "Elements " + elements);
        listView.setAdapter(new Adapter(this, elements));
//        listView.setClickListener(this); TODO change lol this is pretty cool

        /* Shake. */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                count++;
                if (count > 2) {
                    Log.d("SHAKE", "I am shaking :O");
                    Toast.makeText(WearRepresentativeViewActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
//                    new loadData().execute();


                    /**/

                    /**/
//                    String[] locations = {"Berkeley", "Los Angeles", "San Fransisco", "Baltimore"};
//                    Intent intentChangeLocation = new Intent(getBaseContext(), WatchToPhoneService.class);
//                    Random random = new Random();
//                    int value = random.nextInt(((3 - 0) + 1) + 0) % 4;
//                    location = locations[value];
//                    Log.d("Location", "Location index = " + value + "   =   " + location);
//                    intentChangeLocation.putExtra("LOCATION", "shake"); // do api calls in Mobile
//                    startService(intentChangeLocation);
////
//                    Intent newVoteView = new Intent(WearRepresentativeViewActivity.this, WearRepresentativeViewActivity.class);
//                    newVoteView.putExtra("LOCATION", location);
//                    startActivity(newVoteView);
                }
            }
        });
        count++;
        Log.d("SHAKE", "Count = " + count);
    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        // use this data to complete some action ...


        Log.i("BUGG", "Selected a listed watch thing");
        // Start WatchtoPhoneService
        // viewholder.getText to see which person was selected.
        //then send the name to the phone. Things need to be sorted hashmap {name: everythingString}
        Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        sendIntent.putExtra("SELECTION", "Barbara Lee was selected from Watch");
        startService(sendIntent);

        Intent voteIntent = new Intent(getBaseContext(), VoteViewActivity.class);
        String selection = location + "###" + votes;
        voteIntent.putExtra("LOCATION", selection);
        Log.d("WATCH", selection);
        startActivity(voteIntent);
    }



    @Override
    public void onTopEmptyRegionClick() {
    }

    /* Shake part 3. */
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

    public void startVoteView(View v) {
        Intent intent = new Intent(getBaseContext(), VoteViewActivity.class);
        intent.putExtra("LOCATION", location);
        Log.d("LOC", location);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}




