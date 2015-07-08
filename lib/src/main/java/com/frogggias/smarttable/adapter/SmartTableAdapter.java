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
public class SmartTableAdapter
        extends CursorRecyclerViewAdapter<SmartTableAdapter.ViewHolder>
        implements SearchableAdapter {

    private static final String TAG = SmartTableAdapter.class.getSimpleName();

    /* DATA */
    protected SmartTableProvider mProvider;
    protected String mQuery;

    /* CONTROLLER */
    OnItemClickListener mOnItemClickListener;

    public SmartTableAdapter(Context context, Cursor cursor, SmartTableProvider provider) {
        super(context, cursor);
        mProvider = provider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.row, viewGroup, false);
        layout.setClickable(mOnItemClickListener != null);
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
        viewHolder.pos = cursor.getPosition();
        for (int column = 0; column < layout.getChildCount(); column++) {
            TextView tv = (TextView) layout.getChildAt(column);
            mProvider.formatContentTextView(tv, cursor, column, mQuery);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void setSearchQuery(String query) {
        mQuery = query;
    }

    public interface OnItemClickListener {
        void onItemClick(Cursor cursor);
    }

    class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public int pos;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public LinearLayout getView() {
            return (LinearLayout) itemView;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                Cursor cursor = getCursor();
                cursor.move(pos);
                mOnItemClickListener.onItemClick(cursor);
            }
        }
    }
}
