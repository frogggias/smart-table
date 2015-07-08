package com.frogggias.smarttable.formatter;

import android.database.Cursor;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
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
        SpannableString spannable = new SpannableString(text);
        if (getCanonizer().canonize(text).contains(searchString)) {
            int start = text.indexOf(searchString);
            spannable.setSpan(BOLD, start, searchString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannable;
    }
}
