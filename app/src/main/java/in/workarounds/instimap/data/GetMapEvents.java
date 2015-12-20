package in.workarounds.instimap.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import in.workarounds.instimap.campusmap.MapActivity;

public class GetMapEvents extends AsyncTask<Void, Void, Void> {

	// Json key names
	private final String ID = "id";
	private final String TITLE = "title";
	private final String VENUE = "venue";
	private final String DATE = "date";
	private final String TIME = "time";
	private final String HEADER = "header";
	private final String DESC = "description";

	// constructor variable (arguments)
	private MapActivity mainActivity;
	private String fileName = "";

	private String jsonStr = null;

	// output vaiables
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, String> eventIdMap = new HashMap<Integer, String>();
	private HashMap<String, MapEvent> eventValueMap = new HashMap<String, MapEvent>();

	public GetMapEvents(String jsonFileName, MapActivity mainActivity) {
		this.fileName = jsonFileName;
		this.mainActivity = mainActivity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Void doInBackground(Void... params) {
		jsonStr = null;
		try {
			FileInputStream fis = mainActivity.openFileInput(fileName);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bufferedReader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			jsonStr = sb.toString();
			Log.d("GetMapEvents", "file read from internal memory");
		} catch (FileNotFoundException e) {
			Log.e("GetMapEvents", "file not yet created: " + fileName);
		} catch (IOException e) {
			Log.e("GetMapEvents", "unable to read file: " + fileName);
		}

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

				Log.d("GetLocations", "parsed json");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("GetMapEvents", "Couldn't get any data from file");
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mainActivity.setEventIdMap(eventIdMap);
		mainActivity.setEventValueMap(eventValueMap);
		mainActivity.onEventsUpdated();
	}

	int[] toIntArray(List<Integer> list) {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)
			ret[i++] = e.intValue();
		return ret;
	}

}
