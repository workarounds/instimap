package com.mrane.campusmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.mrane.data.Marker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class IndexFragment extends Fragment implements OnGroupExpandListener {

	MapActivity mainActivity;
	ExpandableListAdapter adapter;
	HashMap<String, Marker> data;
	View rootView;
	ExpandableListView list;
	List<String> headers = new ArrayList<String>();
	HashMap<String, List<String>> childData = new HashMap<String, List<String>>();
	int pos;

	public IndexFragment() {
	}

	@SuppressLint("NewApi")
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
		mainActivity.setExpAdapter(adapter);
		list.setAdapter(adapter);
		list.setOnChildClickListener(mainActivity);
		list.setOnGroupExpandListener(this);

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			list.setIndicatorBounds(list.getRight() - 40, list.getWidth());
		} else {
			list.setIndicatorBoundsRelative(list.getRight() - 40,
					list.getWidth());
		}

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

	@Override
	public void onGroupExpand(int groupPosition) {
		/*
		 * pos = groupPosition; int len = headers.size(); for (int i = 0; i <
		 * len; i++) { if (i != groupPosition) { list.collapseGroup(i); }
		 * list.post(new Runnable() {
		 * 
		 * @Override public void run() { list.smoothScrollToPosition(pos); } });
		 * }
		 */

	}

}
