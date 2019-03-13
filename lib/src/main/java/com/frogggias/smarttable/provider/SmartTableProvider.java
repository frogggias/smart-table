package com.frogggias.smarttable.provider;

import android.database.Cursor;
import android.net.Uri;
import android.widget.TextView;

import com.frogggias.smarttable.formatter.ColumnFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableProvider implements Serializable {

    private static final String TAG = SmartTableProvider.class.getSimpleName();

    public static final String COLUMN_TYPE_TEXT = "text";

    private String mName;

    /* DATABASE DATA */
    private final String mUri;
    private SmartTableColumn[] mColumn;

    private String[] mProjection = new String[] {};

    private String mDefaultSelection = "";
    private String[] mDefaultSelectionArgs = new String[] {};

    private Boolean mIsSearchable = null;
    private Boolean mIsSortable = null;

    /* FORMATTER CACHE */
    protected HashMap<Class<? extends ColumnFormatter>, ColumnFormatter> mFormatterInstances = new HashMap<>();

    public SmartTableProvider(Uri uri) {
        mUri = uri.toString();
    }

    public Uri getUri() {
        return Uri.parse(mUri);
    }

    public int getProjectionCount() {
        return mProjection.length;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public String getProjection(int position) {
        return mProjection[position];
    }

    public SmartTableColumn getColumn(int column) {
        return mColumn[column];
    }

    public SmartTableColumn[] getColumns() {
        return mColumn;
    }

    public int getColumnCount() {
        return mColumn.length;
    }

    public String getColumnName(int column) {
        return getColumn(column).getName();
    }

    public boolean isSearchable() {
        if (mIsSearchable == null) {
            mIsSearchable = false;
            for (int column = 0; column < getColumnCount(); column++) {
                if (getColumn(column).isSearchable()) {
                    mIsSearchable = true;
                    break;
                }
            }
        }

        return mIsSearchable;
    }

    public boolean isSortable() {
        if (mIsSortable == null) {
            mIsSortable = false;
            for (int column = 0; column < getColumnCount(); column++) {
                if (getColumn(column).isSortable()) {
                    mIsSortable = true;
                    break;
                }
            }
        }

        return mIsSortable;
    }

    public String getDefaultSelection() {
        return mDefaultSelection;
    }

    public String[] getDefaultSelectionArgs() {
        return mDefaultSelectionArgs;
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column) {
        getColumn(column)
                .getFormatter()
                .setContent(textView, cursor, mColumn[column].getName());
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column, String query) {
        getColumn(column)
                .getFormatter()
                .setContent(textView, cursor, mColumn[column].getName(), query);
    }

    @Override
    public String toString() {
        if (mName != null) {
            return mName;
        }
        return super.toString();
    }

    public static class Builder {

        private SmartTableProvider mProvider;

        private ArrayList<SmartTableColumn> mColumn = new ArrayList<>();

        private HashSet<String> mProjection = new HashSet<>();

        public Builder(Uri uri) {
            mProvider = new SmartTableProvider(uri);
        }

        public Builder(SmartTableProvider provider) {
            mProvider = provider;
        }

        public Builder setName(String name) {
            mProvider.mName = name;
            return this;
        }

        public Builder addColumn(SmartTableColumn column) {
            mColumn.add(column);
            return this;
        }

        public Builder addProjection(String... columns) {
            Collections.addAll(mProjection, columns);
            return this;
        }

        public Builder addProjection(Collection<String> columns) {
            mProjection.addAll(columns);
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



        public SmartTableProvider build() {
            mProvider.mColumn = mColumn.toArray(new SmartTableColumn[mColumn.size()]);
            mProvider.mProjection = mProjection.toArray(new String[mProjection.size()]);
            return mProvider;
        }
    }

}
