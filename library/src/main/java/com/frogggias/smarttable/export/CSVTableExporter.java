package com.frogggias.smarttable.export;

import android.content.Context;

import com.frogggias.smarttable.R;

/**
 * Created by frogggias on 29.06.15.
 */
public class CSVTableExporter extends TableExporter {

    private static final String TAG = CSVTableExporter.class.getSimpleName();

    @Override
    public boolean hasSettings() {
        return false;
    }

    @Override
    public String getActionName() {
        return "Export to CSV";
    }

    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.exporter_csv_name);
    }
}
