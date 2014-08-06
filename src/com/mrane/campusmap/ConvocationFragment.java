package com.mrane.campusmap;

import in.designlabs.instimap.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ConvocationFragment extends Fragment {
	private ListView venuesListView;
	
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.convocation_fragment, container, false);
			venuesListView = (ListView)root.findViewById(R.id.convocation_venues_list);
			initList();
			setListViewHeightBasedOnChildren(venuesListView);
			
			//applyFonts(container);
			
			return root;
		}
		
		private void initList() {
			String[] markerNames = {"Convocation Hall", "Lecture Hall Complex - 1 & 2", "Victor Menezes Convention Centre"};
			Venue convo = new Venue("Convocation Hall", "VVIP and Media", R.drawable.yellow_blue_dot, markerNames[0]);
			Venue lhc = new Venue("Lecture Hall Complex 1, 2, 3 & 4", "Parents and Friends", R.drawable.green_dot, markerNames[1]);
			Venue vmcc = new Venue("VMCC, Main Auditorium", "Faculty and Staff members", R.drawable.red_dot, markerNames[2]);
			ArrayList<Venue> venueList = new ArrayList<Venue>();
			venueList.add(convo);
			venueList.add(lhc);
			venueList.add(vmcc);
			
			ArrayList<String> names = new ArrayList<String>();
			for(String name : markerNames){
				names.add(name);
			}
			
			VenueListAdapter adapter = new VenueListAdapter(getActivity(), R.layout.convocation_venue, venueList, names);
			venuesListView.setAdapter(adapter);
		}
		
		private class Venue{
			public String name;
			public String title;
			public String subTitle;
			public int drawableId;
			
			public Venue(String title, String subTitle, int drawableId, String name){
				this.title = title;
				this.subTitle = subTitle;
				this.drawableId = drawableId;
				this.name = name;
			}
		}
		
		private class VenueListAdapter extends ArrayAdapter<String> {

			private Context mContext;
			private int id;
			private List<Venue> venues;


			public VenueListAdapter(Context context, int viewResourceId,
					List<Venue> venueList, List<String> names) {
				super(context, viewResourceId, names);
				mContext = context;
				id = viewResourceId;
				venues = venueList;
			}

			@Override
			public View getView(int position, View v, ViewGroup parent) {
				View mView = v;
				if (mView == null) {
					LayoutInflater vi = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					mView = vi.inflate(id, null);
				}

				TextView titleTextView = (TextView) mView.findViewById(R.id.convocation_venue_title);
				TextView subTitleTextView = (TextView) mView.findViewById(R.id.convocation_venue_subtitle);
				ImageView dotImage = (ImageView) mView.findViewById(R.id.convocation_venue_color);
				if (venues.get(position) != null) {
					//Typeface regular = Typeface.createFromAsset(getActivity().getAssets(),
							//MapActivity.FONT_REGULAR);
					SpannableString content = new SpannableString(venues.get(position).title);
					content.setSpan(new UnderlineSpan(), 0, venues.get(position).title.length(), 0);
					//titleTextView.setTypeface(regular);
					titleTextView.setText(content);
					titleTextView.setTextColor(Color.rgb(19, 140, 190));
					
					
					//subTitleTextView.setTypeface(regular);
					subTitleTextView.setText(venues.get(position).subTitle);
					dotImage.setImageResource(venues.get(position).drawableId);
				}

				return mView;
			}

		}
		
		public static void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null)
				return;

			int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
					MeasureSpec.UNSPECIFIED);
			int totalHeight = 0;
			View view = null;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				view = listAdapter.getView(i, view, listView);
				if (i == 0)
					view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
							LayoutParams.WRAP_CONTENT));

				view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
				totalHeight += view.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
			listView.requestLayout();
		}

		private void applyFonts(ViewGroup root) {
			Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), MapActivity.FONT_REGULAR); 
			ArrayList<View> textNormalViews = getViewsByTag(root, "text_normal");
			for(View textNormalView : textNormalViews){
				try{
					TextView v = (TextView)textNormalView;
					v.setTypeface(normal);
				}
				catch(ClassCastException e){
					Log.d("ConvocationFragment.java", "view not a textview\n");
				}
			}
		}

		private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
		    ArrayList<View> views = new ArrayList<View>();
		    final int childCount = root.getChildCount();
		    for (int i = 0; i < childCount; i++) {
		        final View child = root.getChildAt(i);
		        if (child instanceof ViewGroup) {
		            views.addAll(getViewsByTag((ViewGroup) child, tag));
		        } 

		        final Object tagObj = child.getTag();
		        if (tagObj != null && tagObj.equals(tag)) {
		            views.add(child);
		        }

		    }
		    return views;
		}
}
