package com.s16.filechooser.fragment;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import com.s16.filechooser.R;

/**
 * Created by SMM on 9/28/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.settings);
    }
}