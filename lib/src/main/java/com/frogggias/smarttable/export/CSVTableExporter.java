package com.frogggias.smarttable.export;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.commons.CSVHelper;
import com.frogggias.smarttable.helper.SmartTableExtractor;
import com.frogggias.smarttable.provider.SmartTableProvider;

import java.text.DateFormat;
import java.util.Date;

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
    public String export(String filename, SmartTableProvider provider, Cursor data) {
        mProvider = provider;
        mFilename = filename + " (" + DateFormat.getTimeInstance().format(new Date()) + ")";
        new ExportTask().execute(data);
        return filename;
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
                columnTitles = SmartTableExtractor.getColumnTitles(mProvider);
            } else {
                columnTitles = SmartTableExtractor.getColumnTitles(mProvider, mContext);
            }
            new CSVHelper().createCSVFromCursor(mFilename, cursor, SmartTableExtractor.getColumnNames(mProvider), columnTitles);
            return null;
        }
    }
}
