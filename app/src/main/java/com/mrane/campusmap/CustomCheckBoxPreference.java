package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCheckBoxPreference extends RelativeLayout implements OnTouchListener {
	private RelativeLayout container;
	private CheckBox checkBox;
	private TextView nameTextView;
	private TextView summaryTextView;
	private SharedPreferences sharedPrefs;
	private String key;
	private String name;
	private String summary;
	private String summaryOn;
	private String summaryOff;
	private boolean defaultValue = false;

	public CustomCheckBoxPreference(Context context) {
		super(context);
		init(context);
	}

	public CustomCheckBoxPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		applyAttributes(context, attrs);
	}
	
	public CustomCheckBoxPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(context, attrs);
	}

	private void init(Context context){
		View root = View.inflate(context, R.layout.custom_check_box_preference, this);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		container = (RelativeLayout)root.findViewById(R.id.preference_container);
		container.setOnTouchListener(this);
		checkBox = (CheckBox)root.findViewById(R.id.preference_check_box);
		checkBox.setOnTouchListener(this);
		nameTextView = (TextView)root.findViewById(R.id.preference_name);
		summaryTextView = (TextView)root.findViewById(R.id.preference_summary);
	}
	
	private void applyAttributes(Context context, AttributeSet attrs){
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.CustomCheckBoxPreference,
		        0, 0);

	   try {
		   key = a.getString(R.styleable.CustomCheckBoxPreference_key);
	       defaultValue = a.getBoolean(R.styleable.CustomCheckBoxPreference_defaultValue, false);
	       name = a.getString(R.styleable.CustomCheckBoxPreference_name);
	       summary = a.getString(R.styleable.CustomCheckBoxPreference_summary);
	       summaryOn = a.getString(R.styleable.CustomCheckBoxPreference_summaryOn);
	       summaryOff = a.getString(R.styleable.CustomCheckBoxPreference_summaryOff);
	   } finally {
	       a.recycle();
	   }
	   updateViewElements();
	}

	protected void updateViewElements() {
		checkBox.setChecked(sharedPrefs.getBoolean(key, defaultValue));
		sharedPrefs.edit().putBoolean(key, checkBox.isChecked());
		   
		nameTextView.setText(name);
		setSuitableSummary();
	}

	protected void setSuitableSummary() {
		if(checkBox.isChecked()){
			if(summaryOn != null) summaryTextView.setText(summaryOn);
			else if(summary != null) summaryTextView.setText(summary);
			else summaryTextView.setVisibility(View.GONE);
		}
		else{
			if(summaryOff != null) summaryTextView.setText(summaryOff);
			else if(summary != null) summaryTextView.setText(summary);
			else summaryTextView.setVisibility(View.GONE);
		}
	}
	
	protected void togglePreference(){
		boolean currentPref = sharedPrefs.getBoolean(key, defaultValue);
		sharedPrefs.edit().putBoolean(key, !currentPref).commit();
		updateViewElements();
	}
	
	protected void animatePressed(){
		container.setBackgroundColor(Color.rgb(232, 232, 232));
		checkBox.setBackgroundColor(Color.rgb(216, 216, 216));
	}
	
	protected void animateNormal(){
		container.setBackgroundColor(Color.TRANSPARENT);
		checkBox.setBackgroundColor(Color.TRANSPARENT);
	}
	
	public boolean isChecked(){
		return checkBox.isChecked();
	}

	Rect rect;
	boolean ignore = false;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getActionMasked();
		if(ignore && action != MotionEvent.ACTION_UP) return false;
		switch(action){
		case MotionEvent.ACTION_DOWN:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			animatePressed();
			break;
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
			animateNormal();
			break;
		case MotionEvent.ACTION_MOVE:
			if(!rect.contains(rect.left + (int)event.getX(), rect.top + (int)event.getY())){
				animateNormal();
				ignore = true;
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(!ignore){
				v.performClick();
				togglePreference();
				animateNormal();
			}
			else ignore = false;
			break;
		}
		return true;
	}
	
}
