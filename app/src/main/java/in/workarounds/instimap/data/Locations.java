package in.workarounds.instimap.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.workarounds.instimap.models.Venue;
import in.workarounds.instimap.parser.CustomGson;

public class Locations {
	public HashMap<String, Marker> data = new HashMap<String, Marker>();
    private Context context;
    private final String JSON_FILE = "data.json";
    private static Locations mLocations;

    private Locations(Context context) {
        this.context = context;
        List<Venue> venues = Venue.listAll(Venue.class);
        if(venues.isEmpty()) {
            venues = this.populateFromJson();
        }

        for (Venue venue: venues) {
            String parentName = null;
            List<String> childList = new ArrayList<String>();
            if(venue.getParent() != 0) {
                List<Venue> parents = Venue.find(Venue.class, "db_id=?", Long.toString(venue.getParent()));
                if(!parents.isEmpty()) {
                    parentName = parents.get(0).getName();
                }
            } else {
                List<Venue> children = Venue.find(Venue.class, "parent=?", Long.toString(venue.getDbId()));
                if(!children.isEmpty()) {
                    for (Venue child: children) {
                        childList.add(child.getName());
                    }
                }
            }

            Marker marker = null;
            if(parentName != null) {
                marker = new Room(venue.getName(), venue.getShortName(), venue.getPixelX(),
                        venue.getPixelY(), venue.getGroupId(), parentName,
                        venue.getParentRelation(), venue.getDescription());
            } else if(!childList.isEmpty()) {
                String childNames[] = childList.toArray(new String[0]);
                marker = new Building(venue.getName(), venue.getShortName(), venue.getPixelX(),
                        venue.getPixelY(),venue.getGroupId(), childNames, venue.getDescription());
            } else {
                marker = new Marker(venue.getName(), venue.getShortName(), venue.getPixelX(),
                        venue.getPixelY(), venue.getGroupId(), venue.getDescription());
            }
            data.put(venue.getName(), marker);
        }
    }

    public static Locations getInstance(Context context){
        if(mLocations == null){
            mLocations = new Locations(context);
        }
        return mLocations;
    }

    public Marker getMarkerById(Long id){
        List<Venue> venues = Venue.find(Venue.class, "db_id = ?", id.toString());
        if(!venues.isEmpty()){
            String name = venues.get(0).getName();
            return data.get(name);
        }
        return null;
    }

    private List<Venue> populateFromJson() {
        String venueJson = this.readFromAssets();
        CustomGson customGson = new CustomGson();
        Gson gson = customGson.getGson();
        Type listType = new TypeToken<ArrayList<Venue>>() {}.getType();
        List<Venue> venues = gson.fromJson(venueJson, listType);
        for (Venue v: venues) {
            v.saveOrUpdate(Venue.class, v.getDbId());
        }

        return venues;
    }

    private String readFromAssets() {
        String fileContent = "";

        try {
            InputStream stream = context.getAssets().open(JSON_FILE);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            fileContent = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return fileContent;
    }
}