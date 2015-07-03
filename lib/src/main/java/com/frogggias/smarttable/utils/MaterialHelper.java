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

    public static double getLuminosity(@ColorInt int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        double l1 = 0.2126 * Math.pow(r/255, 2.2) +
                0.7152 * Math.pow(g / 255, 2.2) +
                0.0722 * Math.pow(b / 255, 2.2);

        double l2 = 0.2126 * Math.pow(0 / 255, 2.2) +
                0.7152 * Math.pow(0 / 255, 2.2) +
                0.0722 * Math.pow(0 / 255, 2.2);

        if (l1 > l2) {
            return (l1+0.05) / (l2+0.05);
        } else {
            return (l1+0.05) / (l2+0.05);
        }
    }
}
