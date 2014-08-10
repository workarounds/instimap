package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NewCardTouchListener implements OnTouchListener {
	private MapActivity mainActivity;
	private RelativeLayout expandContainer;
	private LinearLayout newSmallCard;
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
			mainActivity.toggleCardIcon.setImageResource(R.drawable.arrow_circle_down);
		} else if(getCardState() == STATE_EXPANDED) {
			mainActivity.showCard();
			mainActivity.toggleCardIcon.setImageResource(R.drawable.arrow_circle_up);
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
