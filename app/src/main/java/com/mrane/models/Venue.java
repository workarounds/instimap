package com.mrane.models;


import com.google.gson.annotations.SerializedName;

public class Venue {

    @SerializedName("id")
    long dbId;
    String name;
    @SerializedName("short_name")
    String shortName;
    int group;
    @SerializedName("pixel_x")
    int pixelX;
    @SerializedName("pixel_y")
    int pixelY;
    long parent;
    @SerializedName("parent_relation")
    String parentRelation;
    String description;
    long lat;
    long lng;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPixelX() {
        return pixelX;
    }

    public void setPixelX(int pixelX) {
        this.pixelX = pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public void setPixelY(int pixelY) {
        this.pixelY = pixelY;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public String getParentRelation() {
        return parentRelation;
    }

    public void setParentRelation(String parentRelation) {
        this.parentRelation = parentRelation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public Venue() {}
}
