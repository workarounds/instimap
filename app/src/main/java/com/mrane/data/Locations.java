package com.mrane.data;

import com.mrane.models.Venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Locations {
	public HashMap<String, Marker> data = new HashMap<String, Marker>();

    public Locations() {
        List<Venue> venues = Venue.listAll(Venue.class);
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
}