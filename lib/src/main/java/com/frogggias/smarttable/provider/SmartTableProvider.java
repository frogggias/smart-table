package com.frogggias.smarttable.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.widget.TextView;

import com.frogggias.smarttable.formatter.ColumnFormatter;
import com.frogggias.smarttable.formatter.TextColumnFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableProvider implements Serializable {

    private static final String TAG = SmartTableProvider.class.getSimpleName();

    public static final String COLUMN_TYPE_TEXT = "text";

    /* DATABASE DATA */
    private final String mUri;
    private String[] mColumnName;
    private String[] mSortColumnName;
    private String[] mSearchColumnName;
    private String mDefaultSelection = "";
    private String[] mDefaultSelectionArgs = new String[] {};

    /* UI DATA */
    @StringRes
    private int[] mColumnTitleResId;
    private String[] mColumnTitle;
    private Class<? extends ColumnFormatter>[] mColumnFormatter;
    private boolean[] mSortable;
    private boolean[] mSearchable;

    /* FORMATTER CACHE */
    protected HashMap<Class<? extends ColumnFormatter>, ColumnFormatter> mFormatterInstances = new HashMap<>();

    public SmartTableProvider(Uri uri) {
        mUri = uri.toString();
    }

    public Uri getUri() {
        return Uri.parse(mUri);
    }

    public int getColumnCount() {
        return mColumnName.length;
    }

    public String getColumnName(int column) {
        return mColumnName[column];
    }

    public String[] getColumnNames() {
        return mColumnName;
    }

    public String[] getColumnTitles() {
        String[] columnTitles = new String[mColumnName.length];
        for(int i = 0; i <mColumnName.length; i++) {
            columnTitles[i]= getColumnTitle(i);
        }
        return columnTitles;
    }

    public String[] getColumnTitles(Context context) {
        String[] columnTitles = new String[mColumnName.length];
        for(int i = 0; i <mColumnName.length; i++) {
            columnTitles[i]= getColumnTitle(context, i);
        }
        return columnTitles;
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
        if ((mColumnTitleResId == null) || (mColumnTitleResId.length <= column) || (mColumnTitleResId[column] == 0)) {
                return getColumnTitle(column);
        }
        return context.getString(mColumnTitleResId[column]);
    }

    private String getColumnTitle(int column) {
        if ((mColumnTitle == null) || (mColumnTitle.length <= column)) {
            return getColumnName(column);
        }
        return mColumnTitle[column];
    }

    public boolean isSortable(int column) {
        if ((mSortable == null) || (mSortable.length <= column)) {
            return false;
        }
        return mSortable[column];
    }

    public boolean isSearchable(int column) {
        if ((mSearchable == null) || (mSearchable.length <= column)) {
            return false;
        }
        return mSearchable[column];
    }

    public int getColumnWeight(int column) {
        return 1;
    }

    public String getDefaultSelection() {
        return mDefaultSelection;
    }

    public String[] getDefaultSelectionArgs() {
        return mDefaultSelectionArgs;
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column) {
        getFormatter(column).setContent(textView, cursor, getColumnName(column));
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column, String query) {
        getFormatter(column).setContent(textView, cursor, getColumnName(column), query);
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

    public boolean isSearchable() {
        for (int column = 0; column < getColumnCount(); column++) {
            if (isSearchable(column)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {

        private SmartTableProvider mProvider;

        private boolean mSimpleBuildMode = false;
        private boolean mDefaultSortable = false;
        private boolean mDefaultSearchable = false;

        private ArrayList<String> mColumnName = new ArrayList();
        private ArrayList<String> mSortColumnName = new ArrayList<>();
        private ArrayList<String> mSearchColumnName = new ArrayList<>();

        private ArrayList<Integer> mColumnTitleResId = new ArrayList<>();
        private ArrayList<String> mColumnTitle = new ArrayList<>();
        private ArrayList<Class<? extends ColumnFormatter>> mColumnFormatter = new ArrayList<>();

        public Builder(Uri uri) {
            mProvider = new SmartTableProvider(uri);
        }

        public Builder(SmartTableProvider provider) {
            mProvider = provider;
        }

        public Builder setDefaultSortable(boolean sortable) {
            mDefaultSortable = sortable;
            return this;
        }

        public Builder setDefaultSearchable(boolean searchable) {
            mDefaultSearchable = searchable;
            return this;
        }

        public Builder setColumnNames(String[] columnName) {
            mProvider.mColumnName = columnName;
            return this;
        }

        public Builder setSortColumnNames(String[] sortColumnNames) {
            mProvider.mSortColumnName = sortColumnNames;
            return this;
        }

        public Builder setSearchColumnNames(String[] searchColumnNames) {
            mProvider.mSearchColumnName = searchColumnNames;
            return this;
        }

        public Builder setSearchable(boolean[] searchable) {
            mProvider.mSearchable = searchable;
            return this;
        }

        public Builder setColumnTitleNames(String[] columnTitleNames) {
            mProvider.mColumnTitle = columnTitleNames;
            return this;
        }

        public Builder setColumnTitleNames(@StringRes int[] columnResNames) {
            mProvider.mColumnTitleResId = columnResNames;
            return this;
        }

        public Builder addColumn(String name, String title) {
            return addColumn(name, title, 0, null, null, null);
        }

        public Builder addColumn(String name, String title, @StringRes int titleResId) {
            return addColumn(name, title, titleResId, null, null, null);
        }

        public Builder addColumn(String name, String title, @StringRes int titleResId,
                                 Class<? extends ColumnFormatter> formatter, String sortColumnName, String searchColumnName) {
            mColumnName.add(name);
            mColumnTitle.add(title);
            mColumnTitleResId.add(titleResId);
            mColumnFormatter.add(formatter);
            mSortColumnName.add(sortColumnName);
            mSearchColumnName.add(searchColumnName);
            mSimpleBuildMode = true;
            return this;
        }

        public Builder setDefaultSelection(String selection) {
            mProvider.mDefaultSelection = selection;
            return this;
        }

        public Builder setDefaultSelectionArgs(String[] selectionArgs) {
            mProvider.mDefaultSelectionArgs = selectionArgs;
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
                data[i] = iterator.next() != null;
            }

            return data;
        }

        private boolean[] getValBooleanArray(int size, boolean value) {
            boolean[] data = new boolean[size];
            if (value) {
                for(int i = 0 ; i < size; i++) {
                    data[i]= value;
                }
            }
            return data;
        }

        public SmartTableProvider build() {
            if (mSimpleBuildMode) {
                mProvider.mColumnName = getStringArray(mColumnName);
                mProvider.mSortColumnName = getStringArray(mSortColumnName);
                mProvider.mSearchColumnName = getStringArray(mSearchColumnName);
                mProvider.mColumnTitle = getStringArray(mColumnTitle);
                mProvider.mColumnTitleResId = getIntArray(mColumnTitleResId);
                if (mDefaultSortable) {
                    mProvider.mSortable = getValBooleanArray(mSortColumnName.size(), true);
                } else {
                    mProvider.mSortable = getNotNullBooleanArray(mSortColumnName);
                }
                if (mDefaultSearchable) {
                    mProvider.mSearchable = getValBooleanArray(mSearchColumnName.size(), true);
                } else {
                    mProvider.mSearchable = getNotNullBooleanArray(mSearchColumnName);
                }
                mProvider.mSearchable = getNotNullBooleanArray(mSearchColumnName);
            }
            return mProvider;
        }
    }

}
