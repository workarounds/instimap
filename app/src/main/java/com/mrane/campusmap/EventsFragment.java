package com.mrane.campusmap;

import in.designlabs.instimap.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mrane.data.MapEvent;

public class EventsFragment extends Fragment {
	MapActivity mainActivity;
	EventsAdapter adapter;
	HashMap<String, MapEvent> data;
	View rootView;
	ListView list;

	public EventsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = MapActivity.getMainActivity();
		rootView = inflater.inflate(R.layout.list_fragment, container, false);

		data = mainActivity.getEventValueMap();
		List<MapEvent> objects = new ArrayList<MapEvent>();
		for (String key : data.keySet()) {
			objects.add(data.get(key));
		}
		adapter = new EventsAdapter(mainActivity, R.layout.event_item, objects);
		list = (ListView) rootView.findViewById(R.id.suggestion_list);
		list.setAdapter(adapter);
		list.setFastScrollEnabled(true);
		return rootView;
	}

	public void onDataChanged() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
