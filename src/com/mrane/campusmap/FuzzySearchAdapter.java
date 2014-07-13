package com.mrane.campusmap;

import in.designlabs.instimap.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrane.data.Marker;

public class FuzzySearchAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private List<Marker> resultlist = null;
	private ArrayList<Marker> inputlist;
	private List<ScoredMarker> map;
	private Locale l;

	public FuzzySearchAdapter(Context context, List<Marker> inputlist) {
		mContext = context;
		l = Locale.getDefault();
		this.resultlist = inputlist;
		Collections.sort(resultlist, new MarkerNameComparator());
		inflater = LayoutInflater.from(mContext);
		this.inputlist = new ArrayList<Marker>();
		this.inputlist.addAll(resultlist);
		map = new ArrayList<ScoredMarker>();
	}

	public class ViewHolder {
		TextView label;
		LinearLayout rowContainer;
	}

	public class ScoredMarker {
		Marker m;
		int score;

		public ScoredMarker(int score, Marker m) {
			this.m = m;
			this.score = score;
		}
	}

	public int getResultSize() {
		return resultlist.size();
	}

	@Override
	public int getCount() {
		if (this.getResultSize() == 0) {
			return 1;
		}
		return resultlist.size();
	}

	@Override
	public Marker getItem(int position) {
		return resultlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.row_layout, null);

			holder.label = (TextView) view.findViewById(R.id.label);
			Typeface regular = Typeface.createFromAsset(mContext.getAssets(), MapActivity.FONT_REGULAR);
			holder.label.setTypeface(regular);
			holder.rowContainer = (LinearLayout) view
					.findViewById(R.id.row_container);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews

		if (this.getResultSize() == 0) {
			holder.label.setText("Sorry, no results found.");
		} else {

			if (position == 0) {
				holder.label.setText(Html.fromHtml("<b>"
						+ resultlist.get(position).name + "</b>"));
				// holder.rowContainer.setBackgroundColor(Color.GRAY);
				// holder.rowContainer.getBackground().setAlpha(100);
			} else {
				holder.label.setText(resultlist.get(position).name);
				// holder.rowContainer.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		return view;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		resultlist.clear();
		map.clear();
		if (charText.length() == 0) {
			resultlist.addAll(inputlist);
		} else if (charText.length() > 10) {
			for (Marker m : inputlist) {
				if (m.name.toLowerCase(Locale.getDefault()).contains(charText)) {
					resultlist.add(m);
				}
			}
		} else {
			for (Marker m : inputlist) {
				int score = checkModifyMarker(m, charText);
				if (score != 0) {
					map.add(new ScoredMarker(score, m));
				}
			}
			resultlist = sortByScore(map);
		}
		notifyDataSetChanged();
	}

	private List<Marker> sortByScore(List<ScoredMarker> tempScore) {
		List<Marker> templist = new ArrayList<Marker>();
		Collections.sort(tempScore, new MarkerScoreComparator());
		for (ScoredMarker k : tempScore) {
			templist.add(k.m);
		}
		return templist;
	}

	private int checkModifyMarker(Marker m, String charText) {
		int tempScore = 5;
		String tempCharText = "(.*)";
		for (int i = 0; i < charText.length(); i++) {
			tempCharText += charText.charAt(i) + "(.*)";
		}
		if (m.name.toLowerCase(l).matches(tempCharText)) {
			boolean b = false;
			if (m.name.toLowerCase(l).startsWith(charText)) {
				return 1;
			}
			for (String s : m.name.split(" ")) {
				b = b || s.toLowerCase(l).startsWith("" + charText.charAt(0));
				if (!b) {
					tempScore += 10;
				}
				if (s.startsWith(charText)) {
					return 3;
				}
			}
			if (b) {
				return tempScore;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	public class MarkerScoreComparator implements Comparator<ScoredMarker> {
		public int compare(ScoredMarker m1, ScoredMarker m2) {
			return m1.score - m2.score;
		}
	}

	public class MarkerNameComparator implements Comparator<Marker> {
		public int compare(Marker m1, Marker m2) {
			return m1.name.toLowerCase(l).compareTo(m2.name.toLowerCase(l));
		}
	}
}
