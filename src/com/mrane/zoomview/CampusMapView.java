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
	private Bitmap highlightedBluePin;
	private Bitmap markerText;
	private float pinWidth = 48;
	private static final float SHOW_PINS_AT_SCALE = 1f;
	private Paint paint;
	private Paint textPaint;
	private Rect bounds = new Rect();

	public CampusMapView(Context context) {
		this(context, null);
	}
	
	public CampusMapView(Context context, AttributeSet attr) {
		super(context, attr);
		initialise();
	}

	private void initialise(){
		float density = getResources().getDisplayMetrics().density;
        bluePin = BitmapFactory.decodeResource(this.getResources(), drawable.markerdot);
        markerText = BitmapFactory.decodeResource(this.getResources(), drawable.text);
        float w = pinWidth*density;
        float h = bluePin.getHeight() * (w/bluePin.getWidth());
        bluePin = Bitmap.createScaledBitmap(bluePin, (int)w, (int)h, true);
        markerText = Bitmap.createScaledBitmap(markerText, (int)(markerText.getWidth()*h/markerText.getHeight()), (int)h, true);
        highlightedBluePin = Bitmap.createScaledBitmap(bluePin, (int)(1.5*w), (int)(1.5*h), true);
        paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        //textPaint.setColor(Color.rgb(254, 250, 217));
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(8.0f*density, -1*density, 1*density, Color.BLACK);
        textPaint.setTextSize(16*density);
        textPaint.setTypeface(Typeface.SANS_SERIF);
        mMainActivity  = MainActivity.getmMainActivity();
        setGestureDetector();
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
		Log.d("testing", "goToMarkerCalled");
	}
	
	public void removeHighlightedMarkers(){
		highlightedMarkerList.clear();
	}
	
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float density = getResources().getDisplayMetrics().density;

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isImageReady()) {
            return;
        }        
        

        for(Marker marker : markerList){
        	if(highlightedMarkerList.contains(marker)){
        		PointF vPin = sourceToViewCoord(marker.point);
	            float vX = vPin.x - (highlightedBluePin.getWidth()/2);
	            float vY = vPin.y - highlightedBluePin.getHeight();
	            canvas.drawBitmap(highlightedBluePin, vX, vY, paint);
        	}
        	else if(super.getScale() > SHOW_PINS_AT_SCALE){
	            PointF vPin = sourceToViewCoord(marker.point);
	            float vX = vPin.x - (bluePin.getWidth()/2);
	            float vY = vPin.y - (bluePin.getHeight()/2);
	            canvas.drawBitmap(bluePin, vX, vY, paint);
//	            canvas.drawBitmap(markerText, vX + bluePin.getWidth(), vY, paint); 
	            String name;
	            if(marker.shortName.isEmpty()) name = marker.name;
	            else name = marker.shortName;
	            textPaint.getTextBounds(name, 0, name.length() - 1, bounds);
	            float tX = vX + 0.75f*bluePin.getWidth();
	            float tY = vY + bluePin.getHeight()/2 + 4*density;
	            canvas.drawText(name, tX, tY, textPaint);
        	}
        }

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
	

	private boolean isMarkerInTouchRegion(Marker marker, PointF origin) {
		PointF point  = marker.point;
		float dist = (float) calculateDistance(point, origin);
		float density = getResources().getDisplayMetrics().density;
		if(dist < pinWidth*density/2 ){ return true;}
		return false;
	}


}
