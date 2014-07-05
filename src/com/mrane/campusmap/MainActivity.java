package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mrane.zoomview.CampusMapView;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, TextWatcher {
	private static MainActivity mMainActivity;
	boolean isOpened = false;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private IndexFragment indexFragment;
	AutoCompleteTextView textView;
	HashMap<String, Marker> data;
	FragmentTransaction transaction;
	CampusMapView campusMapView;
	ImageButton searchIcon;
	ImageButton removeIcon;
	ImageButton indexIcon;
	LocationManager locationManager;
	LocationListener locationListener;
	private boolean itemSelected = false;
	private boolean isFirstFragment = true;
	private final String firstStackTag = "FIRST_TAG";
	private boolean inIndexMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmMainActivity(this);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getSupportActionBar().hide();

		setContentView(R.layout.activity_main);

		Locations mLocations = new Locations();
		data = mLocations.data;
		Set<String> keys = data.keySet();
		String[] KEYS = keys.toArray(new String[keys.size()]);

		adapter = new ArrayAdapter<String>(this, R.layout.row_layout,
				R.id.label, KEYS);
		textView = (CustomAutoCompleteView) findViewById(R.id.search);
		textView.setAdapter(adapter);
		textView.addTextChangedListener(this);

		campusMapView = (CampusMapView) findViewById(R.id.campusMapView);
		campusMapView.setImageAsset("map.png");
		campusMapView.setData(mMainActivity.data);

		searchIcon = (ImageButton) findViewById(R.id.search_icon);
		removeIcon = (ImageButton) findViewById(R.id.remove_icon);
		indexIcon = (ImageButton) findViewById(R.id.index_icon);

		fragmentManager = getSupportFragmentManager();
		listFragment = new ListFragment();
		indexFragment = new IndexFragment();
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

//		boolean enabled = locationManager
//				  .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//				if (!enabled) {
//				  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//				  startActivity(intent);
//				} 
		// Define a listener that responds to location updates
		locationListener = new LocationListenerClass();
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

	}

	public static MainActivity getmMainActivity() {
		return mMainActivity;
	}

	public static void setmMainActivity(MainActivity mMainActivity) {
		MainActivity.mMainActivity = mMainActivity;
	}

	public void autoCompleteFocusChanged(boolean focused) {
		ViewGroup headerContainer;
		headerContainer = (ViewGroup) findViewById(R.id.header_container);
		if (focused) {
			itemSelected = false;
			headerContainer.setBackgroundColor(Color.rgb(208, 208, 208));
			putFragment(listFragment);
			inIndexMode = false;
		} else {
			if (itemSelected) {
				fragmentManager.popBackStack(firstStackTag,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				isFirstFragment = true;
			} else {

			}
			headerContainer.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	private void putFragment(Fragment fragment) {
		transaction = fragmentManager.beginTransaction();
		if (isFirstFragment) {
			transaction.add(R.id.fragment_container, fragment);
			transaction.addToBackStack(firstStackTag);
			transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			isFirstFragment = false;
		} else {
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	public ArrayAdapter<String> getAdapter() {
		return adapter;
	}

	public void setAdapter(ArrayAdapter<String> adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onBackPressed() {
		textView.clearFocus();
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String key = adapter.getItem(arg2);
		setAutoCompleteText(arg3, arg2, key);
		fragmentManager.popBackStack();
		resultMarker(key);
		inIndexMode = false;
	}

	private void setAutoCompleteText(long id, int index, String key) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		CompletionInfo completion = new CompletionInfo(id, index, (CharSequence)key);
		CompletionInfo[] completions = {completion};
		imm.displayCompletions(textView, completions);
		imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
		textView.setText(key);
		textView.clearFocus();
	}

	public void resultMarker(String key) {
		Log.d("testing", "resultMarker");
		Marker marker = mMainActivity.data.get(key);
		campusMapView.removeHighlightedMarkers();
		campusMapView.goToMarker(marker);
	}

	public void searchClick(View v) {
		Toast t = Toast.makeText(mMainActivity, "search", Toast.LENGTH_LONG);
		t.show();
	}

	public void indexClick(View v) {
		if (!inIndexMode) {
			putFragment(indexFragment);
			setAutoCompleteText(0, 0, null);
			indexIcon.setImageResource(R.drawable.ic_action_map);
			inIndexMode = true;
		} else {
			fragmentManager.popBackStack(firstStackTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			isFirstFragment = true;
			inIndexMode = false;
			indexIcon.setImageResource(R.drawable.ic_action_view_as_list);
		}
	}

	public void removeClick(View v) {
		textView.getText().clear();
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String text = arg0.toString();
		if (text.equals(null) || text.equals("")) {
			removeIcon.setVisibility(View.GONE);
			indexIcon.setVisibility(View.VISIBLE);
		} else {
			removeIcon.setVisibility(View.VISIBLE);
			indexIcon.setVisibility(View.GONE);
		}
	}

	
}
