package com.mrane.zoomview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.mrane.campusmap.MapActivity;
import com.mrane.campusmap.R.drawable;
import com.mrane.data.Marker;

public class CampusMapView extends SubsamplingScaleImageView {
	private MapActivity mainActivity;
	private HashMap<String, Marker> data;
	private Collection<Marker> markerList;
	private ArrayList<Marker> addedMarkerList;
	private ArrayList<Marker> specialMarkerList;
	private Marker resultMarker;
	private Bitmap lightGreenPin;
	private Bitmap yellowPin;
	private Bitmap redPin;
	private Bitmap greenPin;
	private Bitmap highlightedLightGreenPin;
	private Bitmap highlightedYellowPin;
	private Bitmap highlightedRedPin;
	private Bitmap highlightedGreenPin;
	private Bitmap currentCenter;
	private Bitmap currentPlusCenter;
	private Bitmap plusCenter;
	private float pinWidth = 24;
	private float highlightedPinScale;
	private Paint paint;
	private Paint textPaint;
	private Paint strokePaint;
	private Rect bounds = new Rect();
	private static int RATIO_SHOW_PIN = 10;
	private static int RATIO_SHOW_PIN_TEXT = 16;
	private static long DURATION_MARKER_ANIMATION = 500;
	private static long DELAY_MARKER_ANIMATION = 675;
	private static float MAX_SCALE = 1F;
	private float density;
	private boolean isFirstLoad = true;

	public CampusMapView(Context context) {
		this(context, null);
	}
	
	public CampusMapView(Context context, AttributeSet attr) {
		super(context, attr);
		initialise();
	}

	private void initialise(){
		density = getResources().getDisplayMetrics().density;
		highlightedPinScale = 1.0f;
        initMarkers();
        
        initPaints();
        
        mainActivity = MapActivity.getMainActivity();
        
        setGestureDetector();
        super.setMaxScale(density*MAX_SCALE);
	}
	
	@Override
	protected void onImageReady(){
		if(isFirstLoad){
			Runnable runnable = new Runnable(){
				public void run(){
					AnimationBuilder anim = animateScaleAndCenter(getTargetMinScale(), MapActivity.MAP_CENTER);
					anim.withDuration(MapActivity.DURATION_INIT_MAP_ANIM).start();
					isFirstLoad = false;
				}
			};
			mainActivity.runOnUiThread(runnable);
		}
	}
	
	public void setFirstLoad(boolean b){
		isFirstLoad = b;
	}
	
	private void initMarkers(){
		float w = 0;
		float h = 0;
		Options options = new BitmapFactory.Options();
	    options.inScaled = false;
		
		lightGreenPin = BitmapFactory.decodeResource(this.getResources(), drawable.blue_pin, options);
		highlightedLightGreenPin = BitmapFactory.decodeResource(this.getResources(), drawable.marker_blue_h, options);
        w = pinWidth*density/2;
        h = lightGreenPin.getHeight() * (w/lightGreenPin.getWidth());
        lightGreenPin = Bitmap.createScaledBitmap(lightGreenPin, (int)w, (int)h, true);
        w = 4f*w;
        h = highlightedLightGreenPin.getHeight() * (w/highlightedLightGreenPin.getWidth());
        highlightedLightGreenPin = Bitmap.createScaledBitmap(highlightedLightGreenPin, (int)(w), (int)(h), true);
        
        yellowPin = BitmapFactory.decodeResource(this.getResources(), drawable.orange_pin, options);
        highlightedYellowPin = BitmapFactory.decodeResource(this.getResources(), drawable.marker_blue_s, options);
        w = pinWidth*density/2;
        h = yellowPin.getHeight() * (w/yellowPin.getWidth());
        yellowPin = Bitmap.createScaledBitmap(yellowPin, (int)w, (int)h, true);
        w = 4f*w;
        h = highlightedYellowPin.getHeight() * (w/highlightedYellowPin.getWidth());
        highlightedYellowPin = Bitmap.createScaledBitmap(highlightedYellowPin, (int)(w), (int)(h), true);
        
        redPin = BitmapFactory.decodeResource(this.getResources(), drawable.red_pin, options);
        highlightedRedPin = BitmapFactory.decodeResource(this.getResources(), drawable.marker_green_h, options);
        w = pinWidth*density/2;
        h = redPin.getHeight() * (w/redPin.getWidth());
        redPin = Bitmap.createScaledBitmap(redPin, (int)w, (int)h, true);
        w = 4f*w;
        h = highlightedRedPin.getHeight() * (w/highlightedRedPin.getWidth());
        highlightedRedPin = Bitmap.createScaledBitmap(highlightedRedPin, (int)(w), (int)(h), true);
        
        greenPin = BitmapFactory.decodeResource(this.getResources(), drawable.purple_pin, options);
        highlightedGreenPin = BitmapFactory.decodeResource(this.getResources(), drawable.marker_yellow_h, options);
        w = pinWidth*density/2;
        h = greenPin.getHeight() * (w/greenPin.getWidth());
        greenPin = Bitmap.createScaledBitmap(greenPin, (int)w, (int)h, true);
        w = 4f*w;
        float scaling = w/highlightedGreenPin.getWidth();
        h = highlightedGreenPin.getHeight()*scaling;
        highlightedGreenPin = Bitmap.createScaledBitmap(highlightedGreenPin, (int)(w), (int)(h), true);
        
        currentCenter = BitmapFactory.decodeResource(getResources(), drawable.current_center, options);
        w = currentCenter.getWidth()*scaling*0.5f;
        h = currentCenter.getHeight()*w/currentCenter.getWidth();
        currentCenter = Bitmap.createScaledBitmap(currentCenter, (int)w, (int)h, true);
        currentPlusCenter = BitmapFactory.decodeResource(getResources(), drawable.current_plus_center, options);
        currentPlusCenter = Bitmap.createScaledBitmap(currentPlusCenter, (int)w, (int)h, true);
        plusCenter = BitmapFactory.decodeResource(getResources(), drawable.plus_center, options);
        plusCenter = Bitmap.createScaledBitmap(plusCenter, (int)w, (int)h, true);
	}
	
	private void initPaints(){
		paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        //textPaint.setColor(Color.rgb(254, 250, 217));
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(8.0f*density, -1*density, 1*density, Color.BLACK);
        textPaint.setTextSize(14*density);
        Typeface boldCn = Typeface.createFromAsset(getContext().getAssets(), MapActivity.FONT_BOLD);
        textPaint.setTypeface(boldCn);
        
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
        strokePaint.setTextSize(14*density);
        strokePaint.setStyle(Style.STROKE);
        strokePaint.setStrokeJoin(Join.ROUND);
        strokePaint.setStrokeWidth(0.2f*density);
        
	}
	
	public float getTargetMinScale() {
    	return Math.max(getWidth() / (float) getSWidth(), (getHeight())/ (float) getSHeight());
    }

	public void setData(HashMap<String, Marker> markerData){
		data = markerData;
		markerList = data.values();
		addedMarkerList = new ArrayList<Marker>();
		specialMarkerList = new ArrayList<Marker>();
		setSpecialMarkers();
	}
	
	private void setSpecialMarkers() {
		for(Marker m : markerList){
			if(m.showDefault){
				specialMarkerList.add(m);
			}
		}
	}

	public static int getShowPinRatio(){
		return RATIO_SHOW_PIN;
	}
	
	public static void setShowPinRatio(int ratio){
		RATIO_SHOW_PIN = ratio;
	}
	
	public static int getShowPinTextRatio(){
		return RATIO_SHOW_PIN_TEXT;
	}
	
	public static void setShowPinTextRatio(int ratio){
		RATIO_SHOW_PIN_TEXT = ratio;
	}
	
	public Marker getResultMarker(){
		return resultMarker;
	}
	
	@Deprecated
	public Marker getHighlightedMarker(){
		return getResultMarker();
	}
	
	public void setResultMarker(Marker marker){
		resultMarker = marker;
	}
	
	public boolean isResultMarker(Marker marker){
		return resultMarker == marker;
	}
	
	public void showResultMarker(){
		if(resultMarker != null){
			boolean noDelay = false;
			if(isInView(getResultMarker().point)) noDelay = true;
			AnimationBuilder anim = animateScaleAndCenter(getShowTextScale(), resultMarker.point);
			anim.withDuration(750).start();
			setMarkerAnimation(noDelay, MapActivity.SOUND_ID_RESULT);
		}
	}
	
	public void setAndShowResultMarker(Marker marker){
		setResultMarker(marker);
		showResultMarker();
	}
	
	public void addMarker(Marker m){
		if(!addedMarkerList.contains(m)){
			addedMarkerList.add(m);
			
		}
	}
	
	public void addMarker(){
		Marker m = getResultMarker();
		addMarker(m);
	}
	
	public void addMarkers(Collection<? extends Marker> markers){
		for(Marker m : markers){
			addMarker(m);
		}
	}
	
	public void addMarkers(Marker[] markerArray){
		List<Marker> markerList = Arrays.asList(markerArray);
		addMarkers(markerList);
	}
	
	public void removeAddedMarker(Marker m){
		if(addedMarkerList.contains(m)){
			addedMarkerList.remove(m);
		}
	}
	
	public void removeAddedMarkers(Collection<? extends Marker> markers){
		for(Marker m : markers){
			removeAddedMarker(m);
		}
	}
	
	public void removeAddedMarkers(Marker[] markerArray){
		List<Marker> markerList = Arrays.asList(markerArray);
		removeAddedMarkers(markerList);
	}
	
	public void removeAddedMarkers(){
		addedMarkerList.clear();
	}
	
	public boolean isAddedMarker(Marker m){
		return addedMarkerList.contains(m);
	}
	
	public boolean isAddedMarker(){
		return isAddedMarker(getResultMarker());
	}
	
	public void toggleMarker(Marker m){
		if(isAddedMarker(m)){
			removeAddedMarker(m);
			mainActivity.playAnimSound(MapActivity.SOUND_ID_REMOVE);
		}
		else{
			addMarker(m);
			if(!isInView(m.point)){
				AnimationBuilder anim = animateScaleAndCenter(getShowTextScale(), m.point);
				anim.withDuration(750).start();
				setMarkerAnimation(false, MapActivity.SOUND_ID_ADD);
			}
			else{
				setMarkerAnimation(true, MapActivity.SOUND_ID_ADD);
			}
		}
		invalidate();
	}
	
	public void toggleMarker(){
		toggleMarker(getResultMarker());
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isImageReady()) {
            return;
        }        
        

        for(Marker marker : markerList){
        	if(isInView(marker.point)){
            	if(isShowPinScale(marker) && !(isResultMarker(marker) || addedMarkerList.contains(marker))){
            		drawPinAndText(canvas, marker);
            	}
	        	else if((isResultMarker(marker) || addedMarkerList.contains(marker))){
		            drawHighlightedPin(canvas, marker);
		            drawHighlightedMarkerText(canvas, marker);
		            drawPinCenter(canvas, marker);
	        	}
        	}
        }

    }
	
	private void drawHighlightedPin(Canvas canvas, Marker marker){
		Bitmap highlightedPin = getHighlightedPin(marker);
		PointF vPin = sourceToViewCoord(marker.point);
        float vX = vPin.x - (highlightedPin.getWidth()/2);
        float vY = vPin.y - highlightedPin.getHeight();
        canvas.drawBitmap(highlightedPin, vX, vY, paint);
	}
	
	private void drawHighlightedMarkerText(Canvas canvas, Marker marker){
		String name;
		PointF vPin = sourceToViewCoord(marker.point);
        if(marker.shortName.isEmpty()) name = marker.name;
        else name = marker.shortName;
        String[] names = name.split(" ");
        textPaint.getTextBounds(names[0], 0, names[0].length() - 1, bounds);
        float tX = vPin.x - bounds.width()/2;
        float tY = vPin.y + bounds.height();
        canvas.drawText(names[0], tX, tY, textPaint);
        //canvas.drawText(names[0], tX, tY, strokePaint);
        
        int size = names.length;
        if(size >1){
	        for(int i = 1; i<size; i++){
	        	float displacement = textPaint.getFontSpacing();
	        	textPaint.getTextBounds(names[i], 0, names[i].length() - 1, bounds);
	        	tX = vPin.x - bounds.width()/2;
	        	tY += displacement;
	        	canvas.drawText(names[i], tX, tY, textPaint);
	            //canvas.drawText(names[i], tX, tY, strokePaint);
	        }
        }
	}
	
	private void drawPinCenter(Canvas canvas, Marker marker){
		Bitmap pinCenter = getPinCenter(marker);
		Bitmap highlightedPin = getHighlightedPin(marker);
		PointF vPin = sourceToViewCoord(marker.point);
		float vX = vPin.x - pinCenter.getWidth()/2;
		float vY = vPin.y - 0.65f*highlightedPin.getHeight() - pinCenter.getHeight()/2;
		//canvas.drawBitmap(pinCenter, vX, vY, paint);
	}
	
	private void drawPinAndText(Canvas canvas, Marker marker){
		Bitmap pin = getPin(marker);
		PointF vPin = sourceToViewCoord(marker.point);
        float vX = vPin.x - (pin.getWidth()/2);
        float vY = vPin.y - (pin.getHeight()/2);
        canvas.drawBitmap(pin, vX, vY, paint);
        if(isShowPinTextScale(marker)){
            String name;
            if(marker.shortName.isEmpty()) name = marker.name;
            else name = marker.shortName;
            String[] names = name.split(" ");
            textPaint.getTextBounds(name, 0, name.length() - 1, bounds);
            float tX = vX + pin.getWidth() + 2*density;
            float tY = vY + pin.getHeight()/2 + 4*density;
            
            int size = names.length;
            for(int i = 0; i<size; i++){
            	canvas.drawText(names[i], tX, tY, textPaint);
                //canvas.drawText(names[i], tX, tY, strokePaint);
            	float displacement = textPaint.getFontSpacing();
            	tY += displacement;
            }
        }
	}
	
	private Bitmap getPinCenter(Marker marker){
		Bitmap pinCenter = null;
		if(addedMarkerList.contains(marker)){
			if(marker == getResultMarker()){
				pinCenter = currentPlusCenter;
			}
			else pinCenter = plusCenter;
		}
		else	pinCenter = currentCenter;
		
		if(highlightedPinScale != 1.0f && isResultMarker(marker)){
			float w = pinCenter.getWidth()*highlightedPinScale;
			float h = pinCenter.getHeight()*highlightedPinScale;
			pinCenter = Bitmap.createScaledBitmap(pinCenter, (int)w, (int)h, true);
		}
		return pinCenter;
	}
	
	private boolean isInView(PointF point){
		int displayWidth = getResources().getDisplayMetrics().widthPixels;
		int displayHeight = getResources().getDisplayMetrics().heightPixels;
		
		int viewX = (int)sourceToViewCoord(point).x;
		int viewY = (int)sourceToViewCoord(point).y;
		
		if(viewX > -displayWidth/3 && viewX < displayWidth && viewY > 0 && viewY < displayHeight) return true;
		
		return false;
	}
	
	private Marker getNearestMarker(PointF touchPoint) {
		Marker resultMarker = null;
		float minDist = 100000000f;
		for(Marker marker: markerList){
			PointF point = marker.point;
			float dist  = (float) calculateDistance(point, touchPoint);
			
			if(dist < minDist && isMarkerVisible(marker)){
				minDist = dist;
				resultMarker = marker;
			}
		}
		return resultMarker;
	}
	
	private double calculateDistance(PointF point1, PointF point2){
		float xDiff = point1.x - point2.x;
		float yDiff = point1.y - point2.y;
		
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	private Bitmap getPin(Marker marker){
		int color = marker.getColor();
		
		if(color == Marker.COLOR_BLUE){
			return lightGreenPin;
		}
		else if(color == Marker.COLOR_YELLOW){
			return yellowPin;
		}
		else if(color == Marker.COLOR_GREEN){
			return redPin;
		}
		else if(color == Marker.COLOR_PURPLE){
			return greenPin;
		}
		
		return lightGreenPin;
	}
	
	private Bitmap getHighlightedPin(Marker marker){
		int color = marker.getColor();
		
		Bitmap highlightedPin = null;
		
		if(color == Marker.COLOR_BLUE){
			highlightedPin = highlightedLightGreenPin;
		}
		else if(color == Marker.COLOR_YELLOW){
			highlightedPin = highlightedYellowPin;
		}
		else if(color == Marker.COLOR_GREEN){
			highlightedPin = highlightedRedPin;
		}
		else if(color == Marker.COLOR_PURPLE){
			highlightedPin = highlightedGreenPin;
		}
		
		if(highlightedPinScale != 1.0f && isResultMarker(marker)){
			float w = highlightedPin.getWidth()*highlightedPinScale;
			float h = highlightedPin.getHeight()*highlightedPinScale;
			highlightedPin = Bitmap.createScaledBitmap(highlightedPin, (int)w, (int)h, true);
		}
		
		if(isResultMarker(marker)) {
			float w = highlightedPin.getWidth()*1.2f;
			float h = highlightedPin.getHeight()*1.2f;
			highlightedPin = Bitmap.createScaledBitmap(highlightedPin, (int)w, (int)h, true);
		}
		
		return highlightedPin;
	}
	
	private void setMarkerAnimation(boolean noDelay, int _sound_index){
		final int sound_index = _sound_index;
		long delay = 0;
		if(!noDelay){
			delay = DELAY_MARKER_ANIMATION;
		}
		
		if(android.os.Build.VERSION.SDK_INT>=11){
			playAnim(delay);
		}
		mainActivity.playAnimSoundDelayed(sound_index, delay);
	}
	
	@SuppressLint("NewApi")
	private void playAnim(long delay){
		highlightedPinScale = 0.1f;
		ValueAnimator valAnim = new ValueAnimator();
		valAnim.setFloatValues(0.1f,1.0f);
		valAnim.setDuration(DURATION_MARKER_ANIMATION);
		valAnim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				highlightedPinScale = (Float) animation.getAnimatedValue();
				if(isImageReady()) invalidate();
			}
		});
		TimeInterpolator i = new BounceInterpolator();
		valAnim.setInterpolator(i);
		valAnim.setStartDelay(delay);
		valAnim.start();
	}
	
	private void setGestureDetector() {
		final GestureDetector gestureDetector = new GestureDetector(mainActivity, new GestureDetector.SimpleOnGestureListener(){
			@Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isImageReady()) {
                    PointF sCoord = viewToSourceCoord(e.getX(), e.getY());
                    Marker marker = getNearestMarker(sCoord);
                    if(isMarkerInTouchRegion(marker, sCoord)){
                    	//mMainActivity.resultMarker(marker.name);
                    	mainActivity.editText.setText(marker.name);
                    	mainActivity.displayMap();
                    }
                } else {
                    
                }
                return true;
            }
		});
		
		setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
            	final float targetMinScale = getTargetMinScale();
            	int action = motionEvent.getAction();
            	if(targetMinScale > getScale()){
            		callSuperOnTouch(motionEvent);
	            	if(action == MotionEvent.ACTION_UP){
	            		Runnable anim = new Runnable(){
	            			public void run(){
	            				AnimationBuilder animation = animateScale(targetMinScale);
	            				animation.withDuration(200).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).start();
	            			}
	            		};
	            		anim.run();
	            	}
	            	return true;
            	}
                return gestureDetector.onTouchEvent(motionEvent);
            }
		});
		
	}
	
	private void callSuperOnTouch(MotionEvent me){
		super.onTouchEvent(me);
	}

	private boolean isMarkerInTouchRegion(Marker marker, PointF o) {
		if(marker != null){
			PointF point  = sourceToViewCoord(marker.point);
			PointF origin = sourceToViewCoord(o);
			float dist = (float) calculateDistance(point, origin);
			if(dist < pinWidth*density*2 && isMarkerVisible(marker)) { return true;}
		}
		return false;
	}

	private boolean isMarkerVisible(Marker marker) {
		if(marker == resultMarker) return true;
		if(addedMarkerList.contains(marker)) return true;
		if(isShowPinScale(marker)) return true;
		return false;
	}
	
	private boolean isShowPinScale(Marker m){
		if(specialMarkerList.contains(m)) return true;
		PointF left = viewToSourceCoord(0, 0);
		PointF right = viewToSourceCoord(getWidth(), 0);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float xDpi = metrics.xdpi;
		if((right.x-left.x)*xDpi/getWidth() < getSWidth()/RATIO_SHOW_PIN) return true;
		return false;
	}
	
	private boolean isShowPinTextScale(Marker m){
		if(specialMarkerList.contains(m)) return true;
		PointF left = viewToSourceCoord(0, 0);
		PointF right = viewToSourceCoord(getWidth(), 0);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float xDpi = metrics.xdpi;
		if((right.x-left.x)*xDpi/getWidth() < getSWidth()*density/(RATIO_SHOW_PIN_TEXT*2)) return true;
		return false;
	}
	
	private float getShowTextScale(){
		float xDpi = getResources().getDisplayMetrics().xdpi;
		float scale = (RATIO_SHOW_PIN_TEXT*xDpi*2/density + 20)/getSWidth();
		return scale;
	}

}
