package com.mrane.campusmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class SettingsActivity extends ActionBarActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
