package com.frogggias.smarttable.helper;

import android.content.Context;

import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 10.07.15.
 */
public class SmartTableExtractor {

    private static final String TAG = SmartTableExtractor.class.getSimpleName();

    public static String[] getColumnTitles(SmartTableProvider provider) {
        String[] data = new String[provider.getColumnCount()];
        for (int column = 0; column < provider.getColumnCount(); column++) {
            data[column]= provider.getColumn(column).getTitle();
        }
        return data;
    }

    public static String[] getColumnTitles(SmartTableProvider provider, Context context) {
        String[] data = new String[provider.getColumnCount()];
        for (int column = 0; column < provider.getColumnCount(); column++) {
            data[column]= provider.getColumn(column).getTitle(context);
        }
        return data;
    }

    public static String[] getColumnNames(SmartTableProvider provider) {
        String[] data = new String[provider.getColumnCount()];
        for (int column = 0; column < provider.getColumnCount(); column++) {
            data[column]= provider.getColumn(column).getName();
        }
        return data;
    }
}
