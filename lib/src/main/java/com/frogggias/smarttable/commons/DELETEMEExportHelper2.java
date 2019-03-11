package com.frogggias.smarttable.commons;

import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by frogggias on 05.08.15.
 */
public class DELETEMEExportHelper2 {

    private static final String TAG = DELETEMEExportHelper2.class.getSimpleName();

    public static File getSharedExternalStorageDir() {
        File file;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            file = new File(Environment.getExternalStorageDirectory() + "/Documents");
        } else {
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }
        file.mkdirs();
        return file;
    }

    public static File getFilePath(String filename) {
        return new File(getSharedExternalStorageDir().getAbsolutePath() + "/" + filename);
    }
}
