package com.mrane.data;

public class Room extends Marker {
	public String parentKey;
	public String tag;

	public Room(String name, String shortName, float x, float y, int groupIndex) {
		super(name, shortName, x, y, groupIndex);
	}

	public Room(String name, String shortName, float x, float y,
			int groupIndex, String parentKey) {
		super(name, shortName, x, y, groupIndex);
		this.parentKey = parentKey;
	}

	public Room(String name, String shortName, float x, float y,
			int groupIndex, String parentKey, String tag) {
		super(name, shortName, x, y, groupIndex);
		this.parentKey = parentKey;
		this.tag = tag;
	}

	public Room(String fullName, String shortName, float x, float y,
			int groupId, String parentName, String tag, String desc) {
		super(fullName, shortName, x, y, groupId, desc);
		this.tag = tag;
		this.parentKey = parentName;		
	}

}
