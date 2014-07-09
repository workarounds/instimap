package com.mrane.data;

public class Building extends Marker {
	public String[] children;
	
	public Building(String name, String shortName, float x, float y,
			int groupIndex) {
		super(name, shortName, x, y, groupIndex);
	}
	
	public Building(String name, String shortName, float x, float y,
			int groupIndex, String[] children) {
		super(name, shortName, x, y, groupIndex);
		this.children = children;
	}

}
