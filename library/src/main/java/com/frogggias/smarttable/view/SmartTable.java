package com.frogggias.smarttable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.export.CSVTableExporter;
import com.frogggias.smarttable.export.TableExporter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTable
        extends FrameLayout implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = SmartTable.class.getSimpleName();

    private static final int LOADER_DEFAULT = 0;

    private static final TableExporter[] DEFAULT_TABLE_EXPORTERS = new TableExporter[] {
        new CSVTableExporter()
    };

    /* DATA */
    protected SmartTableProvider mSmartTableProvider;
    protected boolean mExportable = true;
    protected List<TableExporter> mTableExporters = Arrays.asList(DEFAULT_TABLE_EXPORTERS);

    /* CONTROLLER */
    protected LoaderManager mLoaderManager;

    public SmartTable(Context context) {
        super(context);
        initUI();
    }

    public SmartTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SmartTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartTable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI();
    }

    private void initUI() {

    }

    private void invalidateData() {
        if (mLoaderManager == null || mSmartTableProvider == null) {
            return;
        }

        mLoaderManager.restartLoader(LOADER_DEFAULT, null, this);
    }

    public void setTableProvider(SmartTableProvider smartTableProvider) {
        mSmartTableProvider = smartTableProvider;
        invalidateData();
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
    }

    protected String getSelection() {
        return "";
    }

    protected String[] getSelectionArgs() {
        return new String[] {};
    }

    protected String getOrder() {
        return "";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                mSmartTableProvider.getUri(),
                null,
                getSelection(),
                getSelectionArgs(),
                getOrder()
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
