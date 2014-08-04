package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class SettingsActivity extends ActionBarActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_settings);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Display the fragment as the main content.
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.settings_fragment, new SettingsFragment())
//                .commit();
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            this.onBackPressed();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_slide_out_left);
	}
}
