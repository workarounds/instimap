package com.mrane.campusmap;

import android.graphics.PointF;

public class Marker {
	public String name;
	public String shortName;
	public PointF point;
	public int groupIndex;
	public Marker(String name, String shortName, float x, float y, int groupIndex) {
		this.point = new PointF(x, y);
		this.groupIndex = groupIndex;
		this.name = name;
		this.shortName = shortName;
	}
}
