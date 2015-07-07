package com.frogggias.smarttable.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableAdapter extends CursorRecyclerViewAdapter<SmartTableAdapter.ViewHolder> {

    private static final String TAG = SmartTableAdapter.class.getSimpleName();

    protected SmartTableProvider mProvider;

    public SmartTableAdapter(Context context, Cursor cursor, SmartTableProvider provider) {
        super(context, cursor);
        mProvider = provider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout layout = new LinearLayout(viewGroup.getContext());
        layout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        LinearLayout.LayoutParams lp;

        for (int i = 0; i < mProvider.getColumnCount(); i++) {
            lp  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = mProvider.getColumnWeight(i);
            View view = inflater.inflate(R.layout.cell, layout, false);
            view.setLayoutParams(lp);
            layout.addView(view);
        }
        return new ViewHolder(layout);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        LinearLayout layout = viewHolder.getView();
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView tv = (TextView) layout.getChildAt(i);
            mProvider.formatContentTextView(tv, cursor, i);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
        }

        public LinearLayout getView() {
            return (LinearLayout) itemView;
        }
    }
}
