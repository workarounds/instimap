package com.mrane.zoomview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import com.mrane.campusmap.Marker;
import com.mrane.campusmap.R.drawable;

public class CampusMapView extends SubsamplingScaleImageView {
	private HashMap<String, Marker> data;
	private Collection<Marker> markerList;
	private ArrayList<Marker> highlightedMarkerList;
	private Bitmap bluePin;
	private Bitmap highlightedBluePin;
	private float pinWidth = 24;
	private static final float SHOW_PINS_AT_SCALE = 1f;
	private Paint paint = new Paint();

	public CampusMapView(Context context) {
		this(context, null);
	}
	
	public CampusMapView(Context context, AttributeSet attr) {
		super(context, attr);
		initialise();
	}

	private void initialise(){
		float density = getResources().getDisplayMetrics().density;
        bluePin = BitmapFactory.decodeResource(this.getResources(), drawable.test_marker);
        float w = pinWidth*density;
        float h = bluePin.getHeight() * (w/bluePin.getWidth());
        bluePin = Bitmap.createScaledBitmap(bluePin, (int)w, (int)h, true);
        highlightedBluePin = Bitmap.createScaledBitmap(bluePin, (int)(1.5*w), (int)(1.5*h), true);
        paint = new Paint();
        paint.setAntiAlias(true);
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
	            float vY = vPin.y - bluePin.getHeight();
	            canvas.drawBitmap(bluePin, vX, vY, paint);
        	}
        }

    }

}
