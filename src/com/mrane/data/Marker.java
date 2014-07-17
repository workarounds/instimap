package com.mrane.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Color;
import android.graphics.PointF;

public class Marker {
	public String name;
	public String shortName;
	public PointF point;
	public int groupIndex;
	public boolean showDefault;
	public String description;
	public String tag;
	public String imageUri;

	public static final int COLOR_BLUE = Color.rgb(75, 186, 238);
	public static final int COLOR_YELLOW = Color.rgb(255, 186, 0);
	public static final int COLOR_GREEN = Color.rgb(162, 208, 104);
	public static final int COLOR_GRAY = Color.rgb(156, 156, 156);

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
	private static final int PRINT = 11;
	private static final int LABS = 12;

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
	private static final String PRINT_NAME = "Printer facility";
	private static final String LABS_NAME = "Labs";

	public Marker(String name, String shortName, float x, float y,
			int groupIndex) {
		this.point = new PointF(x, y);
		this.groupIndex = groupIndex;
		this.name = name;
		this.shortName = shortName;
		this.showDefault = false;
		this.description = "";
		this.imageUri = "";
	}

	public Marker(String name, String shortName, float x, float y,
			int groupIndex, String description) {
		this.point = new PointF(x, y);
		this.groupIndex = groupIndex;
		this.name = name;
		this.shortName = shortName;
		this.showDefault = false;
		this.description = description;
		this.imageUri = "";
	}

	public static int getColor(int group) {
		Integer[] yellowGroup = new Integer[] { HOSTELS };
		Integer[] blueGroup = new Integer[] { DEPARTMENTS, LABS,
				HALLS_N_AUDITORIUMS };
		Integer[] greenGroup = new Integer[] { RESIDENCES };
		Integer[] purpleGroup = new Integer[] { FOOD_STALLS, BANKS_N_ATMS,
				SCHOOLS, SPORTS, OTHERS, GATES, PRINT };

		ArrayList<Integer> yellowList = new ArrayList<Integer>(
				Arrays.asList(yellowGroup));
		ArrayList<Integer> blueList = new ArrayList<Integer>(
				Arrays.asList(blueGroup));
		ArrayList<Integer> greenList = new ArrayList<Integer>(
				Arrays.asList(greenGroup));
		ArrayList<Integer> purpleList = new ArrayList<Integer>(
				Arrays.asList(purpleGroup));

		if (yellowList.contains(group)) {
			return COLOR_YELLOW;
		} else if (blueList.contains(group)) {
			return COLOR_BLUE;
		} else if (greenList.contains(group)) {
			return COLOR_GREEN;
		} else if (purpleList.contains(group)) {
			return COLOR_GRAY;
		}

		return 0;
	}

	public int getColor() {
		int group = this.groupIndex;
		return getColor(group);
	}

	public String getGroupName() {
		switch (groupIndex) {
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
		case GATES:
			return GATES_NAME;
		case PRINT:
			return PRINT_NAME;
		case LABS:
			return LABS_NAME;
		}
		return "";
	}

	public static String[] getGroupNames() {
		String[] groupNames = { DEPARTMENTS_NAME, LABS_NAME,
				HALLS_N_AUDITORIUMS_NAME, HOSTELS_NAME, RESIDENCES_NAME,
				FOOD_STALLS_NAME, BANKS_N_ATMS_NAME, SCHOOLS_NAME, SPORTS_NAME,
				PRINT_NAME, GATES_NAME, OTHERS_NAME };
		return groupNames;
	}

	public static int getGroupId(String groupName) {
		int result = 0;
		if (groupName.equals(DEPARTMENTS_NAME))
			result = DEPARTMENTS;
		if (groupName.equals(HOSTELS_NAME))
			result = HOSTELS;
		if (groupName.equals(RESIDENCES_NAME))
			result = RESIDENCES;
		if (groupName.equals(HALLS_N_AUDITORIUMS_NAME))
			result = HALLS_N_AUDITORIUMS;
		if (groupName.equals(FOOD_STALLS_NAME))
			result = FOOD_STALLS;
		if (groupName.equals(BANKS_N_ATMS_NAME))
			result = BANKS_N_ATMS;
		if (groupName.equals(SCHOOLS_NAME))
			result = SCHOOLS;
		if (groupName.equals(SPORTS_NAME))
			result = SPORTS;
		if (groupName.equals(OTHERS_NAME))
			result = OTHERS;
		if (groupName.equals(GATES_NAME))
			result = GATES;
		if (groupName.equals(PRINT_NAME))
			result = PRINT;
		if (groupName.equals(LABS_NAME))
			result = LABS;

		return result;
	}
}
