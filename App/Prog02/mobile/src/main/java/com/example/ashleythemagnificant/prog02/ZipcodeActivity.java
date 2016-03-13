package com.example.ashleythemagnificant.prog02;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class ZipcodeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String tempData2;
    public String location;// = "Alemeda";
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private String latitude;
    private String longitude;
    private String votes;
    private loadData loader;
    private Context context_;
    private String t_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context_ = getBaseContext();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("CONN", "I am connected");
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                googleApiClient);
                        if (mLastLocation != null) {
                            Log.d("LOCATION", "I have a location");
                            latitude = String.valueOf(mLastLocation.getLatitude());
                            longitude = String.valueOf(mLastLocation.getLongitude());
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
        googleApiClient.connect();
    }

//    public void setLocation(String loc) {
//        location = loc;
//    }

    /* This will gove me cards so that the Representative class can just display
    the number of cards that these two functions return. It puts data into a data set but
     accesses the data in different ways hence the use of 2 different functions. */

    public void findRepresentativesZipcode(View view) {
        EditText editText_zip = (EditText) findViewById(R.id.zipcode_input);
        //        intent.putExtra("ZIPCODE", editText_zip.getText().toString());
        String input = editText_zip.getText().toString();
        int length_of_input = input.length();
        boolean isValidInput = true;
        if (length_of_input != 5) {
            Log.d("ERROR", "Incorrect Zipcode Length");
            // TODO give actual Error message to view
        } else {
            try {
                int i = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Log.d("Error", "Incorrect Zipcode Input");
                // TODO add actual error message to the screen
                isValidInput = false;
            }
            if (isValidInput) {
                new loadData("zip_loc").execute("address=" + input);
                new loadData("vote").execute("");
                new loadData("final").execute("zip="+input);
            }
        }
    }


    public void findRepresentativesLocation(View view) {
        Log.d("LOCATION", "Lon = " + longitude + "Lat = " + latitude);
        new loadData("loc").execute("latlng=" + latitude + "," + longitude);
        new loadData("vote").execute("");
        new loadData("final").execute("latitude="+latitude+"&longitude="+longitude);
    }

    private class loadData extends AsyncTask<String, Void, String> {
        private final String API_KEY = "b91f4bfe5c06450fb0d64d415253c1d1";
        private final String G_KEY = "AIzaSyA67ovt3HlCI9PGlv06hNS2FZkG5LwDPNs";//"AIzaSyBTonN_O-U4tXPkHetP7YnTLnl3avxeRwE";
//        "http://maps.googleapis.com/maps/api/geocode/json?address=77379&sensor=true"
        private static final String TWITTER_KEY = "FMfuUSZMSGGL4KsE4F0dvuSs7";
        private static final String TWITTER_SECRET = "sO45oxSiMcIqIqFoMTcHzcI3DhvBQUn9zqdbRk0QHJv8pSOk7u";

        private String type;
        private String conditions;

        public loadData(String t) {
            type = t;
        }
        @Override
        protected String doInBackground(String... params) {
            Intent intent = new Intent(ZipcodeActivity.this, RepresentativeView.class);

            conditions = params[0];
            String result = "";

            String responseString = null;
            if (type.equals("loc") || type.equals("zip_loc")) {
                String url = "https://maps.googleapis.com/maps/api/geocode/json?"
                        + conditions + "&key=" + G_KEY; //latlng=40.714224,-73.961452;
                Log.d("JSON", url);

                try {
                    InputStream input = new URL(url).openStream();
//                    Log.d("JSON", "Input Stream input");

                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
//                    Log.d("JSON", "After the input stream");
                    StringBuilder sb = new StringBuilder();
//                    Log.d("JSON", "Bulding the string");
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.e("JSON", "Error in http connection " + e.toString());
                }
            } else if (type.equals("vote")) {
                 String json = null;
                    try {

                        InputStream is = getAssets().open("election-county-2012.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        json = new String(buffer, "UTF-8");

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                    return json;

        } else {
                try {

                    String url = "http://congress.api.sunlightfoundation.com/legislators/locate?"
                            + conditions + "&apikey=" + API_KEY;

                    Log.d("JSON", url);
                    InputStream input = new URL(url).openStream();
//                    Log.d("JSON", "Input Stream input");

                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
//                    Log.d("JSON", "After the input stream");
                    StringBuilder sb = new StringBuilder();
//                    Log.d("JSON", "Bulding the string");
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.e("JSON", "Error in http connection " + e.toString());
                }
                return result;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(ZipcodeActivity.this, RepresentativeView.class);
//            Log.d("ASYNC", "The Result = " + result);
            if (type.equals("loc") || type.equals("zip_loc")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jresults = jsonObject.getJSONArray("results");

                    Log.d("LOC", "" + jresults);
                    JSONObject v1 = (JSONObject) jresults.get(0);
//                    address_comp
//                    Log.d("LOCA", " Add Comp  = " + v1);
                    JSONArray address_comp = (JSONArray) v1.get("address_components");
                    JSONObject sObj = null;
                    JSONObject cObj = null;
                    for (int i = 0; i < address_comp.length(); i++) {
                        JSONObject curObj = (JSONObject)address_comp.get(i);
                        Log.d("JSON", "curObj = " + curObj);
                        String curType = (String) curObj.getJSONArray("types").get(0);
                        Log.d("JSON", "curType String = " + curType);
                        if (curType.equals("administrative_area_level_2")) {
                            cObj = curObj;
                        } else if (curType.equals("administrative_area_level_1")) {
                            sObj = curObj;
                        }
                    }
                    String county = cObj.getString("short_name");
                    String state = sObj.getString("short_name");
                    location = county + "___" + state;
                    Log.d("ZIPCODE", "COUNTy State " + location);

                } catch (Exception e) {
                    Log.e("LOC", "call error : " + e);
                }

            } else if (type.equals("vote")) {
                String obama_vote = "";
                String romney_vote = "";
                Log.d("VOTE", location);
                Log.d("Location", location);
                String state = location.split("___")[1];
                String county = location.split("___")[0].replace(" County", "");//split(" ")[0];
                Log.d("HELLO?", "%%%%%%%%%%%%%%%%%%%%%%% FOUNd : " + county + state +" what");
                try {
//                    Log.d("WAP", "I AM EGERE");
                    JSONArray arr = new JSONArray(result);
                    for (int i = 0 ; i < arr.length(); i++) {
                        JSONObject currObject = arr.getJSONObject(i);
                        String curC = currObject.getString("county-name");
                        String curS = currObject.getString("state-postal");


                        if (curC.equals(county) && curS.equals(state)) {
                            obama_vote = currObject.getString("obama-percentage");
                            romney_vote = currObject.getString("romney-percentage");
                            votes = obama_vote + "%%%" + romney_vote;
//                            Log.d("VOTe", "Votes O and R : " + votes);
                            break;
                        }
                    }
                } catch (Exception e) {

                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int numResults = jsonObject.getInt("count");
                    JSONArray jresults = jsonObject.getJSONArray("results");

                    /* Each person seperated by ... */
                    String selection = "";
                    for (int i = 0; i < numResults; i++) {
                        JSONObject person = (JSONObject) jresults.get(i);
                        //Bio Id
                        String bioguide_idId = person.getString("bioguide_id") + "___";
                        //Title
                        String title = person.getString("title") + "___";
                        // Website
                        String website = person.getString("website") + "___";
                        // Email
                        String email = person.getString("oc_email") + "___";
                        // Party
                        String party = person.getString("party") + "___";
                        // Full name
                        String first_name = person.getString("first_name") + " ";
                        String last_name = person.getString("last_name") + "___";
                        // Twitter Stuff
                        String twitter_id = person.getString("twitter_id") + "___";

                        // Office term
                        String office_term = person.getString("term_start") + " " + person.getString("term_end");

                        /* Fields seperated by ___ */
                        String url = "___" + getUrl(person.getString("twitter_id"));
                        String fields = bioguide_idId + title + website + email + party + first_name + last_name + twitter_id + office_term;// + url;

                        if (i == numResults - 1) { // dont put the ...
                            selection += fields;
                        } else {
                            selection += fields + "#";
                        }
                    }
                    intent.putExtra("SELECTION", selection);
                    Log.d("JSON", "putting extra :" + selection);
                    startActivity(intent);


                    // TODO also start WatchService
                    Log.i("BUGG", "Starting Watch from Zipcode-Location");
                    Intent watchSplashIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    String message = location + "@@@" + votes + "@@@" + selection;
                    Log.d("SELECTION", message);
                    watchSplashIntent.putExtra("SELECTION", message);
                    Log.d("LOCATION", "location saved : " + location);
                    startService(watchSplashIntent);
                } catch (JSONException e) {
                    Log.e("JSON", "Something went wrong: " + e);
                }
            }
        }

        public String getUrl(final String twitter_id) {
             /* Tweet Stuff */
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(context_, new Twitter(authConfig));
            Log.d("TWITTER", "Twitter Id = " + twitter_id);
            TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                @Override
                public void success(Result<AppSession> appSessionResult) {
                    AppSession session = appSessionResult.data;
                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                    twitterApiClient.getStatusesService().userTimeline(null, twitter_id, 1, null, null, false,
                            false, false, true, new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> listResult) {
                                    t_url = listResult.data.get(0).user.profileImageUrl;
                                    Log.d("Twit URL", t_url);
//                                        Picasso.with(holder.context_)
//                                                .load(url_t)
//                                                .into(holder.imageView);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    e.printStackTrace();
                                }
                            });
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                }

            });
            return t_url;
        }
        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }






    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}
}
