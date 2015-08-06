package com.frogggias.smarttable.export;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class TableExporter {

    private static final String TAG = TableExporter.class.getSimpleName();

    protected Context mContext;
    protected SmartTableProvider mProvider;
    protected String mFilename;
    protected boolean mSilent = false;

    /* CONTROLLER */
    OnExportDoneListener mListener;

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

    public Uri export(Context context, String baseFilename, SmartTableProvider provider, Cursor data) {
        return export(context, baseFilename, provider, data, false);
    }

    public abstract Uri export(Context context, String baseFilename, SmartTableProvider provider, Cursor data, boolean silent);

    public void setOnExportDoneListener(OnExportDoneListener listener) {
        mListener = listener;
    }

    @Override
    public String toString() {
        return getActionName();
    }

    protected void exportDone(Uri uri) {
        if (mListener != null) {
            mListener.onExportDone(uri);
        }
    }

    protected void setSilent(boolean value) {
        mSilent = value;
    }

    protected boolean getSilent() {
        return mSilent;
    }

    public interface OnExportDoneListener {
        public void onExportDone(Uri uri);
    }
}
