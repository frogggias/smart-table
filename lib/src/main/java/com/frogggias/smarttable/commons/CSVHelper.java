package com.frogggias.smarttable.commons;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by frogggias on 16.06.15.
 */
public class CSVHelper extends ExportHelper {

    private static final String TAG = CSVHelper.class.getSimpleName();

    private static final char DEFAULT_SEPARATOR = '\t';

    protected char mSeparator = DEFAULT_SEPARATOR;

    public CSVHelper() {

    }

    public char getSeparator() {
        return mSeparator;
    }

    public void setSeparator(char separator) {
        mSeparator = separator;
    }

    public Uri createCSVFromCursor(String name, Cursor cursor, String[] columnNames, String[] columnHeaders) {
        if ((cursor == null)) {
            return null;
        }
        cursor.moveToFirst();

        if (columnNames == null) {
            columnNames = cursor.getColumnNames();
        }

        if (columnHeaders == null) {
            columnHeaders = columnNames;
        }

        String filename = name + ".csv";
        File file = getFilePath(filename);
        OutputStream outputStream = null;
        try {

            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            // Write headers
            for(int i = 0; i < columnHeaders.length; i++) {
                if (i > 0) {
                    outputStream.write(mSeparator);
                }
                outputStream.write(columnHeaders[i].getBytes());
            }
            outputStream.write('\n');


            while (!cursor.isAfterLast()) {
                for(int i = 0; i < columnNames.length; i++) {
                    if (i > 0) {
                        outputStream.write(mSeparator);
                    }
                    outputStream.write(cursor.getString(cursor.getColumnIndex(columnNames[i])).getBytes());
                }
                outputStream.write('\n');
                cursor.moveToNext();
            }


        } catch (Exception e ) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }

        return Uri.fromFile(file);
    }
}
