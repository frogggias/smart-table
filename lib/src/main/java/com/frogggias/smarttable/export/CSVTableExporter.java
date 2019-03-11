package com.frogggias.smarttable.export;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.commons.CSVHelper;
import com.frogggias.smarttable.helper.SmartTableExtractor;

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
    @WorkerThread
    public Uri export(@NonNull Data data) {
        mContext = data.context;
        mProvider = data.provider;
        mFilename = data.baseFileName + " (" + DateFormat.getDateTimeInstance().format(new Date()) + ")";
        mFilename = mFilename.replace(":","-");
        String[] columnTitles;
        if (mContext == null) {
            columnTitles = SmartTableExtractor.getColumnTitles(mProvider);
        } else {
            columnTitles = SmartTableExtractor.getColumnTitles(mProvider, mContext);
        }
        return new CSVHelper().createCSVFromCursor(mFilename, data.data, SmartTableExtractor.getColumnNames(mProvider), columnTitles);
    }


    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.exporter_csv_name);
    }
}
