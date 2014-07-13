package com.mrane.campusmap;

import in.designlabs.instimap.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	MapActivity mainActivity;
	public static final String[] MENU_ITEMS = {"Hotels Nearby", "Info for Freshers"};
	View rootView;
	ListView menuListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.menu_fragment, container);
		mainActivity = MapActivity.getMainActivity();
		menuListView = (ListView) rootView.findViewById(R.id.menu_list);
		ArrayList<String> menuList = new ArrayList<String>();
		for(String item : MENU_ITEMS){
			menuList.add(item);
		}
		
		CustomListAdapter adapter = new CustomListAdapter(mainActivity, R.layout.menu_layout, menuList);
		menuListView.setAdapter(adapter);
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private class CustomListAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private int id;
        private List <String>items ;

        private CustomListAdapter(Context context, int textViewResourceId , List<String> list ) 
        {
            super(context, textViewResourceId, list);           
            mContext = context;
            id = textViewResourceId;
            items = list ;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent)
        {
            View mView = v ;
            if(mView == null){
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = vi.inflate(id, null);
            }

            TextView text = (TextView) mView.findViewById(R.id.menu_item);

            if(items.get(position) != null )
            {
                Typeface regular = Typeface.createFromAsset(mContext.getAssets(), MapActivity.FONT_REGULAR);
                text.setText(items.get(position));
                text.setTypeface(regular);
            }

            return mView;
        }

	}
}
