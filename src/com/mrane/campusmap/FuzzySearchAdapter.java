package com.mrane.campusmap;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class FuzzySearchAdapter<T extends Marker> extends ArrayAdapter<T> {

	public FuzzySearchAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public FuzzySearchAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public FuzzySearchAdapter(Context context, int resource,
			int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public FuzzySearchAdapter(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public FuzzySearchAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public FuzzySearchAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

}
