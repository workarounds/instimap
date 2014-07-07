package com.mrane.campusmap;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {
	
	MapActivity mainActivity;
	ArrayAdapter<String> adapter;
	HashMap<String, Marker> data;
	View rootView;
	ListView list;
	
	public ListFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = MapActivity.getMainActivity();
		adapter = mainActivity.getAdapter();
		rootView = inflater.inflate(R.layout.list_fragment, container,
				false);
		list = (ListView) rootView.findViewById(R.id.suggestion_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(mainActivity);
		list.setOnTouchListener(mainActivity);
		
		return rootView;
	}

}
