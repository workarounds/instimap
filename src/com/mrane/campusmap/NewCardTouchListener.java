package com.mrane.campusmap;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class NewCardTouchListener implements OnTouchListener {
	private MapActivity mainActivity;
	private RelativeLayout expandContainer;
	private RelativeLayout newSmallCard;
	public final static int STATE_EXPANDED = 1;
	public final static int STATE_HIDDEN = 2;
	public final static int STATE_DISMISSED = 3;
	public final static int STATE_UNKNOWN = 4;

	public NewCardTouchListener(MapActivity main) {
		mainActivity = main;
		expandContainer = main.expandContainer;
		newSmallCard = main.newSmallCard;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		int action = me.getAction();
		if (action == MotionEvent.ACTION_DOWN) {

		} else if (action == MotionEvent.ACTION_MOVE) {

		} else if (action == MotionEvent.ACTION_UP) {
			toggleExpansion();
		}
		return true;
	}

	public void toggleExpansion() {
		if (getCardState() == STATE_HIDDEN) {
			mainActivity.expandCard();
		} else {
			mainActivity.showCard();
		}
	}

	public int getCardState() {
		if (newSmallCard.getVisibility() == View.GONE) {
			return STATE_DISMISSED;
		} else if ((newSmallCard.getVisibility() == View.VISIBLE)
				&& (expandContainer.getVisibility() == View.GONE)) {
			return STATE_HIDDEN;
		} else if ((newSmallCard.getVisibility() == View.VISIBLE)
				&& (expandContainer.getVisibility() == View.VISIBLE)) {
			return STATE_EXPANDED;
		}
		return STATE_UNKNOWN;
	}
	

}
