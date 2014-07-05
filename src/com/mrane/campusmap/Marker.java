package com.mrane.campusmap;

import android.graphics.PointF;

public class Marker {
	public String name;
	public PointF point;
	public int groupIndex;
	public Marker(String name, float x, float y, int groupIndex) {
		this.point = new PointF(x, y);
		this.groupIndex = groupIndex;
		this.name = name;
	}
}
