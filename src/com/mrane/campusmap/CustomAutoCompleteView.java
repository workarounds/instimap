package com.mrane.campusmap;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteView extends AutoCompleteTextView {

	Context mContext;
	MainActivity mMainActivity;
	
	public CustomAutoCompleteView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mMainActivity = MainActivity.getmMainActivity();
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mMainActivity = MainActivity.getmMainActivity();
	}

	public CustomAutoCompleteView(Context context) {
		super(context);
		mContext = context;
		mMainActivity = MainActivity.getmMainActivity();
	}
	
	@Override
	protected void onFocusChanged (boolean focused, int direction, Rect previouslyFocusedRect) {
		mMainActivity.autoCompleteFocusChanged(focused);
		super.onFocusChanged(focused, direction, previouslyFocusedRect);		
	}
	

}
