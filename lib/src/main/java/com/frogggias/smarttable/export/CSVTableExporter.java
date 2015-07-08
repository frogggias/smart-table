package com.frogggias.smarttable.export;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.commons.CSVHelper;
import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public class CSVTableExporter extends TableExporter {

    private static final String TAG = CSVTableExporter.class.getSimpleName();

    public CSVTableExporter() {
        super();
    }

    public CSVTableExporter(Context context) {
        this();
        mContext = context;
    }

    @Override
    public boolean hasSettings() {
        return false;
    }

    @Override
    public String getActionName() {
        return "Export to CSV";
    }

    @Override
    public void export(String filename, SmartTableProvider provider, Cursor data) {
        mProvider = provider;
        mFilename = filename;
        new ExportTask().execute(data);
    }


    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.exporter_csv_name);
    }

    class ExportTask extends AsyncTask<Cursor, Void, Void> {

        @Override
        protected Void doInBackground(Cursor... params) {
            Cursor cursor = params[0];
            String[] columnTitles;
            if (mContext == null) {
                columnTitles = mProvider.getColumnTitles();
            } else {
                columnTitles = mProvider.getColumnTitles(mContext);
            }
            new CSVHelper().createCSVFromCursor(mFilename, cursor, mProvider.getColumnNames(), columnTitles);
            return null;
        }
    }
}
