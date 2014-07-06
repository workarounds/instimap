package com.mrane.campusmap;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Color;
import android.graphics.PointF;

public class Marker {
	public String name;
	public String shortName;
	public PointF point;
	public int groupIndex;
	
	private static final int DEPARTMENTS = 1;
	private static final int HOSTELS = 2;
	private static final int RESIDENCES = 3;
	private static final int HALLS_N_AUDITORIUMS = 4;
	private static final int FOOD_STALLS = 5;
	private static final int BANKS_N_ATMS = 6;
	private static final int SCHOOLS = 7;
	private static final int SPORTS = 8;
	private static final int OTHERS = 9;
	private static final int GATES = 10;
	
	private static final String DEPARTMENTS_NAME = "Departments";
	private static final String HOSTELS_NAME = "Hostels";
	private static final String RESIDENCES_NAME = "Residences";
	private static final String HALLS_N_AUDITORIUMS_NAME = "Halls and Auditoriums";
	private static final String FOOD_STALLS_NAME = "Food Stalls";
	private static final String BANKS_N_ATMS_NAME = "Banks and Atms";
	private static final String SCHOOLS_NAME = "Schools";
	private static final String SPORTS_NAME = "Sports";
	private static final String OTHERS_NAME = "Others";
	private static final String GATES_NAME = "Gates";
	
	
	public Marker(String name, String shortName, float x, float y, int groupIndex) {
		this.point = new PointF(x, y);
		this.groupIndex = groupIndex;
		this.name = name;
		this.shortName = shortName;
	}
	
	public int getColor(){
		int group = this.groupIndex;
		Integer[] blueGroup = new Integer[] {HOSTELS};
		Integer[] orangeGroup = new Integer[] {DEPARTMENTS, HALLS_N_AUDITORIUMS};
		Integer[] redGroup = new Integer[] {RESIDENCES};
		Integer[] purpleGroup = new Integer[] {FOOD_STALLS, BANKS_N_ATMS, SCHOOLS, SPORTS, OTHERS, GATES};
		
		ArrayList<Integer> blueList = new ArrayList<Integer>(Arrays.asList(blueGroup));
		ArrayList<Integer> orangeList = new ArrayList<Integer>(Arrays.asList(orangeGroup));
		ArrayList<Integer> redList = new ArrayList<Integer>(Arrays.asList(redGroup));
		ArrayList<Integer> purpleList = new ArrayList<Integer>(Arrays.asList(purpleGroup));
		
		int blue = Color.rgb(68,136,237);
		int orange = Color.rgb(254, 131, 51);
		int red = Color.rgb(254, 51, 51);
		int purple = Color.rgb(216, 125, 232);
		
		if(blueList.contains(group)){
			return blue;
		}
		else if(orangeList.contains(group)){
			return orange;
		}
		else if(redList.contains(group)){
			return red;
		}
		else if(purpleList.contains(group)){
			return purple;
		}
		
		return 0;
	}
	
	public String getGroupName(){
		switch(groupIndex){
		case DEPARTMENTS:
			return DEPARTMENTS_NAME;
		case HOSTELS:
			return HOSTELS_NAME;
		case RESIDENCES:
			return RESIDENCES_NAME;
		case HALLS_N_AUDITORIUMS:
			return HALLS_N_AUDITORIUMS_NAME;
		case FOOD_STALLS:
			return FOOD_STALLS_NAME;
		case BANKS_N_ATMS:
			return BANKS_N_ATMS_NAME;
		case SCHOOLS:
			return SCHOOLS_NAME;
		case SPORTS:
			return SPORTS_NAME;
		case OTHERS:
			return OTHERS_NAME;
		case GATES :
			return GATES_NAME;
		}
		return "";
	}
	
	public static String[] getGroupNames(){
		String[] groupNames = {DEPARTMENTS_NAME,HOSTELS_NAME,RESIDENCES_NAME,HALLS_N_AUDITORIUMS_NAME,FOOD_STALLS_NAME,BANKS_N_ATMS_NAME,SCHOOLS_NAME,SPORTS_NAME,OTHERS_NAME,GATES_NAME};
		return groupNames;
	}
}
