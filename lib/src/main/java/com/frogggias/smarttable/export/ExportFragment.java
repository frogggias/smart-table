package com.frogggias.smarttable.export;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.frogggias.smarttable.fragment.SmartTableFragment;

import java.lang.ref.WeakReference;

/**
 * @author Maroš Šeleng
 */
public class ExportFragment extends Fragment {

    private static final String TAG = ExportFragment.class.getSimpleName();

    private ExportTask mExportTask;
    private WeakReference<OnExportCompletedListener> mListener;
    private SmartTableFragment mParentFragment;

    public static ExportFragment newInstance(@NonNull TableExporter exporter, @NonNull TableExporter.Data exportData,
                                             @NonNull SmartTableFragment parentFragment) {
        return new ExportFragment(exporter, exportData, parentFragment);
    }

    public ExportFragment() { }

    @SuppressLint("ValidFragment")
    private ExportFragment(@NonNull TableExporter exporter, @NonNull TableExporter.Data exportData,
                           @NonNull SmartTableFragment parentFragment) {
        this();
        mExportTask = new ExportTask(exporter, exportData, this);
        mParentFragment = parentFragment;
    }

    public ExportFragment setListener(@NonNull OnExportCompletedListener listener) {
        mListener = new WeakReference<>(listener);
        return this;
    }

    public void export(@NonNull String tag) {
        if (mParentFragment.getChildFragmentManager().findFragmentByTag(tag) != null) {
            Log.i(TAG, "Export for type '"  + tag + "' is already in progress.");
            return;
        }
        mParentFragment.getChildFragmentManager().beginTransaction()
                .add(this, tag)
                .commit();
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
        mParentFragment.getChildFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

    @Nullable
    private OnExportCompletedListener getListener() {
        return mListener == null ? null : mListener.get();
    }

    public interface OnExportCompletedListener {
        void onExportCompleted(Uri uri);
    }
}
