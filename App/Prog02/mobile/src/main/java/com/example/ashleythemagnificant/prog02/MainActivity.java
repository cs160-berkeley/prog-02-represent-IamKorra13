package com.example.ashleythemagnificant.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


public class MainActivity extends Activity {
    public ImageView imageView;
    private static final String TWITTER_KEY = "FMfuUSZMSGGL4KsE4F0dvuSs7";
    private static final String TWITTER_SECRET = "sO45oxSiMcIqIqFoMTcHzcI3DhvBQUn9zqdbRk0QHJv8pSOk7u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String imageUrl = "http://github.com/unitedstates/images/blob/gh-pages/congress/450x550/A000014.jpg";//"https://github.com/unitedstates/images/blob/gh-pages/congress/225x275/A000014.jpg";//"https://github.com/unitedstates/images/blob/gh-pages/congress/original/A000014.jpg";
        final String imageUrl_i = "http://i.imgur.com/jxHaCxu.jpg";
        imageView = (ImageView) findViewById(R.id.test_image);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
//
//        // TODO: Use a more specific parent
//        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
//        // TODO: Base this Tweet ID on some data from elsewhere in your app
//        long tweetId = 631879971628183552L;
//        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                TweetView tweetView = new TweetView(MainActivity.this, result.data);
//                parentView.addView(tweetView);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("TwitterKit", "Load Tweet failure", exception);
//            }
//        });

//        /*rgrwegrgwr*/
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(MainActivity.this, ZipcodeActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
