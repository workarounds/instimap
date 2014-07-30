package com.mrane.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class NewLocations {
	private final String TAG = "NewLocations";

	public HashMap<Integer, String> idMap;
	public HashMap<String, Marker> valueMap;

	/**
	 * gets
	 * 
	 * @param id
	 * @return
	 * @throws CustomException
	 */
	public Marker getMarkerById(int id) throws CustomException {
		String name = null;
		Marker m = null;
		if (idMap.containsKey(id)) {
			name = idMap.get(id);
		} else {
			throw new CustomException("given id doesn't exist in idMap, id = "
					+ id);
		}
		if (!name.isEmpty()) {
			if (valueMap.containsKey(name)) {
				m = valueMap.get(name);
			} else {
				throw new CustomException(
						"somehow id exists but not name, id = " + id
								+ " name = " + name);
			}
		}

		return m;
	}

	/**
	 * gives a marker given it's name
	 * 
	 * @param name
	 * @return
	 * @throws CustomException
	 */
	public Marker getMarkerByName(String name) throws CustomException {
		Marker m = null;
		if (valueMap.containsKey(name)) {
			m = valueMap.get(name);
		} else {
			throw new CustomException("the name doesn't exist, name = " + name);
		}
		return m;
	}

	/**
	 * gets all the markers in valueMap
	 * 
	 * @return
	 */
	public List<Marker> getAllMarkers() {
		List<Marker> l = new ArrayList<Marker>(valueMap.values());
		return l;
	}

	/**
	 * gives a list of childMarkers
	 * @param parentMarker
	 * @return
	 * @throws CustomException
	 */
	public List<Marker> getChildMarkers(Marker parentMarker)
			throws CustomException {
		boolean hasChildren = false;
		int[] childIds = parentMarker.getChildIds();
		List<Marker> childMarkers = new ArrayList<Marker>();
		if (childIds.length > 0) {
			for (int i = 0; i < childIds.length; i++) {
				try {
					getMarkerById(childIds[i]);
					hasChildren = true;
				} catch (CustomException e) {
					Log.e(TAG, "parent `" + parentMarker.getName()
							+ "` has no child with id `" + childIds[i] + "`");
				}
			}
		}

		if (hasChildren) {
			return childMarkers;
		} else {
			throw new CustomException("given marker has no children : "
					+ parentMarker.getName());
		}
	}
	
	/**
	 * gets parent marker if exists else throws CustomException
	 * @param childMarker
	 * @return
	 * @throws CustomException
	 */
	public Marker getParentMarker(Marker childMarker) throws CustomException {
		boolean hasParent = false;
		Marker m = null;
		int parentId = childMarker.getParentId();
		if (parentId != 0) {
			try {
				m = getMarkerById(parentId);
				hasParent = true;
			} catch (CustomException e) {
				Log.e(TAG, "parent with Id `"+ parentId + "` not found for child `" + childMarker.getName()+"`");
			}
		}
		
		if(hasParent) {
			return m;
		} else {
			throw new CustomException("No parent found for child `"+childMarker.getName()+"`");
		}
	}
	
	
}
