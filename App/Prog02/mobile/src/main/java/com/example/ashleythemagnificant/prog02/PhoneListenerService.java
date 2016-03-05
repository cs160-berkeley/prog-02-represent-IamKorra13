package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public class PhoneListenerService extends WearableListenerService  {
    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";
    private static final String LOCATION = "/location";
    private static final String SELECTION = "/selection";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase(SELECTION) ) {
            Log.i("BUGG", "Message received on the phone :D");
            Log.d("BUGG", "in PhoneListenerService, got: " + messageEvent.getPath());

            Intent intent = new Intent(this, DetailedView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Log.d("T", "Value =" + value);
            intent.putExtra("SELECTION", value);

            /* start Display representative activity. This is where you do the screen stuff I guess. */
            startActivity(intent);
        } else if ( messageEvent.getPath().equalsIgnoreCase(LOCATION) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String location = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent intent = new Intent(this, RepresentativeView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            intent.putExtra("LOCATION", value);
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }
    }
}
