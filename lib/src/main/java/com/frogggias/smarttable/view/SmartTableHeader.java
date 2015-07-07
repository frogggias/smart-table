package com.frogggias.smarttable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.utils.MaterialHelper;

/**
 * Created by frogggias on 03.07.15.
 */
public class SmartTableHeader extends FrameLayout {

    private static final String TAG = SmartTableHeader.class.getSimpleName();

    /* DATA */
    private boolean mSortable = false;
    private @SmartTable.SortDirection int mSortDir = SmartTable.SORT_NONE;

    /* VIEW */
    private View mView;
    private TextView mText;
    private ImageView mSort;

    public SmartTableHeader(Context context) {
        super(context);
        initUI();
    }

    public SmartTableHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SmartTableHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartTableHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI();
    }

    public void setSortable(boolean sortable) {
        mSortable = sortable;
        invalidateSortIcon();
    }

    public void setSortDirection(@SmartTable.SortDirection int dir) {
        mSortDir = dir;
        invalidateSortIcon();
    }

    public boolean isSortable() {
        return mSortable;
    }

    private void initUI() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.header, this, false);
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartTableHeader.this.performClick();
            }
        });
        addView(mView);
        mText = (TextView) findViewById(R.id.text);
        mSort = (ImageView) findViewById(R.id.sort);
        @ColorInt int textColor = MaterialHelper.isLight(MaterialHelper.getPrimaryColor(getContext())) ?
                Color.BLACK : Color.WHITE;
        mSort.setColorFilter(textColor);
        mText.setTextColor(textColor);

        invalidateSortIcon();
    }

    public void setText(CharSequence text) {
        if (mText != null) {
            mText.setText(text);
        }
    }

    private void invalidateSortIcon() {
        if (!isSortable()) {
            mSort.setVisibility(GONE);
            mView.setClickable(false);
            return;
        } else {
            mSort.setVisibility(VISIBLE);
            mView.setClickable(true);
        }
    }
}
