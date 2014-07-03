package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.mrane.zoomview.CampusMapView;

public class MainActivity extends ActionBarActivity {
	private static MainActivity mMainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setmMainActivity(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static MainActivity getmMainActivity() {
		return mMainActivity;
	}

	public static void setmMainActivity(MainActivity mMainActivity) {
		MainActivity.mMainActivity = mMainActivity;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnItemClickListener {

		ArrayAdapter<String> adapter;
		HashMap<String, Marker> data;
		View rootView;
		CampusMapView imageView;
		AutoCompleteTextView textView;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			imageView = (CampusMapView) rootView.findViewById(R.id.imageView);
			imageView.setImageAsset("map.png");
			Locations mLocations = new Locations();
			data = mLocations.data;
			imageView.setData(data);
			Set<String> keys = data.keySet();
			String[] KEYS = keys.toArray(new String[keys.size()]);

			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_dropdown_item_1line, KEYS);
			textView = (AutoCompleteTextView) rootView
					.findViewById(R.id.search);
			textView.setAdapter(adapter);
			textView.setOnItemClickListener(this);

			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			setNewMarker(arg2);

		}

		private void setNewMarker(int arg2) {
			String key = adapter.getItem(arg2);
			Marker marker = data.get(key);
			imageView.removeHighlightedMarkers();
			imageView.goToMarker(marker);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
			textView.clearFocus();
		}
	}

}
