package com.muhammed.iqbal.telugu.geeks;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.muhammed.iqbal.telugu.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the preferences from  an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
