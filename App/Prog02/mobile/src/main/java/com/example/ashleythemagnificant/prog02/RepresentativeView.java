package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import io.fabric.sdk.android.Fabric;


public class RepresentativeView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset;
    private Intent intent;
    private TextView numResultsView;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "FMfuUSZMSGGL4KsE4F0dvuSs7";
    private static final String TWITTER_SECRET = "sO45oxSiMcIqIqFoMTcHzcI3DhvBQUn9zqdbRk0QHJv8pSOk7u";

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Twitter stuff */
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

//        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
//            @Override
//            public void success(Result<AppSession> appSessionResult) {
//                AppSession session = appSessionResult.data;
//                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
//                twitterApiClient.getStatusesService().userTimeline(null, "elonmusk", 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
//                    @Override
//                    public void success(Result<List<Tweet>> listResult) {
//                        for(Tweet tweet: listResult.data) {
//                            Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
//                        }
//                    }
//                    @Override
//                    public void failure(TwitterException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//            @Override
//            public void failure(TwitterException e) {
//                e.printStackTrace();
//            }
//        });

        setContentView(R.layout.activity_representitive_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        intent = getIntent();
        while (intent == null) {
            intent = getIntent();
        }
        displayRepresentatives();
    }

    public void displayRepresentatives() {
        Log.d("Rep", "In rep");
        if (intent.hasExtra("SELECTION")) {
            Log.d("Rep", "HAs selection");
            String data = intent.getStringExtra("SELECTION");
            myDataset = convertStringToDataArray(data);
            Log.d("Data", myDataset[0]);
            int numResults = myDataset.length;
            numResultsView = (TextView) findViewById(R.id.representative_results);
            numResultsView.setText("Number of results: " + Integer.toString(numResults));
//
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
//        } else if (intent.hasExtra("LOCATION")) {
//            //TODO need to reload the data poss in diff Thread
//            myDataset = convertStringToDataArray(tempData);
//        } else { // SELECTION
//            myDataset = convertStringToDataArray(tempData);
        }
        if (intent.hasExtra("INDEX")) {
            String i = intent.getStringExtra("INDEX");
            int index = Integer.parseInt(i);
            String selection = myDataset[index];
            Log.d("SELECTION", "From watch to Phone: index " + index + "  " + selection );
            Intent intent = new Intent(this, DetailedView.class);
            intent.putExtra("SELECTION", selection);
            startActivity(intent);
        }
    }

    public String[] convertStringToDataArray(String data) {
        Log.d("Convert", "Converting string to data set" + data);
        String[] dataset = data.split("#");
        Log.d("data", "Length of data set" + dataset.length);

        return dataset;
    }

    /*  Takes in biID .*/
    private class getTweets extends AsyncTask<String, Void, String> {
        private final String API_KEY = "b91f4bfe5c06450fb0d64d415253c1d1";
        private String type;
        public getTweets(String t) {
            type = t;
        }
        @Override
        protected String doInBackground(String... params) {
            String conditions = params[0];
            String result = "";
            String responseString = null;
            try {

                String url = "http://congress.api.sunlightfoundation.com/"
                        +conditions+"&apikey=" + API_KEY;

                Log.d("JSON", url);
                InputStream input = new URL(url).openStream();
                Log.d("JSON", "Input Stream input" );

                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
                Log.d("JSON", "After the input stream");
                StringBuilder sb = new StringBuilder();
                Log.d("JSON", "Bulding the string");
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            }
            catch (Exception e) {
                Log.e("JSON", "Error in http connection "+e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (type.equals("bills")) {
                try {
                    String bills = "";
                    JSONObject jsonObject = new JSONObject(result);
                    int numResults = jsonObject.getInt("count");
                    JSONArray jresults = jsonObject.getJSONArray("results");
                    int i = 0;
                    while (i< 3) {
                        JSONObject bill = (JSONObject)jresults.get(i);
                        String title = bill.getString("short_title");
                        if (title == null) {
                            title = bill.getString("official_title");
                            i++;
                        }
                        if (title == null) {
                            i--;
                        }
                        if (i == 2) {
                            bills += title;
                            i++;
                        } else {
                            bills += title + "\n";
                            i++;
                        }
                    }
//                    for (int i = 0; i < numResults && i < 3; i++) {
//                        JSONObject bill = (JSONObject)jresults.get(i);
//                        String title = bill.getString("short_title");
//                        if (title == null) {
//                            title = bill.getString("official_title");
//                        }
//                        if (i == 2) {
//                            bills += title;
//                        } else {
//                            bills += title + "\n";
//                        }
//                    }

                    TextView billView = (TextView) findViewById(R.id.sponsored_bills);
                    String bill_text = "Sponsored Bills: " + bills;
                    billView.setText(bill_text);

                    Log.d("BILLS", "bills = " + bills);
                } catch (JSONException e) {
                    Log.e("JSON", "Something went wrong: " + e);
                }
            } else { // committee name
                try {
                    String bills = "";
                    JSONObject jsonObject = new JSONObject(result);
                    int numResults = jsonObject.getInt("count");
                    JSONArray jresults = jsonObject.getJSONArray("results");
                    JSONObject committee = (JSONObject) jresults.get(0);
                    String name = committee.getString("name");


                    TextView committeeView = (TextView) findViewById(R.id.committee);
                    String comm = "Serves on the " + name ;
                    committeeView.setText(comm);
                    Log.d("COMMITTEE", name);

                } catch (JSONException e) {
                    Log.e("JSON", "Something went wrong: " + e);
                }
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
