package com.mrane.campusmap;

import java.util.HashMap;

public class Locations {
	public HashMap<String, Marker> data = new HashMap<String, Marker>();

	public Locations() {
		String s1 = "Co Main building";
		Marker m1 = new Marker(3355f, 1575f);
		data.put(s1, m1);
		
		String s2 = "Co Industrial Design Centre";
		Marker m2 = new Marker(3884f, 1670f);
		data.put(s2, m2);
		
		String s3 = "Convocation Hall";
		Marker m3 = new Marker(3064f, 1636f);
		data.put(s3, m3);
		
		String s4 = "Co Kresit";
		Marker m4 = new Marker(3038f, 1990f);
		data.put(s4, m4);
		
		String s5 = "Co Lecture Hall Complex";
		Marker m5 = new Marker(3236f, 2010f);
		data.put(s5, m5);
		
		String s6 = "Co Lectures Hall Complex";
		Marker m6 = new Marker(2236f, 2010f);
		data.put(s6, m6);
		
	}

}
