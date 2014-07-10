package com.mrane.campusmap;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;

public class CardTouchListener implements OnTouchListener, AnimationListener {

	private MapActivity mainActivity;
	private RelativeLayout bottomLayout;
	private RelativeLayout placeCard;
	private float translation;
	private int curTopMargin;
	private int initTopMargin;
	private float cardHeight;
	private float hiddenCardHeight;
	public final static int STATE_EXPANDED = 1;
	public final static int STATE_HIDDEN = 2;
	public final static int STATE_DISMISSED = 3;
	public final static int STATE_UNKNOWN = 4;
	private final static long DURATION_ANIMATION_DEFAULT = 500;
	private final static float INTERPOLATOR_FACTOR = 2.5f;
	
	public CardTouchListener(MapActivity main){
		mainActivity = main;
		bottomLayout = main.bottomLayout;
		placeCard = main.placeCard;
		translation = 0;
		cardHeight = mainActivity.getResources().getDimension(R.dimen.card_height);
		hiddenCardHeight = mainActivity.getResources().getDimension(R.dimen.hidden_card_height);
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
			toggleExpansion();
		}
		return true;
	}
	

	public Runnable showCardAnimation(){
		final AnimationListener l = this;
		Runnable anim = new Runnable(){
			public void run(){
				float expectedTopMargin = initTopMargin - hiddenCardHeight;
				translation = -curTopMargin + expectedTopMargin;
				float yFrom = 0;
				float yTo =  translation;
				TranslateAnimation animation = new TranslateAnimation(0,0,yFrom,yTo);
				animation.setDuration(DURATION_ANIMATION_DEFAULT);
				Interpolator i = new DecelerateInterpolator(INTERPOLATOR_FACTOR);
				animation.setInterpolator(i);
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
				float expectedTopMargin = initTopMargin - cardHeight;
				translation = - curTopMargin + expectedTopMargin;
				float yFrom = 0;
				float yTo =  translation;
				TranslateAnimation animation = new TranslateAnimation(0,0,yFrom,yTo);
				animation.setDuration(DURATION_ANIMATION_DEFAULT);
				animation.setFillEnabled(false);
				Interpolator i = new DecelerateInterpolator(INTERPOLATOR_FACTOR);
				animation.setInterpolator(i);
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
				animation.setDuration(DURATION_ANIMATION_DEFAULT);
				Interpolator i = new DecelerateInterpolator(INTERPOLATOR_FACTOR);
				animation.setInterpolator(i);
				animation.setFillEnabled(false);
				animation.setAnimationListener(l);
				bottomLayout.startAnimation(animation);
			}
		};
		return anim;
	}
	
	public void toggleExpansion(){
		if(getCardState() == STATE_HIDDEN){
			mainActivity.expandCard();
		}
		else{
			mainActivity.showCard();
		}
	}
	
	public int getCardState(){
		if(curTopMargin == initTopMargin){
			return STATE_DISMISSED;
		}
		else if(curTopMargin == initTopMargin - hiddenCardHeight){
			return STATE_HIDDEN;
		}
		else if(curTopMargin == initTopMargin - cardHeight){
			return STATE_EXPANDED;
		}
		return STATE_UNKNOWN;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		bottomLayout.clearAnimation();
		FrameLayout.LayoutParams params = (LayoutParams) bottomLayout.getLayoutParams();
		params.topMargin += (int)translation;
		curTopMargin = params.topMargin;
		bottomLayout.setLayoutParams(params);
		setVisibility();
	}

	private void setVisibility() {
		if(getCardState() == STATE_DISMISSED) placeCard.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}
}
