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

    @Override
    public String getActionName() {
        return "Export to CSV";
    }

    @Override
    @WorkerThread
    public Uri export(@NonNull Data data) {
        String filename = (data.baseFileName + " (" + DateFormat.getDateTimeInstance().format(new Date()) + ")")
                .replace(":","-");
        String[] columnTitles;
        if (data.context == null) {
            columnTitles = SmartTableExtractor.getColumnTitles(data.provider);
        } else {
            columnTitles = SmartTableExtractor.getColumnTitles(data.provider, data.context);
        }
        return new CSVHelper().createCSVFromCursor(filename, data.data, SmartTableExtractor.getColumnNames(data.provider), columnTitles);
    }


    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.exporter_csv_name);
    }
}
