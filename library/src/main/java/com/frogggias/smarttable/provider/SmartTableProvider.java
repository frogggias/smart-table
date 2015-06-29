package com.frogggias.smarttable.provider;

import android.net.Uri;
import android.support.annotation.StringRes;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableProvider {

    private static final String TAG = SmartTableProvider.class.getSimpleName();

    public static final String COLUMN_TYPE_TEXT = "text";

    private final Uri mUri;
    @StringRes
    private String[] mColumnName;
    private int[] mColumnTitleResId;
    private String[] mColumnTitle;
    private String[] mSortColumnName;
    private String[] mSearchColumnName;
    private String[] mColumnType;
    private String[] mColumnTextAlign;
    private boolean[] mSortable;
    private boolean[] mSearchable;

    public SmartTableProvider(Uri uri) {
        mUri = uri;
    }

    public Uri getUri() {
        return mUri;
    }

}
