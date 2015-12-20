package in.workarounds.instimap.campusmap;

import in.designlabs.instimap.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.workarounds.instimap.data.MapEvent;

public class EventsAdapter extends ArrayAdapter<MapEvent> {
	Context context;

	public EventsAdapter(Context context, int resource, List<MapEvent> objects) {
		super(context, resource, objects);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView title;
		TextView venue;
		TextView time;
		TextView date;
		TextView header;
		TextView description;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MapEvent mapEvent = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.event_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.venue = (TextView) convertView.findViewById(R.id.venue);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.header = (TextView) convertView.findViewById(R.id.header);
			holder.description = (TextView) convertView.findViewById(R.id.description);
			
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
			holder.title.setText(mapEvent.getTitle());
			holder.venue.setText(mapEvent.getVenue());
			holder.time.setText(mapEvent.getTime());
			holder.date.setText(mapEvent.getDate());
			holder.header.setText(mapEvent.getHeader());
			holder.description.setText(mapEvent.getDescription());
			
		return convertView;
	}

}
