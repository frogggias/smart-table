package com.frogggias.smarttable.export;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class TableExporter {

    private static final String TAG = TableExporter.class.getSimpleName();

    public static class Data {
        protected Context context;
        protected String baseFileName;
        protected SmartTableProvider provider;
        protected Cursor data;

        public Data(Context context, String baseFileName, SmartTableProvider provider, Cursor data) {
            this.context = context;
            this.baseFileName = baseFileName;
            this.provider = provider;
            this.data = data;
        }
    }

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

    @WorkerThread
    public abstract Uri export(@NonNull Data data);

    @NonNull
    @Override
    public String toString() {
        return getActionName();
    }

    public interface OnExportDoneListener {
        void onExportDone(Uri uri);
    }
}
