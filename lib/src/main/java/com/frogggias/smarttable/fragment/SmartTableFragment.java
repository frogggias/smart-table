package com.frogggias.smarttable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.frogggias.smarttable.provider.SmartTableProvider;

/**
 * Created by frogggias on 29.06.15.
 */
public class SmartTableFragment extends Fragment {

    private static final String TAG = SmartTableFragment.class.getSimpleName();

    private static final String ARG_PROVIDER = "provider";

    private SmartTableProvider mProvider;

    public static final SmartTableFragment newInstance(SmartTableProvider mProvider) {
        SmartTableFragment fragment = new SmartTableFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_PROVIDER, mProvider);
        fragment.setArguments(args);

        return fragment;
    }

}
