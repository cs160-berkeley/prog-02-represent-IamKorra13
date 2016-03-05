package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class RepresentativeView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Intent intent;
    private TextView numResultsView;
    private String tempData = "Barbara Lee_Democrat_Website_Email_Tweet_https://s3.amazonaws.com/givegreen-cdn/2014/05/Lee-pic-150x150.jpeg#"
            + "Loni Hancock_Democrat_Website_Email_Tweet_http://postnewsgroup.com/wp-content/uploads/2014/02/LoniHancock-1.jpg#" +
            "Mark Leno_Democrat_Website_Email_Tweet_http://www.californiaprogressreport.com/Mark-Leno.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String[] myDataset;
        if (intent.hasExtra("DATA")) {
            String data = intent.getStringExtra("DATA");
            myDataset = convertStringToDataArray(data);
        } else if (intent.hasExtra("LOCATION")) {
            //TODO need to reload the data poss in diff Thread
            myDataset = convertStringToDataArray(tempData);
        } else {
            myDataset = convertStringToDataArray(tempData);
        }

//        Number of results display
        int numResults = myDataset.length;
        numResultsView = (TextView) findViewById(R.id.representative_results);
        numResultsView.setText("Number of results: " + Integer.toString(numResults));

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }

    public String[] convertStringToDataArray(String data) {
        String[] dataset = data.split("#");
        return dataset;
    }

    public void goToDetailedView(View view) {
//        Intent intent  = new Intent(this, DetailedView.class);
//        String message = "You choose Barbara Lee";
//        intent.putExtra("SELECTION", message);
//        startActivity(intent);
    }

}
