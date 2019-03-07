package com.frogggias.smarttable.export;

import android.support.annotation.NonNull;

import com.frogggias.smarttable.ExporterNotInitializedException;

import java.io.File;

/**
 * @author Maroš Šeleng
 */
public class SmartTableExporter {

    private static volatile ExportFileFactory sExportFileFactory;

    private SmartTableExporter() {

    }

    public static synchronized void initWithExportFileFactory(@NonNull ExportFileFactory factory) {
        if (sExportFileFactory != null) {
            return;
        }
        sExportFileFactory = factory;
    }

    @NonNull
    public static File getExportFile(@NonNull String filename) {
        if (sExportFileFactory == null) {
            throw new ExporterNotInitializedException();
        }
        return new File("");
    }
}
