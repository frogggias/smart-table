package com.frogggias.smarttable.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.fragment.SmartTableFragment;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.utils.MaterialHelper;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class SmartTableActivity extends AppCompatActivity {

    private static final String TAG = SmartTableActivity.class.getSimpleName();

    SmartTableFragment mContentFragment;

    protected abstract SmartTableProvider getSmartTableProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarttable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        @ColorInt int primaryColor = MaterialHelper.getPrimaryColor(this);
        @ColorInt int textColor = MaterialHelper.isLight(primaryColor) ? Color.BLACK : Color.WHITE;
        toolbar.setBackgroundColor(primaryColor);

        if (savedInstanceState == null) {
            mContentFragment = SmartTableFragment.newInstance(getSmartTableProvider());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mContentFragment)
                    .commit();
        } else {
            mContentFragment = (SmartTableFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.content);
        }
    }
}
