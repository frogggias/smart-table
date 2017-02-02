package com.frogggias.smarttable.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.frogggias.smarttable.R;
import com.frogggias.smarttable.fragment.SmartTableFragment;
import com.frogggias.smarttable.fragment.SmartTableSearchable;
import com.frogggias.smarttable.provider.SmartTableProvider;
import com.frogggias.smarttable.utils.MaterialHelper;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class SmartTableActivity
        extends AppCompatActivity
        implements SmartTableSearchableActivity {

    private static final String TAG = SmartTableActivity.class.getSimpleName();

    private boolean mHasHomeActionBarButton = false;

    /* CONTROLLER */
    SmartTableFragment mContentFragment;

    /* VIEW */
    View mSearchWrapper;
    EditText mSearchText;
    ImageView mSearchClearIcon;

    protected abstract SmartTableProvider getSmartTableProvider();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId() == android.R.id.home) && (isSearchOpen())) {
            closeSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarttable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        @ColorInt int primaryColor = MaterialHelper.getPrimaryColor(this);
        @ColorInt int textColor = MaterialHelper.isLight(primaryColor) ? Color.BLACK : Color.WHITE;
        toolbar.setBackgroundColor(primaryColor);

        mSearchWrapper = findViewById(R.id.search_container);
        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mSearchText, InputMethodManager.SHOW_FORCED);
                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
                }
            }
        });

        mSearchClearIcon = (ImageView) findViewById(R.id.search_clear);
        mSearchClearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear();
            }
        });


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

    protected void searchClear() {
        mSearchText.setText("");
    }

    protected void textChanged(String text) {
        if (mContentFragment instanceof SmartTableSearchable) {
            ((SmartTableSearchable) mContentFragment).setSearchQuery(text);
        }
    }

    @Override
    public void openSearch() {
        if (isSearchOpen()) {
            return;
        }
        if (getSmartTableProvider().isSearchable()) {
            mSearchWrapper.setVisibility(View.VISIBLE);
            mSearchText.requestFocus();
        }
        mHasHomeActionBarButton = (getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) > 0x0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpen()) {
            closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    public boolean isSearchOpen() {
        return mSearchWrapper.getVisibility() == View.VISIBLE;
    }

    private void closeSearch() {
        searchClear();
        mSearchWrapper.setVisibility(View.GONE);
        if (mContentFragment instanceof SmartTableSearchable) {
            ((SmartTableSearchable) mContentFragment).cancelSearch();
        }
        if (!mHasHomeActionBarButton) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
}
