package com.mrane.campusmap;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CardTouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		Toast.makeText(MainActivity.getmMainActivity(), "Card touched",
				Toast.LENGTH_LONG).show();
		return false;
	}

	public class AnimationBuilder{
		
	}
}
