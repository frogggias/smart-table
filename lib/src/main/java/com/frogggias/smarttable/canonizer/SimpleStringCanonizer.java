package com.frogggias.smarttable.canonizer;

import java.text.Normalizer;

/**
 * Created by frogggias on 08.07.15.
 */
public class SimpleStringCanonizer implements StringCanonizer {

    private static final String TAG = SimpleStringCanonizer.class.getSimpleName();

    @Override
    public String canonize(String text) {
        String decomposed = Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD);
        return decomposed.replaceAll("[\\p{InCombiningDiacriticalMarks}+]", "");
    }
}
