package com.mrane.campusmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class IndexFragment extends Fragment {

	MapActivity mainActivity;
	ExpandableListAdapter adapter;
	HashMap<String, Marker> data;
	View rootView;
	ExpandableListView list;
	List<String> headers = new ArrayList<String>();
	HashMap<String, List<String>> childData = new HashMap<String, List<String>>();

	public IndexFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = MapActivity.getMainActivity();
		data = mainActivity.data;
		if (headers.isEmpty()) {
			setHeaderAndChildData();
		}
		adapter = new ExpandableListAdapter(mainActivity, headers, childData);
		rootView = inflater.inflate(R.layout.index_fragment, container, false);
		list = (ExpandableListView) rootView.findViewById(R.id.index_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(mainActivity);

		return rootView;
	}

	private void setChildData() {
		Collection<Marker> keys = data.values();
		for (Marker key : keys) {
			List<String> child = childData.get(key.getGroupName());
			child.add(key.name);
		}
	}

	private void setHeaderAndChildData() {
		String[] headerString = Marker.getGroupNames();
		Collections.addAll(headers, headerString);
		for (String header : headers) {
			childData.put(header, new ArrayList<String>());
		}
		setChildData();
	}

}
