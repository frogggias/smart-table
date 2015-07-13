package com.frogggias.smarttable.filter;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by frogggias on 13.07.15.
 */
public class SimpleColumnCursorFilterProvider implements ColumnFilterCursorProvider {

    private static final String TAG = SimpleColumnCursorFilterProvider.class.getSimpleName();

    protected Uri mUri;
    protected String mNameColumn;
    protected String mValueColumn;

    public SimpleColumnCursorFilterProvider(Uri uri, String nameColumn) {
        this(uri, nameColumn, null);
    }

    public SimpleColumnCursorFilterProvider(Uri uri, String nameColumn, String valueColumn) {
        mUri = uri;
        mNameColumn = nameColumn;
        if (mValueColumn == null) {
            mValueColumn = mNameColumn;
        } else {
            mValueColumn = valueColumn;
        }
    }

    @Override
    public Uri getUri() {
        return mUri;
    }

    @Override
    public String getName(Cursor cursor) {
        if ((cursor == null) || (cursor.isAfterLast())) {
            return null;
        }
        return cursor.getString(cursor.getColumnIndex(mNameColumn));
    }

    @Override
    public String getValue(Cursor cursor) {
        if ((cursor == null) || (cursor.isAfterLast())) {
            return null;
        }
        return cursor.getString(cursor.getColumnIndex(mValueColumn));
    }
}
