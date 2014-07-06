package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mrane.zoomview.CampusMapView;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, TextWatcher, OnEditorActionListener {
	private static MainActivity mMainActivity;
	boolean isOpened = false;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private IndexFragment indexFragment;
	public RelativeLayout placeCard;
	public TextView placeNameTextView;
	public AutoCompleteTextView textView;
	public HashMap<String, Marker> data;
	public FragmentTransaction transaction;
	public CampusMapView campusMapView;
	public ImageButton searchIcon;
	public ImageButton removeIcon;
	public ImageButton indexIcon;
	public LocationManager locationManager;
	public LocationListener locationListener;
	private boolean itemSelected = false;
	private boolean isFirstFragment = true;
	private final String firstStackTag = "FIRST_TAG";
	private final int MSG_ANIMATE = 1;
	private final long DELAY_ANIMATE = 75;
	private boolean inIndexMode = false;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_ANIMATE:
				resultMarker((String)msg.obj);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmMainActivity(this);

		getSupportActionBar().hide();

		setContentView(R.layout.activity_main);
		
		placeCard  = (RelativeLayout) findViewById(R.id.place_card);
		placeNameTextView = (TextView) findViewById(R.id.place_name);

		Locations mLocations = new Locations();
		data = mLocations.data;
		Set<String> keys = data.keySet();
		String[] KEYS = keys.toArray(new String[keys.size()]);

		adapter = new ArrayAdapter<String>(this, R.layout.row_layout,
				R.id.label, KEYS);
		textView = (CustomAutoCompleteView) findViewById(R.id.search);
		textView.setAdapter(adapter);
		textView.addTextChangedListener(this);
		textView.setOnEditorActionListener(this);

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
		setAutoCompleteText(key);
		fragmentManager.popBackStack();
		
		Message msg = mHandler.obtainMessage(MSG_ANIMATE, key);
		mHandler.sendMessageDelayed(msg, DELAY_ANIMATE);
		
		inIndexMode = false;
	}

	private void setAutoCompleteText(String key) {
		textView.dismissDropDown();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
		textView.setText(key);
		textView.clearFocus();
	}

	public void resultMarker(String key) {
		Marker marker = mMainActivity.data.get(key);
		campusMapView.removeHighlightedMarkers();
		campusMapView.goToMarker(marker);
		showCard(marker);
	}
	
	public void removeMarker(){
		dismissCard();
		campusMapView.removeHighlightedMarkers();
	}

	public void searchClick(View v) {
		Toast t = Toast.makeText(mMainActivity, "search", Toast.LENGTH_LONG);
		t.show();
	}

	public void indexClick(View v) {
		if (!inIndexMode) {
			putFragment(indexFragment);
			setAutoCompleteText(null);
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
		removeMarker();
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

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch(actionId){
		case EditorInfo.IME_ACTION_SEARCH:
			onItemClick(null, v, 0, 0);
		}
		return false;
	}
	
	public void showCard(Marker marker){
		placeNameTextView.setText(marker.name);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int topMargin = 0;
		float density = getResources().getDisplayMetrics().density;
		topMargin = campusMapView.getHeight() - placeNameTextView.getPaddingTop()*2 - (int)(72*density);
		params.setMargins(0, topMargin, 0, 0);
		params.height = (int)(96*density);
		placeCard.setLayoutParams(params);
		placeCard.setVisibility(View.VISIBLE);
	}
	
	public void dismissCard(){
		placeCard.setVisibility(View.GONE);
	}

	
}
