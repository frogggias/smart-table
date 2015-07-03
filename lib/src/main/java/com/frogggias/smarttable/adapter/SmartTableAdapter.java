package com.frogggias.smarttable.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableAdapter extends CursorRecyclerViewAdapter<SmartTableAdapter.ViewHolder> {

    private static final String TAG = SmartTableAdapter.class.getSimpleName();

    public SmartTableAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
