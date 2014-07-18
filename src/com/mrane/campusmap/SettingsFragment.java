package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SettingsFragment extends Fragment implements OnClickListener {
	MapActivity mainActivity;
	View rootView;
	CheckBox checkBox;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = MapActivity.getMainActivity();
		rootView = inflater.inflate(R.layout.settings_fragment, container,
				false);
		checkBox = (CheckBox) rootView.findViewById(R.id.mute_checkbox);
		if (mainActivity.muted) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		checkBox.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		Log.d("test123","on click called");
		Editor editor = mainActivity.sharedpreferences.edit();
		editor.putBoolean("mute", checkBox.isChecked());
		Log.d("test123","on click called with bool : " + checkBox.isChecked());
		mainActivity.muted = checkBox.isChecked();
		// mainActivity.audiomanager.setStreamMute(AudioManager.STREAM_MUSIC, mainActivity.muted);
		editor.commit();
	}
}
