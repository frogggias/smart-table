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

    private String mName;

    /* DATABASE DATA */
    private final String mUri;
    private SmartTableColumn[] mColumn;

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
                .setContent(textView, cursor, column);
    }

    public void formatContentTextView(TextView textView, Cursor cursor, int column, String query) {
        getColumn(column)
                .getFormatter()
                .setContent(textView, cursor, column, query);
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
            return mProvider;
        }
    }

}
