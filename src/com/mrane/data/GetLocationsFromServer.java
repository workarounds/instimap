package com.mrane.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetLocationsFromServer extends AsyncTask<Void, Void, Void> {

	String url = "";
	private final String ID = "id";
	private final String NAME = "name";
	private final String SHORTNAME = "short_name";
	private final String GROUPID = "group_id";
	private final String PIXELX = "pixel_x";
	private final String PIXELY = "pixel_y";
	private final String PARENTID = "parent_id";
	private final String PARENTREL = "parent_rel";
	private final String DESC = "description";
	private final String CHILDIDS = "child_ids";

	public GetLocationsFromServer(String URL) {
		this.url = URL;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Void doInBackground(Void... params) {
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

		if (jsonStr != null) {

			JSONArray markers;
			try {
				markers = new JSONArray(jsonStr);
				
				for(int i=0; i<markers.length(); i++) {
					JSONObject marker = markers.getJSONObject(i);
					String id = marker.getString(ID);
					String name = marker.getString(NAME);
					String shortName = marker.getString(SHORTNAME);
					int groupID = marker.getInt(GROUPID);
					float pixelX = marker.getLong(PIXELX);
					float pixelY = marker.getLong(PIXELY);
					int parentID = marker.getInt(PARENTID);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Dismiss the progress dialog

	}

}
