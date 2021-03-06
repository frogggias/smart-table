package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.view.Gravity;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Created by frogggias on 13.07.15.
 */
public class NumberColumnFormatter extends TextColumnFormatter {

    private static final String TAG = NumberColumnFormatter.class.getSimpleName();

    private NumberFormat nf = NumberFormat.getInstance();

    @Override
    public void setContent(TextView textView, Cursor cursor, String columnName) {
        textView.setText(nf.format(getDouble(cursor, columnName)));
        textView.setGravity(Gravity.RIGHT);
    }

    @Override
    public void setContent(TextView textView, Cursor cursor, String columnName, String query) {
        setContent(textView, cursor, columnName);
    }
}
