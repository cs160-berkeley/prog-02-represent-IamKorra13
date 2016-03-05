package com.example.ashleythemagnificant.prog02;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AshleyTheMagnificant on 2/27/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static String[] mDataset;
    private static int pos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public ImageView imageView;
        public TextView tweetView;
        public TextView descriptionView;
        public ViewHolder(final View v){ //}, ImageView iv, TextView tv) {
            super(v);
            mTextView= v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("T", "I am loggin in myAdapter");
                    /* Mobile Portion. */
                    Intent intent = new Intent(v.getContext(), DetailedView.class);
                    TextView getView = (TextView) view.findViewById(R.id.info_text);
                    String msg = getView.getText().toString().split("\n")[0];
                    Log.d("T", "Message = " + msg);
                    intent.putExtra("SELECTION", msg); //  TODO
                    v.getContext().startActivity(intent);


//                    /* Wear */
//                    Intent wearIntent = new Intent(v.getContext(), PhoneToWatchService.class);
//                    wearIntent.putExtra("SELECTION", mDataset[pos]); //  TODO
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        ViewHolder vh = new ViewHolder(v);//, iv, tv);
        vh.tweetView = (TextView) v.findViewById(R.id.tweet);
        vh.descriptionView = (TextView) v.findViewById(R.id.info_text);
        vh.imageView = (ImageView) v.findViewById(R.id.profile_image);
        return vh;
    }

//     Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        pos = position;
        String[] rep = mDataset[position].split("_");
        holder.tweetView.setText(rep[4]);
        holder.descriptionView.setText(rep[0] + "\n" + rep[1] + "\n" + rep[2] + "\n" + rep[3]);
        String url = rep[5];

//        setImage(holder, url);
        Log.i("BUGG", "I am in MyAdapter yo");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

//    public void setImage(final ViewHolder holder, final String url) {
////   TODO API call for this persons name to get all the data
//        try {
//            ImageView i = holder.imageView;
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
//            i.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

