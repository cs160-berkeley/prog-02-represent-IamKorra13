package com.example.ashleythemagnificant.prog02;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public final class Adapter extends WearableListView.Adapter {
    private static  String[] mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private static int pos;
    private static String location;

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter(Context context, String dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        String [] temp = dataset.split("@");
        mDataset = temp[1].split("-");
        location =  temp[0];
        }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(final View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("T", "I am loggin in myAdapter");

                        /* Mobile Portion. */
                    Intent intent = new Intent(itemView.getContext(), VoteViewActivity.class);
//                    intent.putExtra("SELECTION", mDataset[pos]); //  TODO
                    intent.putExtra("LOCATION", location);
                    itemView.getContext().startActivity(intent);

                        /* Wear */
                    String name = textView.getText().toString();
                    Intent wearIntent = new Intent(itemView.getContext(), WatchToPhoneService.class);
                    wearIntent.putExtra("SELECTION", name); //  TODO
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
        pos = position;
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        view.setText(mDataset[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
//        Log.d("T", "Length of data set = " + mDataset.length);
//        Log.d("T", "" + mDataset);
        return mDataset.length;
    }
}
