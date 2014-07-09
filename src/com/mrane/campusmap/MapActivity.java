package com.mrane.campusmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mrane.data.Locations;
import com.mrane.data.Marker;
import com.mrane.zoomview.CampusMapView;

public class MapActivity extends ActionBarActivity implements TextWatcher,
		OnEditorActionListener, OnItemClickListener, OnFocusChangeListener,
		OnTouchListener, OnChildClickListener {
	private static MapActivity mainActivity;
	boolean isOpened = false;
	private FuzzySearchAdapter adapter;
	private ExpandableListAdapter expAdapter;
	private FragmentManager fragmentManager;
	private ListFragment listFragment;
	private IndexFragment indexFragment;
	private Fragment fragment;
	public RelativeLayout placeCard;
	private CardTouchListener cardTouchListener;
	private LinearLayout fragmentContainer;
	public RelativeLayout bottomLayout;
	public TextView placeNameTextView;
	public EditText editText;
	public HashMap<String, Marker> data;
	private List<Marker> markerlist;
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
	public int expandedGroup = -1;
	private boolean noFragments = true;
	private boolean editTextFocused = false;
	private final String firstStackTag = "FIRST_TAG";
	private final int MSG_ANIMATE = 1;
	private final int MSG_INIT_LAYOUT = 2;
	private final long DELAY_ANIMATE = 75;
	private final long DELAY_INIT_LAYOUT = 500;
	private Toast toast;
	private String message = "Sorry, no such place in our data.";
	public static final PointF MAP_CENTER = new PointF(3628f, 1640f);
	public static final long DURATION_INIT_MAP_ANIM = 500;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ANIMATE:
				showReslutOnMap((String) msg.obj);
				break;
			case MSG_INIT_LAYOUT:
				initLayout();
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
		markerlist = new ArrayList<Marker>(data.values());

		fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);

		adapter = new FuzzySearchAdapter(this, markerlist);
		editText = (EditText) findViewById(R.id.search);
		editText.addTextChangedListener(this);
		editText.setOnEditorActionListener(this);
		editText.setOnFocusChangeListener(this);

		campusMapView = (CampusMapView) findViewById(R.id.campusMapView);
		campusMapView.setImageAsset("map.png");
		campusMapView.setData(data);

		searchIcon = (ImageButton) findViewById(R.id.search_icon);
		removeIcon = (ImageButton) findViewById(R.id.remove_icon);
		indexIcon = (ImageButton) findViewById(R.id.index_icon);
		mapIcon = (ImageButton) findViewById(R.id.map_icon);
		locateIcon = (ImageButton) findViewById(R.id.locate_icon);
		addMarkerIcon = (ImageButton) findViewById(R.id.add_marker_icon);

		cardTouchListener = new CardTouchListener(this);
		placeCard.setOnTouchListener(cardTouchListener);

		fragmentManager = getSupportFragmentManager();
		listFragment = new ListFragment();
		indexFragment = new IndexFragment();

		Message msg = mHandler.obtainMessage(MSG_INIT_LAYOUT);
		mHandler.sendMessageDelayed(msg, DELAY_INIT_LAYOUT);
		toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
	}

	private void initLayout() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int topMargin = 0;
		// float density = getResources().getDisplayMetrics().density;
		RelativeLayout.LayoutParams p = (LayoutParams) locateIcon
				.getLayoutParams();
		int total = p.height + p.bottomMargin + p.topMargin;
		topMargin = campusMapView.getHeight() - total;
		params.setMargins(0, topMargin, 0, 0);
		bottomLayout.setLayoutParams(params);
		bottomLayout.setVisibility(View.VISIBLE);
		placeCard.setVisibility(View.INVISIBLE);
		locateIcon.setVisibility(View.INVISIBLE);
		cardTouchListener.initTopMargin(topMargin);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		if (editTextFocused) {
			String text = editText.getText().toString()
					.toLowerCase(Locale.getDefault());
			adapter.filter(text);
		}

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
		return true;
	}

	public static MapActivity getMainActivity() {
		return mainActivity;
	}

	public static void setMainActivity(MapActivity mainActivity) {
		MapActivity.mainActivity = mainActivity;
	}

	private void putFragment(Fragment tempFragment) {
		locateIcon.setVisibility(View.INVISIBLE);
		this.dismissCard();
		transaction = fragmentManager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.fragment_slide_in,
		// R.anim.fragment_slide_out);
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
				}
			}
		} else {
			backToMap();
			this.removeEditTextFocus("");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		if (adapter.getResultSize() == 0) {
			toast.setText(message);;
			toast.show();
		} else {
			String selection = editText.getText().toString();
			if (id < adapter.getCount()) {
				selection = adapter.getItem(id).name;
			}
			this.hideKeyboard();
			this.removeEditTextFocus(selection);
			this.backToMap();
		}
	}

	public void displayMap() {
		// locateIcon.setVisibility(View.VISIBLE);
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

	public void showCard() {
		Marker marker = campusMapView.getResultMarker();
		showCard(marker);
	}

	public void showCard(Marker marker) {
		placeNameTextView.setText(marker.name);
		setAddMarkerIcon(marker);
		placeCard.setVisibility(View.VISIBLE);
		Runnable anim = cardTouchListener.showCardAnimation();
		anim.run();
	}

	public void expandCard() {
		Runnable anim = cardTouchListener.expandCardAnimation();
		anim.run();
	}

	public void dismissCard() {
		Runnable anim = cardTouchListener.dismissCardAnimation();
		anim.run();
	}

	public boolean removeMarker() {
		if (campusMapView.getResultMarker() == null) {
			return false;
		} else {
			final int state = cardTouchListener.getCardState();
			switch (state) {
			case CardTouchListener.STATE_DISMISSED:
			case CardTouchListener.STATE_HIDDEN:
				editText.getText().clear();
				campusMapView.setResultMarker(null);
				this.dismissCard();
				campusMapView.invalidate();
				break;
			case CardTouchListener.STATE_EXPANDED:
			case CardTouchListener.STATE_UNKNOWN:
				showCard();
				break;
			default:
				break;
			}
			return true;
		}
	}

	public void searchClick(View v) {
		putFragment(listFragment);
		editText.requestFocus();
		editText.setText("");
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
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

	public FuzzySearchAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(FuzzySearchAdapter adapter) {
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
			String text = editText.getText().toString()
					.toLowerCase(Locale.getDefault());
			adapter.filter(text);
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
		if (adapter.getResultSize() != 0) {
			removeEditTextFocus(null);
		}
		return false;
	}

	public void locateClick(View v) {

	}

	public void addMarkerClick(View v) {
		campusMapView.toggleMarker();
		campusMapView.invalidate();
		setAddMarkerIcon();
	}

	private void setAddMarkerIcon() {
		setAddMarkerIcon(campusMapView.getResultMarker());
	}

	private void setAddMarkerIcon(Marker m) {
		if (campusMapView.isAddedMarker(m)) {
			addMarkerIcon
					.setImageResource(R.drawable.ic_action_location_searching);
		} else {
			addMarkerIcon.setImageResource(R.drawable.ic_action_location_found);
		}
	}

	public void removeCardClick(View v) {
		editText.getText().clear();
		displayMap();
		dismissCard();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		String selection = (String) expAdapter.getChild(groupPosition,
				childPosition);
		this.hideKeyboard();
		this.removeEditTextFocus(selection);
		this.backToMap();
		return true;
	}

	public ExpandableListAdapter getExpAdapter() {
		return expAdapter;
	}

	public void setExpAdapter(ExpandableListAdapter expAdapter) {
		this.expAdapter = expAdapter;
	}

}
