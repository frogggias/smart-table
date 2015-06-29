package com.frogggias.smarttable.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.widget.TextView;

import com.frogggias.smarttable.formatter.ColumnFormatter;
import com.frogggias.smarttable.formatter.TextColumnFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableProvider {

    private static final String TAG = SmartTableProvider.class.getSimpleName();

    public static final String COLUMN_TYPE_TEXT = "text";

    /* DATABASE DATA */
    private final Uri mUri;
    private String[] mColumnName;
    private String[] mSortColumnName;
    private String[] mSearchColumnName;

    /* UI DATA */
    @StringRes
    private int[] mColumnTitleResId;
    private String[] mColumnTitle;
    private Class<? extends ColumnFormatter>[] mColumnFormatter;
    private boolean[] mSortable;
    private boolean[] mSearchable;

    protected HashMap<Class<? extends ColumnFormatter>, ColumnFormatter> mFormatterInstances = new HashMap<>();

    public SmartTableProvider(Uri uri) {
        mUri = uri;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getColumnName(int column) {
        return mColumnName[column];
    }

    public String getSortColumnName(int column) {
        if ((mSortColumnName == null) || (mSortColumnName.length < column)
                || mSortColumnName[column] == null) {
            return getColumnName(column);
        }
        return mSortColumnName[column];
    }

    public String getSearchColumnName(int column) {
        if (mSearchColumnName == null || mSearchColumnName.length < column
                || mSearchColumnName[column] == null) {
            return getSortColumnName(column);
        }
        return mSearchColumnName[column];
    }

    public String getColumnTitle(Context context, int column) {
        if ((mColumnTitleResId == null) || (mColumnTitleResId.length < column)) {
                return getColumnTitle(column);
        }
        return context.getString(mColumnTitleResId[column]);
    }

    private String getColumnTitle(int column) {
        if ((mColumnTitle == null) || (mColumnTitle.length < column)) {
            return getColumnName(column);
        }
        return mColumnTitle[column];
    }

    public boolean isSortable(int column) {
        if ((mSortable == null) || (mSortable.length < column)) {
            return false;
        }
        return mSortable[column];
    }

    public boolean isSearchable(int column) {
        if ((mSearchable == null) || (mSearchable.length < column)) {
            return false;
        }
        return mSearchable[column];
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column) {
        getFormatter(column).setContent(textView, cursor, getColumnName(column));
    }

    private ColumnFormatter getFormatter(int column) {
        if ((mColumnFormatter == null) || (mColumnFormatter.length < column)
                || (mColumnFormatter[column] == null)) {
            return getOrInstanitateFormatter(TextColumnFormatter.class);
        }
        return getOrInstanitateFormatter(mColumnFormatter[column]);
    }

    private ColumnFormatter getOrInstanitateFormatter(Class<? extends ColumnFormatter> clz) {
        if (!mFormatterInstances.containsKey(clz)) {
            try {
                mFormatterInstances.put(clz, clz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                return new TextColumnFormatter();
            }
        }
        return mFormatterInstances.get(clz);
    }

    public static class Builder {

        private SmartTableProvider provider;

        private boolean mSimpleBuildMode = false;

        private ArrayList<String> mColumnName = new ArrayList();
        private ArrayList<String> mSortColumnName = new ArrayList<>();
        private ArrayList<String> mSearchColumnName = new ArrayList<>();

        private ArrayList<Integer> mColumnTitleResId = new ArrayList<>();
        private ArrayList<String> mColumnTitle = new ArrayList<>();
        private ArrayList<Class<? extends ColumnFormatter>> mColumnFormatter = new ArrayList<>();

        public Builder(Uri uri) {
            provider = new SmartTableProvider(uri);
        }

        public Builder setColumnNames(String[] columnName) {
            provider.mColumnName = columnName;
            return this;
        }

        public Builder setSortColumnNames(String[] sortColumnNames) {
            provider.mSortColumnName = sortColumnNames;
            return this;
        }

        public Builder setSearchColumnNames(String[] searchColumnNames) {
            provider.mSearchColumnName = searchColumnNames;
            return this;
        }

        public Builder setSearchable(boolean[] searchable) {
            provider.mSearchable = searchable;
            return this;
        }

        public Builder setColumnTitleNames(String[] columnTitleNames) {
            provider.mColumnTitle = columnTitleNames;
            return this;
        }

        public Builder setColumnTitleNames(@StringRes int[] columnResNames) {
            provider.mColumnTitleResId = columnResNames;
            return this;
        }

        public Builder addColumn(String name, String title, @StringRes int titleResId) {
            return addColumn(name, title, titleResId, null, null, null);
        }

        public Builder addColumn(String name, String title, @StringRes int titleResId,
                                 Class<? extends ColumnFormatter> formatter, String sortColumnName, String searchColumnName) {
            mColumnName.add(name);
            mColumnTitle.add(name);
            mColumnFormatter.add(formatter);
            mSortColumnName.add(sortColumnName);
            mSearchColumnName.add(searchColumnName);
            return this;
        }

        private int[] getIntArray(List<Integer> list) {
            if ((list == null) || (list.size() == 0)) {
                return new int[] {};
            }
            int[] data = new int[list.size()];
            Iterator<Integer> iterator = list.iterator();
            for (int i = 0; i < list.size(); i++) {
                data[i] = iterator.next().intValue();
            }
            return data;
        }

        private String[] getStringArray(List<String> list) {
            return list.toArray(new String[list.size()]);
        }

        private boolean[] getNotNullBooleanArray(List list) {
            if ((list == null) || (list.size() == 0)) {
                return new boolean[] {};
            }
            boolean[] data = new boolean[list.size()];
            Iterator<String> iterator = list.iterator();

            for (int i = 0; i < list.size(); i++) {
                data[i] = !iterator.next().equals(null);
            }

            return data;
        }

        private void setProvider(SmartTableProvider externalProvider) {
            provider = externalProvider;
        }

        public SmartTableProvider build() {
            if (mSimpleBuildMode) {
                provider.mColumnName = getStringArray(mColumnName);
                provider.mSortColumnName = getStringArray(mSortColumnName);
                provider.mSearchColumnName = getStringArray(mSearchColumnName);
                provider.mColumnTitleResId = getIntArray(mColumnTitleResId);
                provider.mSortable = getNotNullBooleanArray(mSearchColumnName);
                provider.mSearchable = getNotNullBooleanArray(mSearchColumnName);
            }
            return provider;
        }
    }

}
