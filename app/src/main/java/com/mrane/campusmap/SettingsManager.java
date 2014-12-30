package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

public class SettingsManager{
    private static SettingsManager mSettingsManager;
	private SharedPreferences sharedPrefs;
	private String muteKey;
	private String residencesKey;
	private String lastUpdatedKey;
    private String eventsKey;

	private SettingsManager(Context context){
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Resources res = context.getResources();
		muteKey = res.getString(R.string.setting_mute_key);
		residencesKey = res.getString(R.string.setting_residences_key);
		lastUpdatedKey = res.getString(R.string.settings_last_updated_key);
        eventsKey = res.getString(R.string.setting_events_key);
	}

    public static SettingsManager getInstance(Context context){
        if(mSettingsManager == null){
            mSettingsManager = new SettingsManager(context);
        }
        return mSettingsManager;
    }

    public boolean isInEventsMode(){ return sharedPrefs.getBoolean(eventsKey, true); }
	
	public boolean isMuted(){
		return sharedPrefs.getBoolean(muteKey, false);
	}
	
	public boolean showResidences(){
		return sharedPrefs.getBoolean(residencesKey, true);
	}

	public void setMuted(boolean mute){
		sharedPrefs.edit().putBoolean(muteKey, mute).apply();
	}
	
	public void setShowResidences(boolean show){
		sharedPrefs.edit().putBoolean(residencesKey, show).apply();
	}
	
	public SharedPreferences getSharedPrefs(){
		return sharedPrefs;
	}

	public long getLastUpdatedOn() {
		return sharedPrefs.getLong(lastUpdatedKey, 0);
	}

	public void setLastUpdatedOn(long lastUpdatedOn) {
		sharedPrefs.edit().putLong(lastUpdatedKey, lastUpdatedOn).commit();
	}

	
}
