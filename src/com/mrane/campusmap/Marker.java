package com.mrane.campusmap;

import android.graphics.PointF;

public class Marker {
	public PointF point;
	
	public Marker(float x, float y) {
		this.point = new PointF(x, y);
	}
}
