package com.frogggias.smarttable.commons;

import android.os.Environment;

import java.io.File;

/**
 * Created by frogggias on 05.08.15.
 */
public class ExportHelper {

    private static final String TAG = ExportHelper.class.getSimpleName();

    public static File getSharedExternalStorageDir() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        file.mkdirs();
        return file;
    }

    public static File getFilePath(String filename) {
        return new File(getSharedExternalStorageDir().getAbsolutePath() + "/" + filename);
    }
}
