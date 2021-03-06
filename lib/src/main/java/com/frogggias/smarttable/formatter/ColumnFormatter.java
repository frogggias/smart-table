package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.support.annotation.IntDef;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.frogggias.smarttable.canonizer.SimpleStringCanonizer;
import com.frogggias.smarttable.canonizer.StringCanonizer;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by frogggias on 29.06.15.
 */
public abstract class ColumnFormatter implements Serializable {

    @IntDef({View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_TEXT_END, View.TEXT_ALIGNMENT_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    @interface TextAlignment {}

    public static final int ALIGNMENT_LEFT = View.TEXT_ALIGNMENT_TEXT_START;
    public static final int ALIGNMENT_RIGHT = View.TEXT_ALIGNMENT_TEXT_END;
    public static final int ALIGNMENT_CENTER = View.TEXT_ALIGNMENT_CENTER;



    public abstract void setContent(TextView textView, Cursor cursor, String columnName);
    public abstract void setContent(TextView textView, Cursor cursor, String columnName, String query);

    // Used internally or for data export
    @TextAlignment public abstract int getContentTextAlign();
    public abstract String getAsText(Cursor cursor, String columnName);

    protected String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    protected abstract SpannableString getSearchString(Cursor cursor, String columnName, String searchString);

    protected long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    protected double getDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    protected StringCanonizer getCanonizer() {
        return new SimpleStringCanonizer();
    }

}
