package com.mrane.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomSlidingUpPanelLayout extends SlidingUpPanelLayout {

	public CustomSlidingUpPanelLayout(Context context) {
		super(context);

	}

	public CustomSlidingUpPanelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomSlidingUpPanelLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return super.onInterceptTouchEvent(ev);
	}

}
