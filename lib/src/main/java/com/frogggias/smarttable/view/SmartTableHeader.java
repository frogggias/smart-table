package com.frogggias.smarttable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.provider.SmartTableColumn;
import com.frogggias.smarttable.utils.MaterialHelper;

/**
 * Created by frogggias on 03.07.15.
 */
public class SmartTableHeader extends FrameLayout {

    private static final String TAG = SmartTableHeader.class.getSimpleName();

    /* DATA */
    private SmartTableColumn mColumnInfo;
    private boolean mSortable = false;
    private @SmartTable.SortDirection int mSortDir = SmartTable.SORT_NONE;
    private boolean mFilterUsed = false;

    /* CONTROLLER */
    OnHeaderClickListener mListener;

    /* VIEW */
    private View mView;
    private TextView mText;
    private ImageView mSort;
    private ImageView mFilterIcon;

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

    public void setColumnInfo(SmartTableColumn columnInfo) {
        mColumnInfo = columnInfo;
        invalidateSortIcon();
        invalidateFilterIcon();
    }

    public void setFilterUsed(boolean value) {
        mFilterUsed = value;
        invalidateFilterIcon();
    }

    public void setSortable(boolean sortable) {
        mSortable = sortable;
        invalidateSortIcon();
    }

    public void setSortDirection(@SmartTable.SortDirection int dir) {
        mSortDir = dir;
        invalidateSortIcon();
    }

    public @SmartTable.SortDirection int getSortDirection() {
        return mSortDir;
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
        mFilterIcon = (ImageView) findViewById(R.id.filter);


        @ColorInt int textColor = MaterialHelper.isLight(MaterialHelper.getPrimaryColor(getContext())) ?
                Color.BLACK : Color.WHITE;
        mSort.setColorFilter(textColor);
        mFilterIcon.setColorFilter(textColor);
        mText.setTextColor(textColor);

        invalidateSortIcon();
    }

    public void setText(CharSequence text) {
        if (mText != null) {
            mText.setText(text);
        }
    }

    private void invalidateSortIcon() {
        mText.setTypeface(null, Typeface.NORMAL);
        if (!isSortable()) {
            mSort.setVisibility(GONE);
            mView.setClickable(false);
            return;
        } else {
            mSort.setVisibility(VISIBLE);
            mView.setClickable(true);
            switch (mSortDir) {
                case SmartTable.SORT_DESC:
                case SmartTable.SORT_ASC:
                    mSort.setRotation(mSortDir == SmartTable.SORT_ASC ? 180f : 0f);
                    mSort.setVisibility(VISIBLE);
                    mText.setTypeface(null, Typeface.BOLD);
                    break;
                default:
                    mSort.setVisibility(INVISIBLE);
            }
        }
    }

    private void invalidateFilterIcon() {
        if (!mColumnInfo.isFilterable()) {
            mFilterIcon.setVisibility(GONE);
            mFilterIcon.setOnClickListener(null);
            return;
        }
        mFilterIcon.setVisibility(VISIBLE);
        mFilterIcon.setAlpha(mFilterUsed ? 1.0f : 0.5f);
        mFilterIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFilterClick(SmartTableHeader.this);
                }
            }
        });

    }

    private void sortDirectionToggle() {
        if (mSortDir == SmartTable.SORT_ASC) {
            setSortDirection(SmartTable.SORT_DESC);
        } else {
            setSortDirection(SmartTable.SORT_ASC);
        }
        invalidateSortIcon();
    }

    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mListener = listener;
    }

    @Override
    public boolean performClick() {
        sortDirectionToggle();
        if (mListener != null) {
            mListener.onHeaderClick(this);
        }
        return true;
    }

    interface OnHeaderClickListener {
        void onHeaderClick(SmartTableHeader view);
        void onFilterClick(SmartTableHeader view);
    }
}
