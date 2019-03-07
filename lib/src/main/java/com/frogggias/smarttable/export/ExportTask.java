package com.frogggias.smarttable.export;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * @author Maroš Šeleng
 */
public class ExportTask extends AsyncTask<Void, Void, Uri> {

    private final TableExporter mExporter;
    private final TableExporter.Data mExportData;
    private WeakReference<ExportFragment> mExportFragment;

    public ExportTask(@NonNull TableExporter exporter, @NonNull TableExporter.Data exportData,
                      @NonNull ExportFragment fragment) {
        this.mExporter = exporter;
        this.mExportData = exportData;
        this.mExportFragment = new WeakReference<>(fragment);
    }

    @Override
    protected Uri doInBackground(Void... voids) {
        return mExporter.export(mExportData);
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        ExportFragment exportFragment = getExportFragment();
        if (exportFragment != null) {
            exportFragment.onExportCompleted(uri);
        }
    }

    @Nullable
    private ExportFragment getExportFragment() {
        return mExportFragment == null ? null : mExportFragment.get();
    }
}
