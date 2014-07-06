package com.mrane.campusmap;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class IndexFragment extends Fragment {
	
	MainActivity mMainActivity;
	ExpandableListAdapter adapter;
	HashMap<String, Marker> data;
	View rootView;
	ExpandableListView list;
	List<String> headers;
	HashMap<String, List<String>> childData;
	
	public IndexFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainActivity = MainActivity.getmMainActivity();
		setHeaders();
		setChildData();
		adapter = new ExpandableListAdapter(mMainActivity, headers, childData);
		rootView = inflater.inflate(R.layout.index_fragment, container,
				false);
		list = (ExpandableListView) rootView.findViewById(R.id.index_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(mMainActivity);
		
		return rootView;
	}

	private void setChildData() {
		// TODO Auto-generated method stub
		
	}

	private void setHeaders() {
		// TODO Auto-generated method stub
		
	}

}
