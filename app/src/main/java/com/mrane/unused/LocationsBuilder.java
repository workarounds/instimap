package com.mrane.unused;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mrane.data.Building;
import com.mrane.data.Marker;
import com.mrane.data.Room;

public class LocationsBuilder {
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
	public LocationsBuilder(Context context) {
		dbHelper = new DataBaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public HashMap<String, Marker> getAllRows() {
		HashMap<String, Marker> markers = new HashMap<String, Marker>();
		Cursor cursor = database.query("locations", null, null, null, null, null, null);
		Marker m = cursorToMarker(cursor);
		markers.put(m.getName(), m);
		return markers;
	}
	
	private Marker cursorToMarker(Cursor cur) {
		String fullName = cur.getString(1);
		String shortName = cur.getString(2);
		int groupId = cur.getInt(3);
		float X = cur.getFloat(4);
		float Y = cur.getFloat(5);
		String parentName = cur.getString(9);
		String tag = cur.getString(7);
		String desc = cur.getString(8);
		String children = cur.getString(10);
		Marker m = null;
		
		if(parentName != "") {
			m = new Room(fullName, shortName, X, Y, groupId, parentName, tag, desc);
		} else if (children != "") {
			String[] s = children.split("**");
			m = new Building(fullName, shortName, X, Y, groupId, s, desc);
		} else {
			m = new Marker(fullName, shortName, X, Y, groupId, desc);
		}
		return m;
	}
}
