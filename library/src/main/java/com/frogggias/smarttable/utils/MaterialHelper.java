package com.frogggias.smarttable.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import com.frogggias.smarttable.R;

/**
 * Created by frogggias on 29.06.15.
 */
public class MaterialHelper {

    private static final String TAG = MaterialHelper.class.getSimpleName();

    public static @ColorInt int getPrimaryColor(Context context) {
        return getColor(context, R.attr.colorPrimary);
    }

    public static @ColorInt int getPrimaryColorDark(Context context) {
        return getColor(context, R.attr.colorPrimaryDark);
    }

    public static @ColorInt int getAccentColor(Context context) {
        return getColor(context, R.attr.colorAccent);
    }

    private static @ColorInt int getColor(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{ attr });
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }
}
