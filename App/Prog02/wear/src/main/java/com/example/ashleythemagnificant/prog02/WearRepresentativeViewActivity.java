package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;

public class WearRepresentativeViewActivity extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = { "Barbara Lee \n Democrat", "Loni Hancock \n Democrat",
                            "Mark Leno \n Democrat "}; //TODO change the data struct and set
    String location = "Need to set";
    private Intent intentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_representative_view);
        WearableListView listView =
        (WearableListView) findViewById(R.id.wearable_list);
        listView.setAdapter(new Adapter(this, elements));
//        listView.setClickListener(this);
        intentMain = getIntent();
        while (intentMain == null) {
            intentMain = getIntent();
        }
        location = intentMain.getStringExtra("SELECTION").split("-")[1];

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
        voteIntent.putExtra("LOCATION", location);
        Log.d("LOC", location);
        startActivity(voteIntent);
        }

    @Override
    public void onTopEmptyRegionClick() {
    }

    public void startVoteView(View v) {
        Intent intent = new Intent(getBaseContext(), VoteViewActivity.class);
        intent.putExtra("LOCATION", location);
        Log.d("LOC", location);
        startActivity(intent);
    }
}




