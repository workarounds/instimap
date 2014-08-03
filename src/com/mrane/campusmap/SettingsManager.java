package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;

public class SettingsManager implements OnSharedPreferenceChangeListener {
	private SharedPreferences sharedPrefs;
	private String muteKey;
	private String residencesKey;
	private boolean mutePref;
	private boolean residencesPref;
	
	public SettingsManager(Context context){
		sharedPrefs = context.getSharedPreferences(MapActivity.PREFERENCE_NAME, Context.MODE_PRIVATE);
		Resources res = context.getResources();
		muteKey = res.getString(R.string.setting_mute_key);
		residencesKey = res.getString(R.string.setting_residences_key);
		
		mutePref = sharedPrefs.getBoolean(muteKey, false);
		residencesPref = sharedPrefs.getBoolean(residencesKey, true);
		
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
}
