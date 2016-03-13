package com.example.ashleythemagnificant.prog02;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AshleyTheMagnificant on 2/27/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static String[] mDataset;
    private static int pos;
    private static final String TWITTER_KEY = "FMfuUSZMSGGL4KsE4F0dvuSs7";
    private static final String TWITTER_SECRET = "sO45oxSiMcIqIqFoMTcHzcI3DhvBQUn9zqdbRk0QHJv8pSOk7u";

    Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    Context context_;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LinearLayout layout;
        public View mTextView;
        public ImageView imageView;
        public TextView tweetView;
        public TextView nameV;
        public TextView descriptionView;
        private Context context_;

        public ViewHolder(final View v, Context context){ //}, ImageView iv, TextView tv) {
            super(v);
            mTextView= v;
            context_ = context;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();
                    Log.d("Adapter", "Pos in list = " + i);
                    String data = mDataset[i];
                    //.split("___");
////                    String fields = bioguide_idId[0] + title[1] + website[2] + email[3] + party[4] + name[5] + twitter_id[6] + office_Term[7];
//                    String mfields = data[0] + data[1]
//                                + data[4] + data[5] + data[6] + data[7];

                    /* Mobile Portion. */
                    Intent intent = new Intent(v.getContext(), DetailedView.class);
                    Log.d("T", "Message = " + data);
                    intent.putExtra("SELECTION", data); //  TODO
                    v.getContext().startActivity(intent);

////                    /* Wear */
////                    String wfields = data[5] + "___" + data[4];
//                    Intent wearIntent = new Intent(v.getContext(), PhoneToWatchService.class);
//                    wearIntent.putExtra("SELECTION", mDataset[i]); //  TODO
//                    Log.i("T", "Adapter Starting wear activity");
//                    v.getContext().startService(wearIntent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view this is a
//        This is a linear layout
        context_ = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        ViewHolder vh = new ViewHolder(v, parent.getContext());//, iv, tv);
        vh.tweetView = (TextView) v.findViewById(R.id.tweet);
//        vh.nameV = (TextView) v.findViewById(R.id.card_name);
        vh.descriptionView = (TextView) v.findViewById(R.id.info_text);
        vh.imageView = (ImageView) v.findViewById(R.id.profile_image);

        return vh;

    }

//     Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String, String> parties = new HashMap<>();
        parties.put("D", "Democrat");
        parties.put("R", "Republican");
        parties.put("I", "Independent");
        String[] rep = mDataset[position].split("___");
//        String fields = bioguide_idId[0] + title[1] + website[2] + email[3] + party[4] + name[5] + twitter_id[6] + office_Term[7];
        holder.descriptionView.setText(rep[5] + "\n" + parties.get(rep[4]) + "\n"
                                        + rep[2] + "\n" + rep[3]
                                        + "\n" + "Tap for more");
        final String id = rep[6];

        /* Tweet Stuff */
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context_, new Twitter(authConfig));
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
// Can also use Twitter directly: Twitter.getApiClient()
//        StatusesService statusesService = twitterApiClient.getStatusesService();
//        SearchService searchService = twitterApiClient.getSearchService();

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, id, 1, null, null, false,
                        false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        String url_t = listResult.data.get(0).user.profileImageUrl;
                        Log.d("TWITTER URL", "url = " + url_t);
                        for (Tweet tweet  : listResult.data) {
//                            Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                        holder.tweetView.setText("Recent Tweet \n @" + id + "\n" + tweet.text.toString());
                        Picasso.with(holder.context_)
                                .load(url_t)
                                .into(holder.imageView);
                        }
                        Log.d("DATA", "before = " + mDataset[position]);
                        mDataset[position] += "___" + url_t;
                        Log.d("DATA", "After = " + mDataset[position]);
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

            String imageUrl_i = "http://i.imgur.com/jxHaCxu.jpg";
            String imageUrl = "http://github.com/unitedstates/images/blob/gh-pages/congress/225x275/C001097.jpg";
        Picasso.with(holder.context_)
                .load(imageUrl)
                .into(holder.imageView);

        }

                // Return the size of your dataset (invoked by the layout manager)
        @Override
    public int getItemCount() {
        return mDataset.length;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            Log.d("Imge", "Setting image view");
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.d("Img", "Image thing worked");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

