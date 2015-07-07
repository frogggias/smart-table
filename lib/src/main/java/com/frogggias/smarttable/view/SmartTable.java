package com.frogggias.smarttable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.adapter.SmartTableAdapter;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.export.CSVTableExporter;
import com.frogggias.smarttable.export.TableExporter;
import com.frogggias.smarttable.utils.MaterialHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    @IntDef({SORT_NONE, SORT_ASC, SORT_DESC})
    @Retention(RetentionPolicy.SOURCE)
    @interface SortDirection {};
    public static final int SORT_NONE = 0;
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 2;

    /* DATA */
    protected SmartTableProvider mSmartTableProvider;
    protected boolean mExportable = true;
    protected List<TableExporter> mTableExporters = Arrays.asList(DEFAULT_TABLE_EXPORTERS);
    protected String mSortColumn = null;
    protected @SortDirection
    int mSortOrder = SORT_NONE;

    /* CONTROLLER */
    protected LoaderManager mLoaderManager;
    protected SmartTableAdapter mAdapter;
    private OnRowClickedListener mOnRowClickedListener;

    /* VIEW */
    private View mHeaderWrapper;
    private LinearLayout mHeader;
    private RecyclerView mList;
    private View mEmpty;
    private View mLoading;

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
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.table, this, true);

        mHeaderWrapper = findViewById(R.id.header_wrapper);
        mHeader = (LinearLayout) findViewById(R.id.header);
        mList = (RecyclerView) findViewById(R.id.list);
        mEmpty = findViewById(R.id.empty);
        mLoading = findViewById(R.id.loading);

        mHeaderWrapper.setBackgroundColor(MaterialHelper.getPrimaryColor(getContext()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);

        updateUI();
    }

    private void updateUI() {
    }

    private void createHeader() {
        if ((mSmartTableProvider == null) || (mHeader == null) || (mHeader.getChildCount() > 0)) {
            return;
        }

        boolean defaultSortSet = false;
        LinearLayout.LayoutParams lp;
        for (int i = 0; i < mSmartTableProvider.getColumnCount(); i++) {

            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = mSmartTableProvider.getColumnWeight(i);

            SmartTableHeader header = new SmartTableHeader(mHeader.getContext());
            header.setLayoutParams(lp);
            header.setSortable(mSmartTableProvider.isSortable(i));
            header.setText(mSmartTableProvider.getColumnTitle(getContext(), i));

            mHeader.addView(header);
        }
        // Default sort
        if ((mSortColumn == null) && (mSmartTableProvider.getColumnCount() > 0)) {

        }
    }

    private void invalidateData() {
        if (mLoaderManager == null || mSmartTableProvider == null) {
            return;
        }
        createHeader();

        mAdapter = new SmartTableAdapter(getContext(), null, mSmartTableProvider);
        mList.setAdapter(mAdapter);
        mLoaderManager.restartLoader(LOADER_DEFAULT, null, this);
    }

    public void setTableProvider(SmartTableProvider smartTableProvider) {
        mSmartTableProvider = smartTableProvider;
        invalidateData();
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
        invalidateData();
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

    public void setOnRowClickedListener(OnRowClickedListener listener) {
        mOnRowClickedListener = listener;
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
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    public interface OnRowClickedListener {
        void onRowClicked(Cursor cursor);
    }
}
