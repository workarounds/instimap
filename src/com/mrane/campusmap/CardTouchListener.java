package com.mrane.campusmap;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;

public class CardTouchListener implements OnTouchListener, AnimationListener {

	private MapActivity mainActivity;
	private RelativeLayout bottomLayout;
	private float translation;
	private int curTopMargin;
	private int initTopMargin;
	private float cardHeight;
	
	public CardTouchListener(MapActivity main){
		mainActivity = main;
		bottomLayout = main.bottomLayout;
		translation = 0;
		cardHeight = mainActivity.getResources().getDimension(R.dimen.card_height);
	}
	
	public void initTopMargin(int topMargin) {
		initTopMargin = topMargin;
		curTopMargin = topMargin;
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		int action = me.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			
		}
		else if(action == MotionEvent.ACTION_MOVE){
			
		}
		else if(action == MotionEvent.ACTION_UP){
			mainActivity.expandCard();
		}
		return true;
	}
	

	public Runnable showCardAnimation(){
		final AnimationListener l = this;
		Runnable anim = new Runnable(){
			public void run(){
				float expectedTopMargin = initTopMargin - 0.3f*cardHeight;
				translation = -curTopMargin + expectedTopMargin;
				float yFrom = 0;
				float yTo =  translation;
				TranslateAnimation animation = new TranslateAnimation(0,0,yFrom,yTo);
				animation.setDuration(500);
				animation.setFillEnabled(false);
				animation.setAnimationListener(l);
				bottomLayout.startAnimation(animation);
			}
		};
		return anim;
	}
	public Runnable expandCardAnimation(){
		final AnimationListener l = this;
		Runnable anim = new Runnable(){
			public void run(){
				float expectedTopMargin = initTopMargin - 1.0f*cardHeight;
				translation = - curTopMargin + expectedTopMargin;
				float yFrom = 0;
				float yTo =  translation;
				TranslateAnimation animation = new TranslateAnimation(0,0,yFrom,yTo);
				animation.setDuration(500);
				animation.setFillEnabled(false);
				animation.setAnimationListener(l);
				bottomLayout.startAnimation(animation);
			}
		};
		return anim;
	}
	public Runnable dismissCardAnimation(){
		final AnimationListener l = this;
		Runnable anim = new Runnable(){
			public void run(){
				float expectedTopMargin = initTopMargin;
				translation = - curTopMargin + expectedTopMargin;
				float yFrom = 0;
				float yTo =  translation;
				TranslateAnimation animation = new TranslateAnimation(0,0,yFrom,yTo);
				animation.setDuration(500);
				animation.setFillEnabled(false);
				animation.setAnimationListener(l);
				bottomLayout.startAnimation(animation);
			}
		};
		return anim;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		bottomLayout.clearAnimation();
		FrameLayout.LayoutParams params = (LayoutParams) bottomLayout.getLayoutParams();
		params.topMargin += (int)translation;
		curTopMargin = params.topMargin;
		bottomLayout.setLayoutParams(params);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}
}
