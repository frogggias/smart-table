package com.frogggias.smarttable.export;

import android.content.Context;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class TableExporter {

    private static final String TAG = TableExporter.class.getSimpleName();

    public abstract boolean hasSettings();

    public String getActionName(Context context) {
        return getActionName();
    }

    public abstract String getActionName();

}
