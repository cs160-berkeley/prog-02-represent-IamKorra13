package com.example.ashleythemagnificant.prog02;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public final class Adapter extends WearableListView.Adapter {
    private static  String[] mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private static int pos;
    private static String location;
    private static String votes;
    private HashMap<String, String> parties = new HashMap<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter(Context context, String dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        String[] values = dataset.split("@@@");
        mDataset = values[2].split("#");
        votes = values[0] + "#" + values[1];
        parties.put("D", "Democrat");
        parties.put("R", "Republican");
        parties.put("I", "Independent");
        }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.circle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("T", "I am loggin in myAdapter");
                    int i = getAdapterPosition();
                    Log.d("Adapter WATCH", "Pos in list = " + i);
                    String data = mDataset[i];

                        /* Mobile Portion. */
                    Intent intent = new Intent(itemView.getContext(), VoteViewActivity.class);
//                    intent.putExtra("SELECTION", mDataset[pos]); //  TODO
                    intent.putExtra("LOCATION", votes);
                    itemView.getContext().startActivity(intent);

                        /* Wear */
                    String name = textView.getText().toString();
                    Intent wearIntent = new Intent(itemView.getContext(), WatchToPhoneService.class);
                    wearIntent.putExtra("SELECTION", data); //  TODO
                    Log.i("T", "Adapter Starting wear activity");
                    itemView.getContext().startService(wearIntent);
                }
            });
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        /* value of the location is stored in first elem of data set. */
//        pos = position;
        String[] fields = mDataset[position].split("___");
        String text = fields[5] + "\n" + parties.get(fields[4]);
        Log.d("WATCH", "Text for adapters " + text);
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        view.setText(text);
        holder.itemView.setTag(position);

        ImageView imageView = itemHolder.imageView;
//        String url = fields[8];
//        Log.d("WATCH", "Image url " + url);
//        Picasso.with(mContext)
//                .load(url)
//                .into(imageView);
    }

    @Override
    public int getItemCount() {
//        Log.d("T", "Length of data set = " + mDataset.length);
//        Log.d("T", "" + mDataset);
        return mDataset.length;
    }
}
