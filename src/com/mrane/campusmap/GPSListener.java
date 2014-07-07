package com.mrane.campusmap;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GPSListener implements LocationListener {

    private Activity activity;
    private LocationManager lm;
    private int numberOfUpdates;

    public static final int MAX_NUMBER_OF_UPDATES = 10;

    public GPSListener(Activity activity, LocationManager lm) {
        this.activity = activity;
        this.lm = lm;
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (numberOfUpdates < MAX_NUMBER_OF_UPDATES) {
            numberOfUpdates++;

            Log.w("LAT", String.valueOf(loc.getLatitude()));
            Log.w("LONG", String.valueOf(loc.getLongitude()));
            Log.w("ACCURACY", String.valueOf(loc.getAccuracy() + " m"));
            Log.w("PROVIDER", String.valueOf(loc.getProvider()));
            Log.w("SPEED", String.valueOf(loc.getSpeed() + " m/s"));
            Log.w("ALTITUDE", String.valueOf(loc.getAltitude()));
            Log.w("BEARING", String.valueOf(loc.getBearing() + " degrees east of true north"));

            String message;

            if (loc != null) {
                message = "Current location is:  Latitude = "
                        + loc.getLatitude() + ", Longitude = "
                        + loc.getLongitude();
                // lm.removeUpdates(this);
            } else
                message = "Location null";

            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(activity, "Gps Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(activity, "Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}