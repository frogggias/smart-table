package com.frogggias.smarttable.commons;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by frogggias on 16.06.15.
 */
public class CSVHelper {

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
        if ((cursor == null) || (!cursor.moveToFirst())) {
            return null;
        }

        if (columnNames == null) {
            columnNames = cursor.getColumnNames();
        }

        if (columnHeaders == null) {
            columnHeaders = columnNames;
        }

        String filename = name + ".csv";
        File file = new File(getSharedExternalStorageDir().getAbsolutePath() + "/" + filename);
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
                } catch (IOException e) {}
            }
        }

        return Uri.fromFile(file);
    }

    private File getSharedExternalStorageDir() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        file.mkdirs();
        return file;
    }
}
