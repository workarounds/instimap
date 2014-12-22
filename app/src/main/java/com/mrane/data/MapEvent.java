package com.mrane.data;

public class MapEvent {
	private int id;
	private String title;
	private String venue;
	private String date;
	private String time;
	private String header;
	private String description;

	public MapEvent(int id, String title, String venue, String date,
			String time, String header, String description) {
		this.id = id;
		this.title = title;
		this.venue = venue;
		this.date = date;
		this.time = time;
		this.header = header;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
