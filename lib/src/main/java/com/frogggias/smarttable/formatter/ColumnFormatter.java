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



    public abstract void setContent(TextView textView, Cursor cursor, int columnIndex);
    public abstract void setContent(TextView textView, Cursor cursor, int columnIndex, String query);

    // Used internally or for data export
    @TextAlignment public abstract int getContentTextAlign();
    public abstract String getAsText(Cursor cursor, int columnIndex);

    protected String getString(Cursor cursor, int columnIndex) {
        return cursor.getString(columnIndex);
    }

    protected abstract SpannableString getSearchString(Cursor cursor, int columnIndex, String searchString);

    protected long getLong(Cursor cursor, int columnIndex) {
        return cursor.getLong(columnIndex);
    }

    protected double getDouble(Cursor cursor, int columnIndex) {
        return cursor.getDouble(columnIndex);
    }

    protected StringCanonizer getCanonizer() {
        return new SimpleStringCanonizer();
    }

}
