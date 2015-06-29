package com.frogggias.smarttable.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.utils.MaterialHelper;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class SmartTableActivity extends AppCompatActivity {

    private static final String TAG = SmartTableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarttable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(MaterialHelper.getPrimaryColor(this));
    }
}
