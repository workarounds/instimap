package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import com.mrane.zoomview.CampusMapView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {
	private static MainActivity mMainActivity;
	boolean isOpened = false;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	AutoCompleteTextView textView;
	HashMap<String, Marker> data;
	FragmentTransaction transaction;
	CampusMapView imageView;
	private boolean itemSelected = false;
	private boolean isFirstFragment = true;
	private final String firstStackTag = "FIRST_TAG";

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

		imageView = (CampusMapView) findViewById(R.id.imageView);
		imageView.setImageAsset("map.png");
		imageView.setData(mMainActivity.data);

		fragmentManager = getSupportFragmentManager();
		listFragment = new ListFragment();

	}

	public static MainActivity getmMainActivity() {
		return mMainActivity;
	}

	public static void setmMainActivity(MainActivity mMainActivity) {
		MainActivity.mMainActivity = mMainActivity;
	}

	public void autoCompleteFocusChanged(boolean focused) {
		LinearLayout headerContainer;
		headerContainer = (LinearLayout) findViewById(R.id.header_container);
		if (focused) {
			itemSelected = false;
			headerContainer.setBackgroundColor(Color.DKGRAY);
			putFragment(listFragment);
		} else {
			if (itemSelected) {
				fragmentManager.popBackStack(firstStackTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
		setAutoCompleteText(key);
		fragmentManager.popBackStack();
		resultMarker(key);
	}

	private void setAutoCompleteText(String key) {
		textView.setText(key);
		textView.clearFocus();
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
	}

	public void resultMarker(String key) {
		Log.d("testing", "resultMarker");
		Marker marker = mMainActivity.data.get(key);
		imageView.removeHighlightedMarkers();
		imageView.goToMarker(marker);
	}

}
