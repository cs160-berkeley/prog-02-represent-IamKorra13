package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class DetailedView extends AppCompatActivity {
    private String selectedRepresentative;

    private Intent intent;
    private HashMap<String, String> data = new HashMap<String, String>();
    private Bitmap bitmap;
    private ImageView i;
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
        selectedRepresentative = message;
        Log.d("DETAILED", selectedRepresentative + " chosen");
        /* load data into HashMap. */
        data.put("image", "http://static2.politico.com/dims4/default/a08c745/2147483647/thumbnail/403x218%3E/quality/90/?url=http%3A%2F%2Fs3-origin-images.politico.com%2F2015%2F07%2F30%2F150730_barbara_lee_gty_1160.jpg");
        data.put("party", "Democrat");
        data.put("office_term", "2/2/2002_2/2/2017");
        data.put("committee_name", "Cool Kid Committee");
        data.put("sponsored_bills", "Food Assistance to Improve Reintegration Act of 2013_Repeal of the Authorization for Use of Military Force_Income Equity Act of 2013");
        data.put("title", "Representative");
        loadImage();
        displayData();
    }

    public void loadImage() {
//   TODO API call for this persons name to get all the data and actually load the url image :/
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ImageView i = (ImageView) findViewById(R.id.image);
//                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(data.get("image")).getContent());
//                    i.setImageBitmap(bitmap);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


//        Thread th = new Thread(new Runnable() {
//            public void run() {
//
//                URL url = null;
//                InputStream content = null;
//                try {
//                    i = (ImageView) findViewById(R.id.image);
//                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(data.get("image")).getContent());
//
////                    url = new URL((data.get(position).getThumbnail()));
//
////                    content = (InputStream) url.getContent();
////                    Drawable d = Drawable.createFromStream(content, "src");
////                    final Bitmap mIcon1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////                    ;
//
//                    Handler handler = new Handler(Looper.getMainLooper()) { .post(new Runnable() {
//
//                        public void run() {
//                            i.setImageBitmap(bitmap);
//
//                            //here you can do everything in UI thread, like put the icon in a imageVew
//                        }
//                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        th.start();
}

    public void displayData() {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(data.get("title"));

        TextView nameView = (TextView) findViewById(R.id.rep_name);
        nameView.setText(selectedRepresentative);

        TextView partyView = (TextView) findViewById(R.id.party);
        partyView.setText(data.get("party"));

        TextView termView = (TextView) findViewById(R.id.office_term);
        String[] terms = data.get("office_term").split("_");
        String message = "Office Term: " +terms[0] + "\n" + terms[1];
        termView.setText(message);

        TextView committeeView = (TextView) findViewById(R.id.committee);
        String comm = "Serves on " + data.get("committee_name");
        committeeView.setText(comm);

        String[] bills = data.get("sponsored_bills").split("_");
        TextView billView = (TextView) findViewById(R.id.sponsored_bills);
        String bill_text = "Sponsored Bills: \n" + bills[0] + "\n" + bills[1] + "\n" + bills[2];
        billView.setText(bill_text);

    }


}
