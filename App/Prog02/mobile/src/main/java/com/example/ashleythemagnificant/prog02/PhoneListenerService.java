package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public class PhoneListenerService extends WearableListenerService  {
    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("BUGG", "Message received on the phone :D");
        Intent intent = new Intent(this, DetailedView.class);
        String path_to_person = "/selection";
        intent.putExtra("SELECTION",path_to_person);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("BUGG", "in PhoneListenerService, got: " + messageEvent.getPath());
        /* start Display representative activity. This is where you do the screen stuff I guess. */
        startActivity(intent);
//        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {
//
//            // Value contains the String we sent over in WatchToPhoneService, "good job"
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//
//            // Make a toast with the String
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, value, duration);
//            toast.show();

        // so you may notice this crashes the phone because it's
        //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
        // replace sending a toast with, like, starting a new activity or something.
        // who said skeleton code is untouchable? #breakCSconceptions

//        } else {
//            super.onMessageReceived( messageEvent );
//        }

    }
}
