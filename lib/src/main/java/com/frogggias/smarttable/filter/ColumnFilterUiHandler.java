package com.frogggias.smarttable.filter;

import android.content.Context;
import android.view.View;

/**
 * Created by frogggias on 10.07.15.
 */
public abstract class ColumnFilterUiHandler {

    private static final String TAG = ColumnFilterUiHandler.class.getSimpleName();

    public abstract View createView(Context context);

    public abstract View bindView(Context context);

}
