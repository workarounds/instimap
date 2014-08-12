package com.mrane.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class EndDetectScrollView extends ScrollView {
	
	private enum ScrollState{
		TOP,
		BETWEEN,
		BOTTOM
	}
	
	private ScrollState mCurrState = ScrollState.TOP; 
	
	public interface ScrollEndListener {
		public void onScrollHitBottom();

		public void onScrollHitTop();
		
		public void onScrollInBetween();
	}
	
	private ScrollEndListener listener;
	
	public EndDetectScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EndDetectScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EndDetectScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setScrollEndListener(ScrollEndListener l){
		listener = l;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View bottomChild = (View) getChildAt(getChildCount() - 1);
		int bottomDiff = (bottomChild.getBottom() - (getHeight() + getScrollY() + bottomChild
				.getTop()));// Calculate the scrolldiff
		if (getScrollY() == 0) { // if scrollY==0 top has been reached
			if(listener != null) listener.onScrollHitTop();
			mCurrState = ScrollState.TOP;
		}
		else if (bottomDiff == 0) { 
			if(listener != null) listener.onScrollHitBottom();
			mCurrState = ScrollState.BOTTOM;
		}
		else{
			if(listener != null) listener.onScrollInBetween();
			mCurrState = ScrollState.BETWEEN;
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public boolean isAtTop(){
		return mCurrState == ScrollState.TOP;
	}
	
	public boolean isAtBottom(){
		return mCurrState == ScrollState.BOTTOM;
	}
	
	public boolean isInBetween(){
		return mCurrState == ScrollState.BETWEEN;
	}
}
