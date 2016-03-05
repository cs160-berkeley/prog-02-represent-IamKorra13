package com.example.ashleythemagnificant.prog02;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    private final Service _this = this;
    private String value = "null";
    private String location = "null";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        /* and actually connect it */
        mWatchApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /* Extra .*/
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.i("BUGG", "The intent is not null ^^^%%%%%");
            Bundle extras = intent.getExtras();
            if (intent.hasExtra("SELECTION")) {
                value = extras.getString("SELECTION");
            }
            if (intent.hasExtra("LOCATION")) {
                location = extras.getString("LOCATION");
            }
            // Send the message with the cat name
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mWatchApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    Log.d("NEVER", "This never sends");
                    sendMessage("/selection", value);
                }
            }).start();

            return START_STICKY;
        } else {
            return START_STICKY;
        }

    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
//        final String value = bundle.getString("SELECTION");
        Log.i("BUGGGGGGGGGG", "IM CONNECTED ##$#@#$%@$%$#");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("T", "found nodes");
                        //when we find a connected node, we populate the list declared above
                        if (!location.equals("null")) {
                            sendMessage("/location", location);
                            _this.stopSelf();
                        } else {
                            sendMessage("/selection", value);
                            _this.stopSelf();
                        }
                    }
                });
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }
}

