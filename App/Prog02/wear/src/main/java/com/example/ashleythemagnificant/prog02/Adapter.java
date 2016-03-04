package com.example.ashleythemagnificant.prog02;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AshleyTheMagnificant on 2/29/16.
 */
public final class Adapter extends WearableListView.Adapter {
    private  String[] mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private int pos;

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter(Context context, String[] dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
        }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(final View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.i("T", "I am loggin in myAdapter");
//
//                        /* Mobile Portion. */
//                    Intent intent = new Intent(itemView.getContext(), VoteViewActivity.class);
//                    intent.putExtra("SELECTION", mDataset[pos]); //  TODO
//                    itemView.getContext().startActivity(intent);
//
//                        /* Wear */
//                    Intent wearIntent = new Intent(itemView.getContext(), WatchToPhoneService.class);
//                    wearIntent.putExtra("SELECTION", mDataset[pos]); //  TODO
//                    Log.i("T", "Adapter Starting wear activity");
//                    itemView.getContext().startService(wearIntent);
//                }
//            });
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
        pos = position;
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        view.setText(mDataset[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
