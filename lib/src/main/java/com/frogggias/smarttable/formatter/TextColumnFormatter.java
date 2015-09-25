package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.widget.TextView;

/**
 * Created by frogggias on 29.06.15.
 */
public class TextColumnFormatter extends ColumnFormatter {

    private static final String TAG = TextColumnFormatter.class.getSimpleName();

    private static final StyleSpan BOLD = new StyleSpan(Typeface.BOLD);

    @Override
    public void setContent(TextView textView, Cursor cursor, String columnName) {
        textView.setText(getString(cursor, columnName));
    }

    @Override
    public void setContent(TextView textView, Cursor cursor, String columnName, String query) {
        textView.setText(getSearchString(cursor, columnName, query));
    }

    @Override
    public int getContentTextAlign() {
        return ALIGNMENT_LEFT;
    }

    @Override
    public String getAsText(Cursor cursor, String columnName) {
        return getString(cursor, columnName);
    }

    @Override
    protected SpannableString getSearchString(Cursor cursor, String columnName, String searchString) {
        String text = getAsText(cursor, columnName);
        if (text == null) {
            text = "--";
        }
        SpannableString spannable = new SpannableString(text);
        if ((!TextUtils.isEmpty(searchString)) && (getCanonizer().canonize(text).contains(searchString))) {
            int start = getCanonizer().canonize(text).indexOf(searchString);
            spannable.setSpan(BOLD, start, start + searchString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannable;
    }
}
