package com.frogggias.smarttable.filter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by frogggias on 09.07.15.
 */
public interface ColumnFilterCursorProvider extends ColumnFilterProvider {

    public Uri getUri();

    public String getName(Cursor cursor);

    public String getValue(Cursor cursor);
}
