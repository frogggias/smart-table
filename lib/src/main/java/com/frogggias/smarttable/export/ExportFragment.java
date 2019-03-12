package com.frogggias.smarttable.export;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * @author Maroš Šeleng
 */
public class ExportFragment extends Fragment {

    private ExportTask mExportTask;
    private WeakReference<OnExportCompletedListener> mListener;

    public static ExportFragment newInstance(@NonNull TableExporter exporter, @NonNull TableExporter.Data exportData) {
        return new ExportFragment(exporter, exportData);
    }

    public ExportFragment() { }

    @SuppressLint("ValidFragment")
    private ExportFragment(@NonNull TableExporter exporter, @NonNull TableExporter.Data exportData) {
        this();
        mExportTask = new ExportTask(exporter, exportData, this);
    }

    public void setListener(@NonNull OnExportCompletedListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (mExportTask == null) {
            return;
        }
        if (!mExportTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            mExportTask.execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExportTask != null) {
            mExportTask.cancel(true);
            mExportTask = null;
        }
    }

    public void onExportCompleted(Uri uri) {
        OnExportCompletedListener listener = getListener();
        if (listener != null) {
            listener.onExportCompleted(uri);
        }
    }

    @Nullable
    private OnExportCompletedListener getListener() {
        return mListener == null ? null : mListener.get();
    }

    public interface OnExportCompletedListener {
        void onExportCompleted(Uri uri);
    }
}
