package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ZipcodeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[][] tempData = new String[3][6];
    private String tempData2;
    private String[] rep1;
    private String location = "Alemeda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Temp Data loading
        rep1 = new String[6];
        String[] rep2 = new String[6];
        String[] rep3 = new String[6];
        rep1[0] = "Barbara Lee"; rep1[1] = "Democrat"; rep1[2] = "Website"; rep1[3] = "Email"; rep1[4] = "Tweet"; rep1[5] = "link to image";
        rep2[0] = "Loni Hancock"; rep2[1] = "Democrat"; rep2[2] = "Website"; rep2[3] = "Email"; rep2[4] = "Tweet"; rep2[5] = "link to image";
        rep3[0] = "Mark Leno"; rep3[1] = "Democrat"; rep3[2] = "Website"; rep3[3] = "Email"; rep3[4] = "Tweet"; rep3[5] = "link to image";

        tempData[0] = rep1;
        tempData[1] = rep2;
        tempData[2] = rep3;

        tempData2 = "Barbara Lee_Democrat_Website_Email_Tweet_https://s3.amazonaws.com/givegreen-cdn/2014/05/Lee-pic-150x150.jpeg#"
        + "Loni Hancock_Democrat_Website_Email_Tweet_http://postnewsgroup.com/wp-content/uploads/2014/02/LoniHancock-1.jpg#" + "Mark Leno_Democrat_Website_Email_Tweet_http://www.californiaprogressreport.com/Mark-Leno.jpg";
    }

    /* This will gove me cards so that the Representative class can just display
    the number of cards that these two functions return. It puts data into a data set but
     accesses the data in different ways hence the use of 2 different functions. */

    public void findRepresentativesZipcode(View view) {
        Intent intent = new Intent(this, RepresentativeView.class);
        if (intent != null) {
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
                    intent.putExtra("RESULT", input);
                    intent.putExtra("DATA", tempData2);
                    startActivity(intent);

                    Log.i("BUGG", "Starting Watch from Zipcode-zipcode");
                 /* Start SmartWatch service */
                    Intent watchSplashIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    watchSplashIntent.putExtra("SELECTION", "From Zipcode-"+location);
                    startService(watchSplashIntent);
                }
            }
        }
    }

    public void findRepresentativesLocation(View view) {
        Intent intent = new Intent(this, RepresentativeView.class);
        if (intent != null) {
            EditText editText_location = (EditText) findViewById(R.id.zipcode_location);
            //        intent.putExtra("LOCATION", editText_location.getText().toString());
            location = editText_location.getText().toString();
            intent.putExtra("RESULT", location);
            intent.putExtra("DATA", tempData2);
            startActivity(intent);

            /* Start SmartWatch service */
            Log.i("BUGG", "Starting Watch from Zipcode-Location");
            Intent watchSplashIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            watchSplashIntent.putExtra("SELECTION", "From Location-" + location);
            startService(watchSplashIntent);
        }
    }
}
