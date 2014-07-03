package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mrane.zoomview.CampusMapView;

public class MainActivity extends ActionBarActivity {
	private static MainActivity mMainActivity;
	boolean isOpened = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getSupportActionBar().hide();

		setContentView(R.layout.activity_main);
		setmMainActivity(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
		ListView listView;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			imageView = (CampusMapView) rootView.findViewById(R.id.imageView);
			imageView.setImageAsset("map.png");
			listView = (ListView) rootView.findViewById(R.id.suggestion_list);
			Locations mLocations = new Locations();
			data = mLocations.data;
			imageView.setData(data);
			Set<String> keys = data.keySet();
			String[] KEYS = keys.toArray(new String[keys.size()]);

			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.row_layout, R.id.label, KEYS);
			textView = (CustomAutoCompleteView) rootView
					.findViewById(R.id.search);
			textView.setAdapter(adapter);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
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
			textView.setText(key);;
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
			textView.clearFocus();
			imageView.removeHighlightedMarkers();
			imageView.goToMarker(marker);
		}
	}

	public void autoCompleteFocusChanged(boolean focused) {
		LinearLayout linear = (LinearLayout) findViewById(R.id.list_background);
		Log.d("testing", " I'm being called ");
		if (focused) {
			linear.setVisibility(View.VISIBLE);
		} else {
			linear.setVisibility(View.GONE);
		}
	}

}
