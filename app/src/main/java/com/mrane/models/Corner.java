package com.mrane.models;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Corner extends ExtendedSugarRecord<Corner> {

    @SerializedName("id")
    long dbId;
    @SerializedName("user_id")
    long userId;
    String tag;
    String name;
    @SerializedName("filter_count")
    int filterCount;
    String header;
    Date created;
    @SerializedName("is_open")
    boolean isOpen;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Corner() {}
}

