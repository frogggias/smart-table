package com.frogggias.smarttable.export;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.commons.CSVHelper;
import com.frogggias.smarttable.helper.SmartTableExtractor;
import com.frogggias.smarttable.provider.SmartTableProvider;

import java.io.File;
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
    public Uri export(Context context, String filename, SmartTableProvider provider, Cursor data, boolean silent) {
        mSilent = silent;
        mContext = context;
        mProvider = provider;
        mFilename = filename + " (" + DateFormat.getDateTimeInstance().format(new Date()) + ")";
        mFilename = mFilename.replace(":","-");
        new ExportTask().execute(data);
        return Uri.fromFile(CSVHelper.getFilePath(filename + ".csv"));
    }


    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.exporter_csv_name);
    }

    class ExportTask extends AsyncTask<Cursor, Void, Uri> {

        @Override
        protected Uri doInBackground(Cursor... params) {
            Cursor cursor = params[0];
            String[] columnTitles;
            if (mContext == null) {
                columnTitles = SmartTableExtractor.getColumnTitles(mProvider);
            } else {
                columnTitles = SmartTableExtractor.getColumnTitles(mProvider, mContext);
            }
            return new CSVHelper().createCSVFromCursor(mFilename, cursor, SmartTableExtractor.getColumnNames(mProvider), columnTitles);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            exportDone(uri);
            if (!getSilent() && (mContext != null) && (uri != null)) {
                AlertDialog dialog = createOpenFileDialog(mContext, uri);
                dialog.show();
            }
        }
    }

    protected AlertDialog createOpenFileDialog(Context context, final Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        File file = new File(Uri.decode(uri.toString()));

        String message = mContext.getString(R.string.exporter_csv_success, file.getName());

        builder.setMessage(message);
        builder.setPositiveButton(R.string.exporter_csv_open_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFile(uri);
            }
        });
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    protected void openFile(Uri uri) {
        if (mContext != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/csv");
            mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.exporter_csv_open_file)));
        }
    }
}
