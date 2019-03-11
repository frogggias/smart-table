package com.frogggias.smarttable.export;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author Maroš Šeleng
 */
public interface ExportFileFactory {
    @NonNull
    File getExportFile(@NonNull String filename);

    @NonNull
    Uri getExportUri(@NonNull File file);
}
