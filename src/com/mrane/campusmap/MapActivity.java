package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mrane.zoomview.CampusMapView;

public class MapActivity extends ActionBarActivity implements TextWatcher,
		OnEditorActionListener, OnItemClickListener {
	private static MapActivity mainActivity;
	boolean isOpened = false;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private IndexFragment indexFragment;
	public RelativeLayout placeCard;
	public TextView placeNameTextView;
	public AutoCompleteTextView editText;
	public HashMap<String, Marker> data;
	public FragmentTransaction transaction;
	public CampusMapView campusMapView;
	public ImageButton searchIcon;
	public ImageButton removeIcon;
	public ImageButton indexIcon;
	public ImageButton mapIcon;
	public ImageButton locateIcon;
	public LocationManager locationManager;
	public LocationListener locationListener;
	private boolean itemSelected = false;
	private boolean isFirstFragment = true;
	private final String firstStackTag = "FIRST_TAG";
	private final int MSG_ANIMATE = 1;
	private final long DELAY_ANIMATE = 75;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ANIMATE:
				// call map showing function
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainActivity(this);

		getSupportActionBar().hide();

		setContentView(R.layout.activity_main);

		placeCard = (RelativeLayout) findViewById(R.id.place_card);
		placeNameTextView = (TextView) findViewById(R.id.place_name);

		Locations mLocations = new Locations();
		data = mLocations.data;
		Set<String> keys = data.keySet();
		String[] KEYS = keys.toArray(new String[keys.size()]);

		adapter = new ArrayAdapter<String>(this, R.layout.row_layout,
				R.id.label, KEYS);
		editText = (CustomAutoCompleteView) findViewById(R.id.search);
		editText.setAdapter(adapter);
		editText.addTextChangedListener(this);
		editText.setOnEditorActionListener(this);

		campusMapView = (CampusMapView) findViewById(R.id.campusMapView);
		campusMapView.setImageAsset("map.png");
		campusMapView.setData(data);

		searchIcon = (ImageButton) findViewById(R.id.search_icon);
		removeIcon = (ImageButton) findViewById(R.id.remove_icon);
		indexIcon = (ImageButton) findViewById(R.id.index_icon);
		mapIcon = (ImageButton) findViewById(R.id.map_icon);
		locateIcon = (ImageButton) findViewById(R.id.locate_icon);

		fragmentManager = getSupportFragmentManager();
		listFragment = new ListFragment();
		indexFragment = new IndexFragment();

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// boolean enabled = locationManager
		// .isProviderEnabled(LocationManager.GPS_PROVIDER);
		//
		// if (!enabled) {
		// Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);
		// }
		// Define a listener that responds to location updates
		locationListener = new LocationListenerClass();
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0, locationListener);

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	public static MapActivity getMainActivity() {
		return mainActivity;
	}

	public static void setMainActivity(MapActivity mainActivity) {
		MapActivity.mainActivity = mainActivity;
	}

	public void autoCompleteFocusChanged(boolean focused) {

	}

	private void putFragment(Fragment fragment) {

	}

	@Override
	public void onBackPressed() {

	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
	}
	
	public void displayMap() {
		// get key from autocomplete view
		// add delay if necessary
		// get Marker object if exists
		// display and zoom to marker if exists
		
	}
	
	public void dismissCard() {
		
	}
	
	public boolean removeMarker() {
		return false; 
	}
	
	public void searchClick(View v) {
		
	}

	public void removeClick(View v) {
		
	}

	public void indexClick(View v) {
		
	}

	public void mapClick(View v) {
		
	}
	
	private void removeEditTextFocus() {
		// set the text as required
	}

}
