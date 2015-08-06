package com.frogggias.smarttable.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.activity.SmartTableSearchableActivity;
import com.frogggias.smarttable.export.TableExporter;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.utils.MaterialHelper;
import com.frogggias.smarttable.view.SmartTable;

import java.util.List;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableFragment
        extends Fragment
        implements SmartTable.OnRowClickedListener, SmartTableSearchable {

    private static final String TAG = SmartTableFragment.class.getSimpleName();

    private static final String ARG_PROVIDER = "provider";

    /* DATA */
    private SmartTableProvider mProvider;

    /* VIEW */
    private SmartTable mSmartTable;
    private MenuItem mSearchMenuItem;

    public static final SmartTableFragment newInstance(SmartTableProvider mProvider) {
        SmartTableFragment fragment = new SmartTableFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_PROVIDER, mProvider);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProvider = (SmartTableProvider) getArguments().getSerializable(ARG_PROVIDER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smarttable, container, false);
        mSmartTable = (SmartTable) view.findViewById(R.id.table);
        mSmartTable.setTableProvider(mProvider);
        mSmartTable.setLoaderManager(getLoaderManager());
        mSmartTable.setOnRowClickedListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.default_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        setMenuIconsTint(menu, MaterialHelper.isLight(MaterialHelper.getPrimaryColor(getActivity())) ?
                Color.BLACK : Color.WHITE);
        mSearchMenuItem = menu.findItem(R.id.search);
        mSearchMenuItem.setVisible(mProvider.isSearchable());
    }

    public void setMenuIconsTint(Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu submenu = item.getSubMenu();
                setMenuIconsTint(submenu, color);
            }
            Drawable d = item.getIcon();
            d.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.export) {
            export();
            return true;
        } else if (itemId == R.id.email) {
            sendByEmail();
            return true;
        } else if (itemId == R.id.search) {
            openSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRowClicked(Cursor cursor) {
        Log.d(TAG, "Row clicked: " + cursor.toString());
    }

    protected void export() {
        List<TableExporter> exporters = mSmartTable.getTableExporters();
        if (exporters.size() == 1) {
            exporters.get(0).export(getActivity(), mProvider.toString(), mProvider, mSmartTable.getCursor());
        } else {
            // TODO exporter chooser
        }

    }

    protected void sendByEmail() {
        List<TableExporter> exporters = mSmartTable.getTableExporters();
        TableExporter exporter;
        if (exporters.size() == 1) {
            exporter = exporters.get(0);
        } else {
            // TODO exporter chooser
            exporter = exporters.get(0);
        }

        TableExporter.OnExportDoneListener onExportDoneListener = new TableExporter.OnExportDoneListener() {
            @Override
            public void onExportDone(Uri uri) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_SUBJECT, mProvider.toString());
                    intent.putExtra(Intent.EXTRA_TEXT, mProvider.toString());
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, getString(R.string.send_by_email)));
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        };
        exporter.setOnExportDoneListener(onExportDoneListener);

        exporter.export(getActivity(), mProvider.toString(), mProvider, mSmartTable.getCursor(), true);
    }

    protected void openSearch() {
        if (getActivity() instanceof SmartTableSearchableActivity) {
            ((SmartTableSearchableActivity) getActivity()).openSearch();
            mSearchMenuItem.setVisible(false);
        }
    }

    @Override
    public void setSearchQuery(String query) {
        mSmartTable.setSearchQuery(query);
    }

    @Override
    public void cancelSearch() {
        mSearchMenuItem.setVisible(true);
    }
}
