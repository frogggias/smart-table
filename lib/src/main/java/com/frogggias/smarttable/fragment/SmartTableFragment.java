package com.frogggias.smarttable.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.activity.SmartTableSearchableActivity;
import com.frogggias.smarttable.export.ExportFragment;
import com.frogggias.smarttable.export.TableExporter;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.utils.MaterialHelper;
import com.frogggias.smarttable.view.SmartTable;

import java.io.File;
import java.util.List;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableFragment
        extends Fragment
        implements SmartTable.OnRowClickedListener, SmartTableSearchable {

    private static final String TAG = SmartTableFragment.class.getSimpleName();

    private static final String ARG_PROVIDER = "provider";
    private static final String TAG_EXPORT_FRAGMENT = "export-fragment";
    private static final String TAG_EMAIL_EXPORT_FRAGMENT = "email-export-fragment";

    private static final int REQUEST_CODE_STORAGE_EXPORT = 101;
    private static final int REQUEST_CODE_STORAGE_EMAIL = 102;

    /* DATA */
    private SmartTableProvider mProvider;

    /* VIEW */
    private SmartTable mSmartTable;
    private MenuItem mSearchMenuItem;

    public static SmartTableFragment newInstance(SmartTableProvider mProvider) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smarttable, container, false);
        mSmartTable = view.findViewById(R.id.table);
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

    private boolean hasExternalStoragePermission(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    protected void export() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        if (!hasExternalStoragePermission(context)) {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_STORAGE_EXPORT);
            return;
        }
        List<TableExporter> exporters = mSmartTable.getTableExporters();
        TableExporter exporter;
        if (exporters.size() == 1) {
            exporter = exporters.get(0);
        } else {
            // TODO exporter chooser
            exporter = exporters.get(0);
        }

        TableExporter.Data data = new TableExporter.Data(getActivity(), mProvider.toString(), mProvider, mSmartTable.getCursor());
        ExportFragment exportFragment = ExportFragment.newInstance(exporter, data);
        ExportFragment.OnExportCompletedListener listener = new ExportFragment.OnExportCompletedListener() {
            @Override
            public void onExportCompleted(Uri uri) {
                Fragment exportFragment = getChildFragmentManager().findFragmentByTag(TAG_EXPORT_FRAGMENT);
                if (exportFragment != null) {
                    getChildFragmentManager().beginTransaction()
                            .remove(exportFragment)
                            .commit();
                }
                Context context = getContext();
                if (context == null || uri == null) {
                    return;
                }
                createOpenFileDialog(context, uri).show();
            }
        };
        exportFragment.setListener(listener);
        performExportIfPossible(exportFragment, TAG_EXPORT_FRAGMENT);
    }

    private AlertDialog createOpenFileDialog(final Context context, final Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        // TODO here also maybe?
        File file = new File(Uri.decode(uri.toString()));

        String message = context.getString(R.string.exporter_csv_success, file.getName());

        builder.setMessage(message);
        builder.setPositiveButton(R.string.exporter_csv_open_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFile(context, uri);
            }
        });
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private void openFile(Context context, Uri uri) {
        if (context != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setDataAndType(uri, "text/csv");
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.exporter_csv_open_file)));
        }
    }

    protected void sendByEmail() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        if (!hasExternalStoragePermission(context)) {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_STORAGE_EMAIL);
            return;
        }
        List<TableExporter> exporters = mSmartTable.getTableExporters();
        TableExporter exporter;
        if (exporters.size() == 1) {
            exporter = exporters.get(0);
        } else {
            // TODO exporter chooser
            exporter = exporters.get(0);
        }

        TableExporter.Data data = new TableExporter.Data(getActivity(), mProvider.toString(), mProvider, mSmartTable.getCursor());
        ExportFragment exportFragment = ExportFragment.newInstance(exporter, data);
        ExportFragment.OnExportCompletedListener listener = new ExportFragment.OnExportCompletedListener() {
            @Override
            public void onExportCompleted(Uri uri) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_SUBJECT, mProvider.toString());
                    intent.putExtra(Intent.EXTRA_TEXT, mProvider.toString());
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, getString(R.string.send_by_email)));
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        };
        exportFragment.setListener(listener);
        performExportIfPossible(exportFragment, TAG_EMAIL_EXPORT_FRAGMENT);
    }

    private void performExportIfPossible(@NonNull ExportFragment fragment, String tag) {
        Fragment existing = getChildFragmentManager().findFragmentByTag(tag);
        if (existing == null) {
            getChildFragmentManager().beginTransaction()
                    .add(fragment, tag)
                    .commit();
        } else {
            Log.d(TAG, "Export of kind '" + tag + "' is already in progress");
        }
    }

    protected void openSearch() {
        if (getActivity() instanceof SmartTableSearchableActivity) {
            ((SmartTableSearchableActivity) getActivity()).openSearch();
            mSearchMenuItem.setVisible(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_EXPORT:
            case REQUEST_CODE_STORAGE_EMAIL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedWithPermissionGranted(requestCode);
                    return;
                } else if (shouldShowRequestPermissionRationale(permissions[0])) {
                    // TODO show dialog
                } else {
                    // TODO what here?
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        // TODO consider hiding items from options menu when user choose not to ask them anymore.
    }

    private void proceedWithPermissionGranted(int originalRequestCode) {
        switch (originalRequestCode) {
            case REQUEST_CODE_STORAGE_EXPORT:
                export();
                break;
            case REQUEST_CODE_STORAGE_EMAIL:
                sendByEmail();
                break;
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
