package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class DetailedView extends AppCompatActivity {
    private String selectedRepresentative;

    private Intent intent;
    private HashMap<String, String> data = new HashMap<String, String>();
    private HashMap<String, String> parties = new HashMap<>();
    private HashMap<String, String> titles = new HashMap<>();
    private Bitmap bitmap;
    private ImageView i;
    private static final String TWITTER_KEY = "FMfuUSZMSGGL4KsE4F0dvuSs7";
    private static final String TWITTER_SECRET = "sO45oxSiMcIqIqFoMTcHzcI3DhvBQUn9zqdbRk0QHJv8pSOk7u";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        while (intent == null) {
            intent = getIntent();
        }
        String message = intent.getStringExtra("SELECTION");
        //bioguide_idId[0] + title[1] + website[2] + email[3] + party[4] + name[5] + twitter_id[6] + office_Term[7];
//        String mfields = data[0] + data[1]
//                + data[2] + data[5] + data[6] + data[7];
        String[] fields = message.split("___");

        parties.put("D", "Democrat");
        parties.put("R", "Republican");
        parties.put("I", "Independent");

//         "Sen", "Rep", "Del", or "Com".
        titles.put("Sen", "Senator");
        titles.put("Rep", "Representative");
        titles.put("Del", "Delegate");
        titles.put("Com", "Commerce");

        selectedRepresentative = fields[5];
        Log.d("DETAILED", selectedRepresentative + " chosen");
        /* load data into HashMap. */
        data.put("image", "http://static2.politico.com/dims4/default/a08c745/2147483647/thumbnail/403x218%3E/quality/90/?url=http%3A%2F%2Fs3-origin-images.politico.com%2F2015%2F07%2F30%2F150730_barbara_lee_gty_1160.jpg"); // TODO get image
        data.put("party", parties.get(fields[4]));
        data.put("office_term", fields[7]);
        data.put("title", titles.get(fields[1]));
        new getData("bills").execute("bills?sponsor_id=" + fields[0]);
        new getData("committee").execute("committees?member_ids=" + fields[0]);

        loadImage(fields[6]);
        displayData();
    }

    public void loadImage(final String t_id) {
        /* Tweet Stuff */
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getBaseContext(), new Twitter(authConfig));
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
// Can also use Twitter directly: Twitter.getApiClient()
//        StatusesService statusesService = twitterApiClient.getStatusesService();
//        SearchService searchService = twitterApiClient.getSearchService();

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, t_id, 1, null, null, false,
                        false, false, true, new Callback<List<Tweet>>() {
                            @Override
                            public void success(Result<List<Tweet>> listResult) {
                                String url_t = listResult.data.get(0).user.profileImageUrl;
                                Log.d("TWITTER URL", "url = " + url_t);
                                for (Tweet tweet  : listResult.data) {
//                            Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                                    Picasso.with(getBaseContext())
                                            .load(url_t)
                                            .into((ImageView)findViewById(R.id.image));
                                }
                            }

                            @Override
                            public void failure(TwitterException e) {
                                e.printStackTrace();
                            }
                        });
//                twitterApiClient.getStatusesService().show(id, true, false, false, new Callback<List<T>>() {
//                    @Override
//                    public void success(Result<List<Tweet>> listResult) {}
//                    @Override
//                    public void failure(TwitterException e) {
//                        e.printStackTrace();
//                    }
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }

        });
//
//        Log.d("IMAGE URL", url);
//        Picasso.with(getBaseContext())
//                .load(url)
//                .into((ImageView)findViewById(R.id.image));
    }

    public void displayData() {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(data.get("title"));

        TextView nameView = (TextView) findViewById(R.id.rep_name);
        nameView.setText(selectedRepresentative);

        TextView partyView = (TextView) findViewById(R.id.party);
        partyView.setText(data.get("party"));

        TextView termView = (TextView) findViewById(R.id.office_term);
        String[] terms = data.get("office_term").split(" ");
        String date1 = formatDate(terms[0]);
        String date2 = formatDate(terms[1]);
//        DateFormat sdf = new DateFormat("MM-dd-yyyy");
        String message = "Office Term: " + date1 + " - " + date2; //+ sdf.format(new Date(terms[0])) + " - " +  sdf.format(new Date(terms[1]));
        termView.setText(message);

    }

    public String formatDate(String data) {
        String[] parts = data.split("-"); //
        String strCurrentDate = parts[0] + " " + parts[1] + " " + parts[2]; //"Wed, 18 Apr 2012 07:55:29 +0000";
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
        try {
            Date newDate = format.parse(strCurrentDate);

            format = new SimpleDateFormat("MMM dd, yyyy");
            String date = format.format(newDate);
            return date;
        }catch (Exception e) {

        }
        return data;
    }

    /*  Takes in biID .*/
    private class getData extends AsyncTask<String, Void, String> {
        private final String API_KEY = "b91f4bfe5c06450fb0d64d415253c1d1";
        private String type;
        public getData(String t) {
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
                    for (int i = 0; i < numResults && i < 3; i++) {
                        JSONObject bill = (JSONObject)jresults.get(i);
                        String title = bill.getString("short_title");
                        if (title == null) {
                            title = bill.getString("official_title");
                        }
                        if (i == 2) {
                            bills += title;
                        } else {
                            bills += title + "\n";
                        }
                    }

                    TextView billView = (TextView) findViewById(R.id.sponsored_bills);
                    String bill_text = "Sponsored Bills: \n" + bills;
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
