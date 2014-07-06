package com.mrane.campusmap;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardTouchListener implements OnTouchListener {

	private RelativeLayout bottomLayout;
	private RelativeLayout placeCard;
	private TextView placeNameTextView;
	private float density;
	private float displayHeight;
	private float displayWidth;
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		Toast.makeText(MapActivity.getMainActivity(), "Card touched",
				Toast.LENGTH_LONG).show();
		return false;
	}

	public class AnimationBuilder{
		public Animation showCardAnimation(){
			int xType = Animation.ABSOLUTE;
			float xValueFrom = bottomLayout.getLeft();
			float xValueTo = bottomLayout.getLeft();
			int yType = Animation.ABSOLUTE;
			float yValueFrom = bottomLayout.getTop();
			float yValueTo = displayHeight - (bottomLayout.getTop() - placeCard.getTop()) -  placeNameTextView.getPaddingTop()*2 - 80*density;
			TranslateAnimation anim = new TranslateAnimation(xType, xValueFrom, xType, xValueTo, yType, yValueFrom, yType, yValueTo);
			return anim;
		}
		public Animation expandCardAnimation(){
			return null;
		}
		public Animation collapseCardAnimation(){
			return null;
		}
		public Animation dismissCardAnimation(){
			return null;
		}
	}
}
