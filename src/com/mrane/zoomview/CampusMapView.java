package com.mrane.zoomview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mrane.campusmap.MainActivity;
import com.mrane.campusmap.Marker;
import com.mrane.campusmap.R.drawable;

public class CampusMapView extends SubsamplingScaleImageView {
	private MainActivity mMainActivity;
	private HashMap<String, Marker> data;
	private Collection<Marker> markerList;
	private ArrayList<Marker> highlightedMarkerList;
	private Bitmap bluePin;
	private Bitmap orangePin;
	private Bitmap redPin;
	private Bitmap purplePin;
	private Bitmap highlightedBluePin;
	private Bitmap highlightedOrangePin;
	private Bitmap highlightedRedPin;
	private Bitmap highlightedPurplePin;
	private float pinWidth = 24;
	private static final float SHOW_PINS_AT_SCALE = 0.8f;
	private static final float SHOW_TEXT_AT_SCALE = 1.4f;
	private Paint paint;
	private Paint textPaint;
	private Rect bounds = new Rect();
	float density;

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
        
        mMainActivity  = MainActivity.getmMainActivity();
        setGestureDetector();
	}
	
	private void initMarkers(){
		float w = 0;
		float h = 0;
		
		bluePin = BitmapFactory.decodeResource(this.getResources(), drawable.blue_pin);
        w = pinWidth*density/2;
        h = bluePin.getHeight() * (w/bluePin.getWidth());
        bluePin = Bitmap.createScaledBitmap(bluePin, (int)w, (int)h, true);
        highlightedBluePin = Bitmap.createScaledBitmap(bluePin, (int)(1.5*w), (int)(1.5*h), true);
        
        orangePin = BitmapFactory.decodeResource(this.getResources(), drawable.orange_pin);
        w = pinWidth*density/2;
        h = orangePin.getHeight() * (w/orangePin.getWidth());
        orangePin = Bitmap.createScaledBitmap(orangePin, (int)w, (int)h, true);
        highlightedOrangePin = Bitmap.createScaledBitmap(orangePin, (int)(1.5*w), (int)(1.5*h), true);
        
        redPin = BitmapFactory.decodeResource(this.getResources(), drawable.red_pin);
        w = pinWidth*density/2;
        h = redPin.getHeight() * (w/redPin.getWidth());
        redPin = Bitmap.createScaledBitmap(redPin, (int)w, (int)h, true);
        highlightedRedPin = Bitmap.createScaledBitmap(redPin, (int)(1.5*w), (int)(1.5*h), true);
        
        purplePin = BitmapFactory.decodeResource(this.getResources(), drawable.purple_pin);
        w = pinWidth*density/2;
        h = purplePin.getHeight() * (w/purplePin.getWidth());
        purplePin = Bitmap.createScaledBitmap(purplePin, (int)w, (int)h, true);
        highlightedPurplePin = Bitmap.createScaledBitmap(purplePin, (int)(1.5*w), (int)(1.5*h), true);
	}
	
	private void initPaints(){
		paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        //textPaint.setColor(Color.rgb(254, 250, 217));
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(8.0f*density, -1*density, 1*density, Color.BLACK);
        textPaint.setTextSize(16*density);
        textPaint.setTypeface(Typeface.SANS_SERIF);
	}

	public void setData(HashMap<String, Marker> markerData){
		data = markerData;
		markerList = data.values();
		highlightedMarkerList = new ArrayList<Marker>();
	}
	
	public void goToMarker(Marker marker){
		highlightedMarkerList.add(marker);
		AnimationBuilder anim = animateScaleAndCenter(getMaxScale(), marker.point);
		anim.withDuration(750).start();
	}
	
	public void removeHighlightedMarkers(){
		highlightedMarkerList.clear();
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
	        	if(highlightedMarkerList.contains(marker)){
	        		PointF vPin = sourceToViewCoord(marker.point);
		            float vX = vPin.x - (highlightedPin.getWidth()/2);
		            float vY = vPin.y - highlightedPin.getHeight();
		            canvas.drawBitmap(highlightedPin, vX, vY, paint);
	        	}
	        	else if(getScale() > SHOW_PINS_AT_SCALE){
		            PointF vPin = sourceToViewCoord(marker.point);
		            float vX = vPin.x - (pin.getWidth()/2);
		            float vY = vPin.y - (pin.getHeight()/2);
		            canvas.drawBitmap(pin, vX, vY, paint);
		            if(getScale() > SHOW_TEXT_AT_SCALE){
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
			
			if(dist < minDist){
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
		final GestureDetector gestureDetector = new GestureDetector(mMainActivity, new GestureDetector.SimpleOnGestureListener(){
			@Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isImageReady()) {
                    PointF sCoord = viewToSourceCoord(e.getX(), e.getY());
                    Marker marker = getNearestMarker(sCoord);
                    if(isMarkerInTouchRegion(marker, sCoord)){
                    	mMainActivity.resultMarker(marker.name);
                    }
                } else {
                    
                }
                return true;
            }
		});
		
		setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
		});
		
	}
	

	private boolean isMarkerInTouchRegion(Marker marker, PointF o) {
		PointF point  = sourceToViewCoord(marker.point);
		PointF origin = sourceToViewCoord(o);
		float dist = (float) calculateDistance(point, origin);
		if(dist < pinWidth*density*2 && isMarkerVisible(marker)) { return true;}
		return false;
	}

	private boolean isMarkerVisible(Marker marker) {
		if(highlightedMarkerList.contains(marker)) return true;
		if(getScale() > SHOW_PINS_AT_SCALE) return true;
		return false;
	}


}
