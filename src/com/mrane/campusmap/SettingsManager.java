package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class SettingsManager implements OnSharedPreferenceChangeListener {
	private SharedPreferences sharedPrefs;
	private String muteKey;
	private String residencesKey;
	private String lastUpdatedKey;
	private boolean mutePref;
	private boolean residencesPref;
	private long lastUpdatedOn;
	
	public SettingsManager(Context context){
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Resources res = context.getResources();
		muteKey = res.getString(R.string.setting_mute_key);
		residencesKey = res.getString(R.string.setting_residences_key);
		lastUpdatedKey = res.getString(R.string.settings_last_updated_key);
		
		mutePref = sharedPrefs.getBoolean(muteKey, false);
		residencesPref = sharedPrefs.getBoolean(residencesKey, true);
		lastUpdatedOn = sharedPrefs.getLong(lastUpdatedKey, 0);
		
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key.equals(muteKey)){
			mutePref = sharedPreferences.getBoolean(muteKey, false);
		}
		else if(key.equals(residencesKey)){
			residencesPref = sharedPreferences.getBoolean(residencesKey, true);
		}
	}
	
	public boolean isMuted(){
		return mutePref;
	}
	
	public boolean showResidences(){
		return residencesPref;
	}

	public void setMuted(boolean mute){
		mutePref = mute;
		sharedPrefs.edit().putBoolean(muteKey, mutePref).commit();
	}
	
	public void setShowResidences(boolean show){
		residencesPref = show;
		sharedPrefs.edit().putBoolean(residencesKey, residencesPref).commit();
	}
	
	public SharedPreferences getSharedPrefs(){
		return sharedPrefs;
	}

	public long getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(long lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
		sharedPrefs.edit().putLong(lastUpdatedKey, lastUpdatedOn).commit();
	}
}
