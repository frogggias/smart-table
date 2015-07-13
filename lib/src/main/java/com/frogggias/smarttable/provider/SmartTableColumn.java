package com.frogggias.smarttable.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.frogggias.smarttable.filter.ColumnFilterProvider;
import com.frogggias.smarttable.formatter.ColumnFormatter;
import com.frogggias.smarttable.formatter.TextColumnFormatter;

/**
 * Created by frogggias on 10.07.15.
 */
public class SmartTableColumn {

    private static final String TAG = SmartTableColumn.class.getSimpleName();

    private final String mName;

    private String mTitle;
    private @StringRes int mTitleRes = 0;

    private float mLayoutWeight = 1.0f;

    private boolean mIsSortable;
    private String mSortName;

    private boolean mIsSearchable;
    private String mSearchName;

    private ColumnFormatter mFormatter = new TextColumnFormatter();

    private ColumnFilterProvider mFilterProvider;

    public SmartTableColumn (@NonNull String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @NonNull
    public String getTitle() {
        if (mTitle != null) {
            return mTitle;
        }
        return getName();
    }

    public void setTitleRes(@StringRes int titleRes) {
        mTitleRes = titleRes;
    }

    public String getTitle(Context context) {
        if ((context == null) || (mTitleRes == 0)) {
            return getTitle();
        }
        return context.getString(mTitleRes);
    }

    public void setLayoutWeight(float layoutWeight) {
        mLayoutWeight = layoutWeight;
    }

    public float getLayoutWeight() {
        return mLayoutWeight;
    }

    public void setSortable(boolean sortable) {
        mIsSortable = sortable;
    }

    public boolean isSortable() {
        return mIsSortable;
    }

    public void setSortName(String sortName) {
        mSortName = sortName;
    }

    public String getSortName() {
        if (mSortName == null) {
            return getName();
        }
        return mSortName;
    }

    public void setSearchable(boolean searchable) {
        mIsSearchable = searchable;
    }

    public boolean isSearchable() {
        return mIsSearchable;
    }

    public void setSearchName(String searchName) {
        mSearchName = searchName;
    }

    public String getSearchName() {
        if (mSearchName == null) {
            return getName();
        }
        return mSearchName;
    }

    public void setFormatter(ColumnFormatter formatter) {
        mFormatter = formatter;
    }

    public ColumnFormatter getFormatter() {
        return mFormatter;
    }

    public boolean isFilterable() {
        return mFilterProvider != null;
    }

    public void setFilterProvider(ColumnFilterProvider filterProvider) {
        mFilterProvider = filterProvider;
    }

    public ColumnFilterProvider getFilterProvider() {
        return mFilterProvider;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static class Factory {

        private float mDefaultLayoutWeight = 1.0f;
        private boolean mDefaultSortable = false;
        private boolean mDefaultSearchable = false;
        private ColumnFormatter mDefaultFormatter = new TextColumnFormatter();

        public Factory() {}

        public void setDefaultLayoutWeight(float layoutWeight) {
            mDefaultLayoutWeight = layoutWeight;
        }

        public void setDefaultSortable(boolean sortable) {
            mDefaultSortable = sortable;
        }

        public void setDefaultSearchable(boolean searchable) {
            mDefaultSearchable = searchable;
        }

        public void setDefaultFormatter(ColumnFormatter formatter) {
            mDefaultFormatter = formatter;
        }

        public SmartTableColumn create(String columnName) {
            SmartTableColumn column = new SmartTableColumn(columnName);
            column.setLayoutWeight(mDefaultLayoutWeight);
            column.setSortable(mDefaultSortable);
            column.setSearchable(mDefaultSearchable);
            column.setFormatter(mDefaultFormatter);
            return column;
        }
    }

}
