package com.frogggias.smarttable.filter;

import android.database.Cursor;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by frogggias on 09.07.15.
 */
public interface ColumnFilterCursorProvider extends ColumnFilterProvider, Serializable {

    public Uri getUri();

    public String getName(Cursor cursor);

    public String getValue(Cursor cursor);
}
