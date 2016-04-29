package com.frogggias.smarttable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.adapter.SmartTableAdapter;
import com.frogggias.smarttable.canonizer.SimpleStringCanonizer;
import com.frogggias.smarttable.canonizer.StringCanonizer;
import com.frogggias.smarttable.provider.SmartTableColumn;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.export.CSVTableExporter;
import com.frogggias.smarttable.export.TableExporter;
import com.frogggias.smarttable.utils.MaterialHelper;
import com.frogggias.smarttable.view.support.DividerItemDecoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.US;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTable
        extends FrameLayout
        implements LoaderManager.LoaderCallbacks<Cursor>, SmartTableHeader.OnHeaderClickListener, SmartTableAdapter.OnItemClickListener {

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
    protected @SortDirection int mSortOrder = SORT_NONE;
    protected String mSearchQuery = null;

    /* CONTROLLER */
    protected LoaderManager mLoaderManager;
    protected SmartTableAdapter mAdapter;
    private OnRowClickedListener mOnRowClickedListener;
    private Cursor mCursor;
    private StringCanonizer mCanonizer = new SimpleStringCanonizer();

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

    public void setSearchQuery(String query) {
        if (TextUtils.isEmpty(query)) {
            setQuery(null);
            return;
        }
        if (query.equals(mSearchQuery)) {
            return;
        }
        setQuery(query);
    }

    protected void setQuery(String query) {
        mSearchQuery = query;
        mAdapter.setSearchQuery(query);
        mLoaderManager.restartLoader(LOADER_DEFAULT, null, this);
    }

    private void initUI() {
        LayoutInflater.from(getContext())
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
        mList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

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
            SmartTableColumn column = mSmartTableProvider.getColumn(i);

            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = column.getLayoutWeight();

            boolean isSortable = column.isSortable();

            SmartTableHeader header = new SmartTableHeader(mHeader.getContext());
            header.setLayoutParams(lp);
            header.setSortable(isSortable);
            header.setText(column.getTitle(getContext()));
            header.setOnHeaderClickListener(this);
            header.setColumnInfo(column);
            if ((!defaultSortSet) && (isSortable)) {
                header.setSortDirection(SORT_ASC);
                mSortColumn = column.getSortName();
                defaultSortSet = true;
            }

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
        mAdapter.setOnItemClickListener(this);

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

    public void setCanonizer(StringCanonizer canonizer) {
        mCanonizer = canonizer;
    }

    public StringCanonizer getCanonizer() {
        return mCanonizer;
    }

    public List<TableExporter> getTableExporters() {
        return mTableExporters;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    protected String[] getProjection() {
        final int columnCount = mSmartTableProvider.getColumnCount();
        String[] projection = new String[columnCount + 1];
        for (int i = 0; i < columnCount; i++) {
            projection[i]= mSmartTableProvider.getColumn(i).getName();
            //projection[i]= String.format(US, "%s AS st_col_%d", mSmartTableProvider.getColumn(i).getName(), i);
        }
        projection[columnCount] = BaseColumns._ID;
        return projection;
    }

    protected String getSelection() {
        StringBuilder sb = new StringBuilder();

        // Default selecion
        String defaultSelection = mSmartTableProvider.getDefaultSelection();
        if (!TextUtils.isEmpty(defaultSelection)) {
            sb.append('(')
                .append(defaultSelection)
                .append(')');
        }

        // Search Queries
        if (!TextUtils.isEmpty(mSearchQuery) && (mSmartTableProvider.isSearchable())) {
            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append('(');
            for (int column = 0; column < mSmartTableProvider.getColumnCount(); column++) {
                if (column > 0) {
                    sb.append(" OR ");
                }
                sb.append(mSmartTableProvider.getColumn(column).getName()).append(" LIKE ?");
            }
            sb.append(')');
        }

        return sb.toString();
    }

    protected String[] getSelectionArgs() {
        ArrayList<String> args = new ArrayList<>();

        // Default selection
        String[] defaultSelectionArgs = mSmartTableProvider.getDefaultSelectionArgs();
        if (defaultSelectionArgs != null) {
            for (int i = 0 ; i < defaultSelectionArgs.length; i++) {
                args.add(defaultSelectionArgs[i]);
            }
        }

        // Search
        if (!TextUtils.isEmpty(mSearchQuery) && (mSmartTableProvider.isSearchable())) {
            for (int column = 0; column < mSmartTableProvider.getColumnCount(); column++) {
                args.add("%" + mSearchQuery + "%");
            }
        }

        return args.toArray(new String[args.size()]);
    }

    protected String getOrder() {
        StringBuilder builder = new StringBuilder();
        builder.append(mSortColumn);
        if (mSortOrder == SORT_DESC) {
            builder.append(" DESC");
        }
        return builder.toString();
    }

    public void setOnRowClickedListener(OnRowClickedListener listener) {
        mOnRowClickedListener = listener;
    }

    @Override
    public void onItemClick(Cursor cursor) {
        if (mOnRowClickedListener != null) {
            mOnRowClickedListener.onRowClicked(cursor);
        }
    }

    @Override
    public void onHeaderClick(SmartTableHeader view) {
        for (int i = 0; i < mHeader.getChildCount(); i++) {
            SmartTableHeader header = (SmartTableHeader) mHeader.getChildAt(i);
            if (header != view) {
                header.setSortDirection(SORT_NONE);
            } else {
                mSortOrder = header.getSortDirection();
            }
        }
        mLoaderManager.restartLoader(LOADER_DEFAULT, null, this);
    }

    @Override
    public void onFilterClick(SmartTableHeader view) {
        int column = getHeaderPosition(view);
        view.setFilterUsed(true);
    }

    protected int getHeaderPosition(SmartTableHeader view) {
        for (int column = 0; column < mHeader.getChildCount(); column++) {
            SmartTableHeader header = (SmartTableHeader) mHeader.getChildAt(column);
            if (header != view) {
                return column;
            }
        }
        return -1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                mSmartTableProvider.getUri(),
                getProjection(),
                getSelection(),
                getSelectionArgs(),
                getOrder()
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    public interface OnRowClickedListener {
        void onRowClicked(Cursor cursor);
    }
}
