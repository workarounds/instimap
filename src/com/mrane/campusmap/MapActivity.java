package com.mrane.campusmap;

import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mrane.zoomview.SubsamplingScaleImageView.AnimationBuilder;
import com.mrane.zoomview.CampusMapView;

public class MapActivity extends ActionBarActivity implements TextWatcher,
		OnEditorActionListener, OnItemClickListener, OnFocusChangeListener,
		OnTouchListener {
	private static MapActivity mainActivity;
	boolean isOpened = false;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private IndexFragment indexFragment;
	private Fragment fragment;
	public RelativeLayout placeCard;
	private LinearLayout fragmentContainer;
	public RelativeLayout bottomLayout;
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
	public ImageButton addMarkerIcon;
	public LocationManager locationManager;
	public LocationListener locationListener;
	private boolean noFragments = true;
	private boolean editTextFocused = false;
	private final String firstStackTag = "FIRST_TAG";
	private final int MSG_ANIMATE = 1;
	private final long DELAY_ANIMATE = 75;
	public static final PointF MAP_CENTER = new PointF(3628f, 1640f); 

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ANIMATE:
				showReslutOnMap((String) msg.obj);
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

		bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		placeCard = (RelativeLayout) findViewById(R.id.place_card);
		placeNameTextView = (TextView) findViewById(R.id.place_name);

		Locations mLocations = new Locations();
		data = mLocations.data;
		Set<String> keys = data.keySet();
		String[] KEYS = keys.toArray(new String[keys.size()]);

		fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);

		adapter = new ArrayAdapter<String>(this, R.layout.row_layout,
				R.id.label, KEYS);
		editText = (AutoCompleteTextView) findViewById(R.id.search);
		editText.setAdapter(adapter);
		editText.addTextChangedListener(this);
		editText.setOnEditorActionListener(this);
		editText.setOnFocusChangeListener(this);

		campusMapView = (CampusMapView) findViewById(R.id.campusMapView);
		campusMapView.setImageAsset("map.png");
		campusMapView.setData(data);
		// this.setMapToCenter();

		searchIcon = (ImageButton) findViewById(R.id.search_icon);
		removeIcon = (ImageButton) findViewById(R.id.remove_icon);
		indexIcon = (ImageButton) findViewById(R.id.index_icon);
		mapIcon = (ImageButton) findViewById(R.id.map_icon);
		locateIcon = (ImageButton) findViewById(R.id.locate_icon);
		addMarkerIcon = (ImageButton) findViewById(R.id.add_marker_icon);

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
		this.setCorrectIcons();

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEARCH:
			onItemClick(null, v, 0, 0);
		}
		return false;
	}

	public static MapActivity getMainActivity() {
		return mainActivity;
	}

	public static void setMainActivity(MapActivity mainActivity) {
		MapActivity.mainActivity = mainActivity;
	}

	private void putFragment(Fragment tempFragment) {
		locateIcon.setVisibility(View.GONE);
		this.dismissCard();
		transaction = fragmentManager.beginTransaction();
		fragment = tempFragment;
		if (noFragments) {
			transaction.add(R.id.fragment_container, tempFragment);
			transaction.addToBackStack(firstStackTag);
			transaction.commit();
		} else {
			transaction.replace(R.id.fragment_container, tempFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		noFragments = false;
	}

	private void backToMap() {
		noFragments = true;
		this.hideKeyboard();
		fragmentManager.popBackStack(firstStackTag,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		this.removeEditTextFocus(null);
		this.setCorrectIcons();
		this.displayMap();
	}

	@Override
	public void onBackPressed() {
		if (noFragments) {
			if (!this.removeMarker()) {
				super.onBackPressed();
			} else {
				if (editText.length() > 0) {
					editText.getText().clear();
				}
			}
		} else {
			backToMap();
			this.removeEditTextFocus("");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		String selection = editText.getText().toString();
		if (id < adapter.getCount()) {
			selection = adapter.getItem(id);
		}
		editText.dismissDropDown();
		this.hideKeyboard();
		this.removeEditTextFocus(selection);
		this.backToMap();
		// this.backToMap();

	}

	public void displayMap() {
		// get text from auto complete text box
		String key = editText.getText().toString();

		// get Marker object if exists
		Marker marker = data.get(key);

		// display and zoom to marker if exists
		if (marker != null) {
			Message msg = mHandler.obtainMessage(MSG_ANIMATE, key);
			mHandler.sendMessageDelayed(msg, DELAY_ANIMATE);
		} else {
			removeMarker();
		}
	}

	private void showReslutOnMap(String key) {
		Marker marker = data.get(key);
		showCard(marker);
		campusMapView.setAndShowResultMarker(marker);
	}

	private void showCard(Marker marker) {

	}

	public void dismissCard() {

	}

	public boolean removeMarker() {
		if (campusMapView.getResultMarker() == null) {
			return false;
		} else {
			campusMapView.setResultMarker(null);
			this.dismissCard();
			campusMapView.invalidate();
			return true;
		}
	}

	public void searchClick(View v) {
		Toast toast = Toast.makeText(this,
				"Use the text box and search for a place", Toast.LENGTH_SHORT);
		toast.show();
	}

	public void removeClick(View v) {
		this.editText.setText("");
	}

	public void indexClick(View v) {
		this.putFragment(indexFragment);
		this.removeEditTextFocus(null);
		this.setCorrectIcons();
	}

	public void mapClick(View v) {
		this.backToMap();
		this.removeEditTextFocus("");
	}

	private void removeEditTextFocus(String text) {
		if (this.editTextFocused) {
			this.hideKeyboard();
			editText.clearFocus();
		}

		if (text == null) {

		} else if (text.equals("")) {
			this.setOldText();
		} else {
			editText.setText(text);
		}

	}

	public ArrayAdapter<String> getAdapter() {
		return adapter;
	}

	public void setAdapter(ArrayAdapter<String> adapter) {
		this.adapter = adapter;
	}

	private void setOldText() {
		Marker oldMarker = campusMapView.getResultMarker();
		if (oldMarker == null) {
			if (editText.length() > 0) {
				editText.getText().clear();
			}
		} else {
			editText.setText(oldMarker.name);
		}
	}

	private void setCorrectIcons() {
		if (noFragments) {
			this.setVisibleButton(indexIcon);
		} else {
			if (fragment instanceof ListFragment) {
				this.indexOrRemove();
			} else {
				setVisibleButton(mapIcon);
			}
		}
	}

	private void indexOrRemove() {
		if (editTextFocused) {
			String text = editText.getText().toString();
			if (text.isEmpty() || text.equals(null)) {
				this.setVisibleButton(indexIcon);
			} else {
				this.setVisibleButton(removeIcon);
			}
		} else {
			this.setVisibleButton(indexIcon);
		}
	}

	private void setVisibleButton(ImageButton icon) {
		indexIcon.setVisibility(View.GONE);
		mapIcon.setVisibility(View.GONE);
		removeIcon.setVisibility(View.GONE);

		icon.setVisibility(View.VISIBLE);
	}

	@Override
	public void onFocusChange(View v, boolean focus) {
		this.editTextFocused = focus;
		if (focus) {
			this.putFragment(listFragment);
			fragmentContainer.setOnTouchListener(this);
		} else {
			fragmentContainer.setOnTouchListener(null);
		}
		this.setCorrectIcons();
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			fragmentContainer.setOnTouchListener(null);
		}

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		removeEditTextFocus(null);
		return false;
	}

	public void locateClick(View v) {

	}

	public void addMarkerClick(View v) {

	}

}
