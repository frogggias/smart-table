package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class ColumnFormatter {

    @IntDef({View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_TEXT_END, View.TEXT_ALIGNMENT_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    @interface TextAlignment {}

    public static final int ALIGNMENT_LEFT = View.TEXT_ALIGNMENT_TEXT_START;
    public static final int ALIGNMENT_RIGHT = View.TEXT_ALIGNMENT_TEXT_END;
    public static final int ALIGNMENT_CENTER = View.TEXT_ALIGNMENT_CENTER;

    public abstract void setContent(TextView textView, Cursor cursor, String columnName);

    // Used internally or for data export
    @TextAlignment public abstract int getTitleTextAlign();
    @TextAlignment public abstract int getContentTextAlign();
    public abstract String getAsText(Cursor cursor, String columnName);

    protected String getString(Cursor cursor, String columnName) {
        if (!validate(cursor, columnName)) {
            return "";
        }

        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    protected long getLong(Cursor cursor, String columnName) {
        if (!validate(cursor, columnName)) {
            return 0L;
        }

        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    protected boolean validate(Cursor cursor, String columnName) {
        if ((cursor == null) || (cursor.isAfterLast())) {
            return false;
        }
        try {
            cursor.getColumnIndexOrThrow(columnName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
