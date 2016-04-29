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
    public void setContent(TextView textView, Cursor cursor, int columnIndex) {
        textView.setText(getString(cursor, columnIndex));
    }

    @Override
    public void setContent(TextView textView, Cursor cursor, int columnIndex, String query) {
        textView.setText(getSearchString(cursor, columnIndex, query));
    }

    @Override
    public int getContentTextAlign() {
        return ALIGNMENT_LEFT;
    }

    @Override
    public String getAsText(Cursor cursor, int columnIndex) {
        return getString(cursor, columnIndex);
    }

    @Override
    protected SpannableString getSearchString(Cursor cursor, int columnIndex, String searchString) {
        String text = getAsText(cursor, columnIndex);
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
