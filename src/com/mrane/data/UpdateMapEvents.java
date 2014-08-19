package com.mrane.data;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.mrane.campusmap.MapActivity;

public class UpdateMapEvents extends AsyncTask<Void, Void, Void> {

	// Json key names
	private final String ID = "id";
	private final String TITLE = "title";
	private final String VENUE = "venue";
	private final String DATE = "date";
	private final String TIME = "time";
	private final String HEADER = "header";
	private final String DESC = "description";

	// constructor variable (arguments)
	private String fileName;
	private MapActivity mainActivity;
	private String url = "";

	private String jsonStr = null;
	private boolean gotValidJson = false;

	// output vaiables
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, String> eventIdMap = new HashMap<Integer, String>();
	private HashMap<String, MapEvent> eventValueMap = new HashMap<String, MapEvent>();

	public UpdateMapEvents(String URL, String fileName, MapActivity mainActivity) {
		this.url = URL;
		this.mainActivity = mainActivity;
		this.fileName = fileName;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Void doInBackground(Void... params) {
		jsonStr = null;
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

		if (jsonStr != null) {

			JSONArray events;
			try {
				events = new JSONArray(jsonStr);

				for (int i = 0; i < events.length(); i++) {
					JSONObject event = events.getJSONObject(i);
					int id = event.getInt(ID);
					String title = event.getString(TITLE);
					String venue = event.getString(VENUE);
					String date = event.getString(DATE);
					String time = event.getString(TIME);
					String header = event.getString(HEADER);
					String description = event.getString(DESC);
					
					MapEvent me = new MapEvent(id, title, venue, date, time,
							header, description);

					eventIdMap.put(id, title);
					eventValueMap.put(title, me);
				}

				Log.d("UpdateMapEvents", "parsed json");
				gotValidJson = true;

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler: UpdateMapEvents", "Couldn't get any data from the url: " + url);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (gotValidJson) {
			if (jsonStr != null) {
				mainActivity.writeToFile(fileName, jsonStr);
				Log.d("UpdateMapEvents", "file updated: "+ fileName);
			}
		}
		new GetMapEvents(fileName, mainActivity).execute();
	}

	int[] toIntArray(List<Integer> list) {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)
			ret[i++] = e.intValue();
		return ret;
	}

}
