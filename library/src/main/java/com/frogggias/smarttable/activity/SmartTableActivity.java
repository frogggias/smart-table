package com.frogggias.smarttable.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class SmartTableActivity extends FragmentActivity {

    private static final String TAG = SmartTableActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
