package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.widget.TextView;

/**
 * Created by frogggias on 29.06.15.
 */
public class TextColumnFormatter extends ColumnFormatter {

    private static final String TAG = TextColumnFormatter.class.getSimpleName();

    @Override
    public void setContent(TextView textView, Cursor cursor, String columnName) {
        textView.setText(getString(cursor, columnName));
    }

    @Override
    public int getContentTextAlign() {
        return ALIGNMENT_LEFT;
    }

    @Override
    public String getAsText(Cursor cursor, String columnName) {
        return null;
    }
}
