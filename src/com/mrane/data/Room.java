package com.mrane.data;

public class Room extends Marker {
	public String parentKey; 
	public Room(String name, String shortName, float x, float y, int groupIndex) {
		super(name, shortName, x, y, groupIndex);
	}
	
	public Room(String name, String shortName, float x, float y, int groupIndex, String parentKey) {
		super(name, shortName, x, y, groupIndex);
		this.parentKey = parentKey;
	}

}
