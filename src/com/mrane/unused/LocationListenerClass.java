package com.mrane.unused;

import com.mrane.campusmap.MapActivity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationListenerClass implements LocationListener {

	@Override
	public void onLocationChanged(Location arg0) {
		Toast.makeText(MapActivity.getMainActivity(), arg0.toString(), Toast.LENGTH_LONG).show();
		Log.d("Testing",arg0.toString());

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
