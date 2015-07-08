package com.frogggias.smarttable.export;

import android.content.Context;
import android.database.Cursor;

import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class TableExporter {

    private static final String TAG = TableExporter.class.getSimpleName();

    protected Context mContext;
    protected SmartTableProvider mProvider;
    protected String mFilename;

    public TableExporter() {

    }

    public void setFilename(String name) {
        mFilename = name;
    }

    public void setSmartTableProvider(SmartTableProvider provider) {
        mProvider = provider;
    }

    public abstract boolean hasSettings();

    public String getActionName(Context context) {
        return getActionName();
    }

    public abstract String getActionName();

    public abstract void export(String filename, SmartTableProvider provider, Cursor data);

    @Override
    public String toString() {
        return getActionName();
    }
}
