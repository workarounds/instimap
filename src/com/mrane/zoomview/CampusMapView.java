package com.mrane.zoomview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mrane.campusmap.MapActivity;
import com.mrane.campusmap.Marker;
import com.mrane.campusmap.R.drawable;

public class CampusMapView extends SubsamplingScaleImageView {
	private MapActivity mainActivity;
	private HashMap<String, Marker> data;
	private Collection<Marker> markerList;
	private ArrayList<Marker> addedMarkerList;
	private Marker resultMarker;
	private Bitmap bluePin;
	private Bitmap orangePin;
	private Bitmap redPin;
	private Bitmap purplePin;
	private Bitmap highlightedBluePin;
	private Bitmap highlightedOrangePin;
	private Bitmap highlightedRedPin;
	private Bitmap highlightedPurplePin;
	private float pinWidth = 24;
	private Paint paint;
	private Paint textPaint;
	private Rect bounds = new Rect();
	private static int RATIO_SHOW_PIN = 5;
	private static int RATIO_SHOW_PIN_TEXT = 8;
	private float density;

	public CampusMapView(Context context) {
		this(context, null);
	}
	
	public CampusMapView(Context context, AttributeSet attr) {
		super(context, attr);
		initialise();
	}

	private void initialise(){
		density = getResources().getDisplayMetrics().density;
        initMarkers();
        
        initPaints();
        
        mainActivity = MapActivity.getMainActivity();
        
        setGestureDetector();
	}
	
	private void initMarkers(){
		float w = 0;
		float h = 0;
		
		bluePin = BitmapFactory.decodeResource(this.getResources(), drawable.blue_pin);
		highlightedBluePin = BitmapFactory.decodeResource(this.getResources(), drawable.highlighted_blue_pin);
        w = pinWidth*density/2;
        h = bluePin.getHeight() * (w/bluePin.getWidth());
        bluePin = Bitmap.createScaledBitmap(bluePin, (int)w, (int)h, true);
        w = 2.5f*w;
        h = highlightedBluePin.getHeight() * (w/highlightedBluePin.getWidth());
        highlightedBluePin = Bitmap.createScaledBitmap(highlightedBluePin, (int)(w), (int)(h), true);
        
        orangePin = BitmapFactory.decodeResource(this.getResources(), drawable.orange_pin);
        highlightedOrangePin = BitmapFactory.decodeResource(this.getResources(), drawable.highlighted_orange_pin);
        w = pinWidth*density/2;
        h = orangePin.getHeight() * (w/orangePin.getWidth());
        orangePin = Bitmap.createScaledBitmap(orangePin, (int)w, (int)h, true);
        w = 2.5f*w;
        h = highlightedOrangePin.getHeight() * (w/highlightedOrangePin.getWidth());
        highlightedOrangePin = Bitmap.createScaledBitmap(highlightedOrangePin, (int)(w), (int)(h), true);
        
        redPin = BitmapFactory.decodeResource(this.getResources(), drawable.red_pin);
        highlightedRedPin = BitmapFactory.decodeResource(this.getResources(), drawable.highlighted_red_pin);
        w = pinWidth*density/2;
        h = redPin.getHeight() * (w/redPin.getWidth());
        redPin = Bitmap.createScaledBitmap(redPin, (int)w, (int)h, true);
        w = 2.5f*w;
        h = highlightedRedPin.getHeight() * (w/highlightedRedPin.getWidth());
        highlightedRedPin = Bitmap.createScaledBitmap(highlightedRedPin, (int)(w), (int)(h), true);
        
        purplePin = BitmapFactory.decodeResource(this.getResources(), drawable.purple_pin);
        highlightedPurplePin = BitmapFactory.decodeResource(this.getResources(), drawable.highlighted_purple_pin);
        w = pinWidth*density/2;
        h = purplePin.getHeight() * (w/purplePin.getWidth());
        purplePin = Bitmap.createScaledBitmap(purplePin, (int)w, (int)h, true);
        w = 2.5f*w;
        h = highlightedPurplePin.getHeight() * (w/highlightedPurplePin.getWidth());
        highlightedPurplePin = Bitmap.createScaledBitmap(highlightedPurplePin, (int)(w), (int)(h), true);
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
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	private float getTargetMinScale() {
    	return Math.max(getWidth() / (float) getSWidth(), (getHeight())/ (float) getSHeight());
    }

	public void setData(HashMap<String, Marker> markerData){
		data = markerData;
		markerList = data.values();
		addedMarkerList = new ArrayList<Marker>();
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
			AnimationBuilder anim = animateScaleAndCenter(getMaxScale(), resultMarker.point);
			anim.withDuration(750).start();
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
	
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isImageReady()) {
            return;
        }        
        

        for(Marker marker : markerList){
        	if(isInView(marker.point)){
        		Bitmap pin = getPin(marker);
            	Bitmap highlightedPin = getHighlightedPin(marker);
            	if(isResultMarker(marker)){
            		PointF vPin = sourceToViewCoord(marker.point);
		            float vX = vPin.x - (highlightedPin.getWidth()/2);
		            float vY = vPin.y - highlightedPin.getHeight();
		            canvas.drawBitmap(highlightedPin, vX, vY, paint);
		            String name;
		            if(marker.shortName.isEmpty()) name = marker.name;
		            else name = marker.shortName;
		            textPaint.getTextBounds(name, 0, name.length() - 1, bounds);
		            float tX = vPin.x - bounds.width()/2;
		            float tY = vPin.y + bounds.height();
		            canvas.drawText(name, tX, tY, textPaint);
            	}
            	else if(addedMarkerList.contains(marker)){
	        		
	        	}
	        	else if(isShowPinScale()){
		            PointF vPin = sourceToViewCoord(marker.point);
		            float vX = vPin.x - (pin.getWidth()/2);
		            float vY = vPin.y - (pin.getHeight()/2);
		            canvas.drawBitmap(pin, vX, vY, paint);
		            if(isShowPinTextScale()){
			            String name;
			            if(marker.shortName.isEmpty()) name = marker.name;
			            else name = marker.shortName;
			            textPaint.getTextBounds(name, 0, name.length() - 1, bounds);
			            float tX = vX + pin.getWidth();
			            float tY = vY + pin.getHeight()/2 + 4*density;
			            canvas.drawText(name, tX, tY, textPaint);
		            }
	        	}
        	}
        }

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
		
		int blue = Color.rgb(68,136,237);
		int orange = Color.rgb(254, 131, 51);
		int red = Color.rgb(254, 51, 51);
		int purple = Color.rgb(216, 125, 232);
		
		if(color == blue){
			return bluePin;
		}
		else if(color == orange){
			return orangePin;
		}
		else if(color == red){
			return redPin;
		}
		else if(color == purple){
			return purplePin;
		}
		
		return null;
	}
	
	private Bitmap getHighlightedPin(Marker marker){
		int color = marker.getColor();
		
		int blue = Color.rgb(68,136,237);
		int orange = Color.rgb(254, 131, 51);
		int red = Color.rgb(254, 51, 51);
		int purple = Color.rgb(216, 125, 232);
		
		if(color == blue){
			return highlightedBluePin;
		}
		else if(color == orange){
			return highlightedOrangePin;
		}
		else if(color == red){
			return highlightedRedPin;
		}
		else if(color == purple){
			return highlightedPurplePin;
		}
		
		return null;
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
		if(isShowPinScale()) return true;
		return false;
	}
	
	private boolean isShowPinScale(){
		PointF left = viewToSourceCoord(0, 0);
		PointF right = viewToSourceCoord(getWidth(), 0);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float xDpi = metrics.xdpi;
		if((right.x-left.x)*2*xDpi/(320*density) < getSWidth()/RATIO_SHOW_PIN) return true;
		return false;
	}
	
	private boolean isShowPinTextScale(){
		PointF left = viewToSourceCoord(0, 0);
		PointF right = viewToSourceCoord(getWidth(), 0);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float xDpi = metrics.xdpi;
		if((right.x-left.x)*2*xDpi/(320*density) < getSWidth()/RATIO_SHOW_PIN_TEXT) return true;
		return false;
	}

}
